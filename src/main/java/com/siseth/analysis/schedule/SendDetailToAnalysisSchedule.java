package com.siseth.analysis.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siseth.analysis.component.analysis.AnalysisParameterConverter;
import com.siseth.analysis.component.analysis.BigDecimalUtil;
import com.siseth.analysis.entity.*;
import com.siseth.analysis.entity.AnalysisDetail;
import com.siseth.analysis.entity.AnalysisParameter;
import com.siseth.analysis.entity.AnalysisResult;
import com.siseth.analysis.feign.fedora.FedoraService;
import com.siseth.analysis.feign.fedora.FedoraServiceImpl;
import com.siseth.analysis.feign.imageProcessing.ImageProcessingAdapterService;
import com.siseth.analysis.feign.imageProcessing.dto.request.AutomaticRoiFeignDTO;
import com.siseth.analysis.feign.imageProcessing.dto.request.CustomRoiFeignDTO;
import com.siseth.analysis.feign.roi.ROIService;
import com.siseth.analysis.feign.roi.dto.RoiResDTO;
import com.siseth.analysis.feign.roi.dto.UploadRoiDTO;
import com.siseth.analysis.repository.AnalysisDetailRepository;
import com.siseth.analysis.repository.AnalysisParameterRepository;
import com.siseth.analysis.repository.AnalysisResultRepository;
import com.siseth.analysis.repository.SourcePreVerificationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static com.siseth.analysis.constant.AppProperty.SEND_DETAIL_TO_ANALYSIS_SCHEDULE;

@Component
@AllArgsConstructor
@Transactional
@Slf4j
public class SendDetailToAnalysisSchedule {

    private final AnalysisDetailRepository analysisDetailRepository;
    private final AnalysisParameterRepository analysisParameterRepository;
    private final AnalysisResultRepository analysisResultRepository;
    private final SourcePreVerificationRepository sourcePreVerificationRepository;


    private final ROIService roiFeign;
    private final FedoraServiceImpl fedoraFeign;
    private final ImageProcessingAdapterService imageProcessingFeign;


    private Map<String,AnalysisParameter> map;


    @Scheduled(initialDelay = 3000L, fixedRate = 30000L)
    public void schedule() {
        log.debug("Check SendDetailToAnalysis");
        if(!SEND_DETAIL_TO_ANALYSIS_SCHEDULE)
            return;
        log.debug("Start SendDetailToAnalysis");

        List<AnalysisParameter> parameters = analysisParameterRepository.findAll();

        map = AnalysisParameterConverter.toMap(parameters);



        List<AnalysisDetail> analysisDetailList = analysisDetailRepository.findAllByStatusOrderByCreatedAt(AnalysisDetail.AnalysisStatus.ADDED);

        List<AnalysisDetail> analysisDetailListProgress = analysisDetailRepository.findAllByStatus(AnalysisDetail.AnalysisStatus.IN_PROGRESS).
                orElseThrow( () -> new RuntimeException("Failed to get analysisDetails"));

        // ***********************
        // START PROCESSING IMAGES
        // ***********************

        long appleMethodCount = analysisDetailListProgress.stream()
                .filter(x -> x.getAnalysis().getType().equals(Analysis.AnalysisType.APPLE))
                .count();

        if (appleMethodCount < 10) {
            analysisDetailList.stream().limit(50)
                    .forEach(analysisDetail -> {
//                        boolean check = sourcePreVerificationRepository.findByImageId(analysisDetail.getImageId())
//                                .map(SourcePreVerification::isCorrect)
//                                .orElse(true);
//                        if (!check)
//                            return;

                        switch (analysisDetail.getAnalysis().getType()) {
                            case LINDEN_BASIC:
                                lindenBasicStart(analysisDetail);
                                break;
                            case LINDEN_EXTENDED:
                                lindenExtendedStart(analysisDetail);
                                break;
                            case APPLE:
                                appleBasicStart(analysisDetail);
                                break;
                            default:
                                throw new RuntimeException("Wrong type");
                        }
                    });
        }


        // ***********************
        // START GET RESULTS FROM PROCESSED IMAGES
        // ***********************

        analysisDetailList = analysisDetailRepository.findAllByStatusOrderByCreatedAt(AnalysisDetail.AnalysisStatus.IN_PROGRESS);

        analysisDetailList
                .forEach(analysisDetail -> {
                    switch (analysisDetail.getAnalysis().getType()){
                        case LINDEN_BASIC:
                            lindenBasicGetResults(analysisDetail);
                            break;
                        case LINDEN_EXTENDED:
                            lindenExtendedGetResults(analysisDetail);
                            break;
                        case APPLE:
                            appleBasicGetResults(analysisDetail);
                            break;
                        default:
                            throw new RuntimeException("Wrong type");
                    }
        });

        log.debug("The End of SendDetailToAnalysis");


        analysisDetailList = analysisDetailRepository.findAllByStatusAndIsCashedIsTrue(AnalysisDetail.AnalysisStatus.FINISHED);

        analysisDetailList
                .forEach(analysisDetail -> {
                    log.debug("deleted task id: " + analysisDetail.getTaskId());
                    try {
                        imageProcessingFeign.deleteTask(analysisDetail.getTaskId());
                    } catch (Exception e) {
                        log.debug("failed to delete task");
                    }
                    analysisDetail.deleteCash();
                    analysisDetailRepository.save(analysisDetail);
                });
    }






    private void  lindenBasicStart(AnalysisDetail analysisDetail) {
        byte[] imageByte = fedoraFeign.getImage(analysisDetail.getImageId());
        byte[] roiByte = fedoraFeign.getRoi(analysisDetail.getRoiId());

        try {
            String taskId = imageProcessingFeign.lindenWithIndicators(new CustomRoiFeignDTO(imageByte, "file.jpg", roiByte)).getTask_id();
            log.debug("started task: " + taskId);
            analysisDetail.setToInProgress(taskId);
        }catch (Exception e) {
            analysisDetail.failed();
        } finally {
            analysisDetailRepository.save(analysisDetail);
        }
    }

    private void lindenBasicGetResults(AnalysisDetail detail) {
        try {
            Map<String, Object> result = imageProcessingFeign.lindenWithIndicatorsResult(detail.getTaskId());
            if (result.getOrDefault("status", "").equals("Success")) {
                _saveResults(detail, result);
            }
        } catch (Exception e){
            log.debug("FAILED TO GET RESULTS FOR THE TASK: " + detail.getTaskId());
            detail.failed();
            analysisDetailRepository.save(detail);
        }
    }

    private void appleBasicStart(AnalysisDetail analysisDetail) {
        String taskId;
        byte[] imageByte = fedoraFeign.getImage(analysisDetail.getImageId());

        try {
            if (analysisDetail.getRoiId() != null) {
                byte[] roiByte = fedoraFeign.getRoi(analysisDetail.getRoiId());
                taskId = imageProcessingFeign.appleAutomaticRoisWithIndicators(new CustomRoiFeignDTO(imageByte, "file.jpg", roiByte)).getTask_id();
            } else {
                taskId = imageProcessingFeign.appleAutomaticRoisWithIndicators(new AutomaticRoiFeignDTO(imageByte, "file.jpg")).getTask_id();
            }
            analysisDetail.setToInProgress(taskId);
            log.debug("started task: " + taskId);
        } catch (Exception e) {
            analysisDetail.failed();
        } finally {
            analysisDetailRepository.save(analysisDetail);
        }
    }

    private void appleBasicGetResults(AnalysisDetail detail) {
        try{
            Map<String, Object> result = imageProcessingFeign.appleAutomaticRoisWithIndicatorsResult(detail.getTaskId());
            if (result.getOrDefault("status", "").equals("Success")) {
                _saveResults(detail, result);

            }
        } catch (Exception e){
            log.debug("FAILED TO GET RESULTS FOR THE TASK: " + detail.getTaskId());
            detail.failed();
            analysisDetailRepository.save(detail);
        }
    }

    private void lindenExtendedStart(AnalysisDetail analysisDetail) {
        byte[] imageByte = fedoraFeign.getImage(analysisDetail.getImageId());

        try {
            String taskId = imageProcessingFeign.lindenAutomaticRoisWithIndicators(new AutomaticRoiFeignDTO(imageByte, "test")).getTask_id();
            log.debug("started task: " + taskId);
            analysisDetail.setToInProgress(taskId);
        } catch (Exception e) {
            analysisDetail.failed();
        } finally {
            analysisDetailRepository.save(analysisDetail);
        }
    }

    private void lindenExtendedGetResults(AnalysisDetail detail) {
        try {
            Map<String, Object> result = imageProcessingFeign.lindenAutomaticRoisWithIndicatorsResult(detail.getTaskId());
            if (result.getOrDefault("status", "").equals("Success")) {
                _saveResults(detail, result);
            }
        } catch (Exception e) {
            log.debug("FAILED TO GET RESULTS FOR THE TASK: " + detail.getTaskId());
            detail.failed();
            analysisDetailRepository.save(detail);
        }
    }


    private void _saveResults(AnalysisDetail analysisDetail, Map<String, Object> result) {

        this.map.forEach(
                (key, value) -> {

                   BigDecimal valueDetail = BigDecimalUtil.cast(result.get(key));

                   if(valueDetail != null ) {
                       AnalysisResult analysisResult = new AnalysisResult(analysisDetail,
                               value,
                               valueDetail,
                               true);
                       analysisResultRepository.save(analysisResult);
                   }
                }
        );

        try {
            String json;
            if (result.get("jsonBase64AppleROIs") != null) {
                json = new String(Base64.getUrlDecoder().decode((String) result.get("jsonBase64AppleROIs")));

                RoiResDTO roiRes = roiFeign.addRoi(new UploadRoiDTO(new ObjectMapper().readValue(json, Object.class), analysisDetail.getAnalysis().getSourceId()),
                        analysisDetail.getAnalysis().getUserId(),
                        analysisDetail.getAnalysis().getRealm());

                analysisDetail.setRoiResultId(roiRes.getFileId());
            }
            if (result.get("jsonBase64LindenROIs") != null) {
                json = new String(Base64.getUrlDecoder().decode((String) result.get("jsonBase64LindenROIs")));

                RoiResDTO roiRes = roiFeign.addRoi(new UploadRoiDTO(new ObjectMapper().readValue(json, Object.class), analysisDetail.getAnalysis().getSourceId()),
                        analysisDetail.getAnalysis().getUserId(),
                        analysisDetail.getAnalysis().getRealm());

                analysisDetail.setRoiResultId(roiRes.getFileId());
            }


        } catch (Exception e){
            log.debug("there is not ROI for this analysis");
        }




        analysisDetail.finished();
        analysisDetailRepository.save(analysisDetail);
    }

}
