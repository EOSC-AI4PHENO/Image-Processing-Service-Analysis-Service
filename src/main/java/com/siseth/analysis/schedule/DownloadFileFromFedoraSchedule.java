package com.siseth.analysis.schedule;

import com.siseth.analysis.entity.Analysis;
import com.siseth.analysis.entity.AnalysisDetail;
import com.siseth.analysis.entity.SourcePreVerification;
import com.siseth.analysis.feign.dataSource.DataSourceAdapterService;
import com.siseth.analysis.feign.dataSource.DataSourceAdapterServiceImpl;
import com.siseth.analysis.feign.dataSource.dto.ImageSourceShortResDTO;
import com.siseth.analysis.feign.fedora.FedoraService;
import com.siseth.analysis.feign.fedora.FedoraServiceImpl;
import com.siseth.analysis.feign.fedora.dto.FileShortResDTO;
import com.siseth.analysis.feign.roi.ROIService;
import com.siseth.analysis.feign.roi.dto.RoiResDTO;
import com.siseth.analysis.repository.AnalysisDetailRepository;
import com.siseth.analysis.repository.AnalysisRepository;
import com.siseth.analysis.repository.SourcePreVerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.siseth.analysis.constant.AppProperty.DOWNLOAD_FILE_FROM_FEDORA_SCHEDULE;

@Component
@RequiredArgsConstructor
@Slf4j
public class DownloadFileFromFedoraSchedule {

    private final AnalysisRepository analysisRepository;
    private final AnalysisDetailRepository analysisDetailRepository;
    private final SourcePreVerificationRepository sourcePreVerificationRepository;
    private final FedoraServiceImpl fedoraService;
    private final DataSourceAdapterServiceImpl dataSourceAdapterService;

    private LocalDate from;

    private LocalDate to;

    @Scheduled(initialDelay = 5000L, fixedRate = 60000L)
    public void schedule() {
        log.debug("Check DownloadFileFromFedoraSchedule");
        if(!DOWNLOAD_FILE_FROM_FEDORA_SCHEDULE)
            return;
        log.debug("Start DownloadFileFromFedoraSchedule");

        LocalDate defaultDateFrom =  LocalDate.now().minusDays(1000);
        LocalDate defaultDateTo =  LocalDate.now();

        for (Analysis analysis : analysisRepository.findAllByToProcessIsTrue()) {
            this.from = Optional.ofNullable(analysis.getDateFrom()).orElse(defaultDateFrom);
            this.to = Optional.ofNullable(analysis.getDateTo()).orElse(defaultDateTo);
            ImageSourceShortResDTO source = get(analysis);
            if(source == null)
                continue;
            List<FileShortResDTO> files = getFiles(analysis);
            for (FileShortResDTO file : files) {
                if(analysisDetailRepository.existsByAnalysisAndImageId(analysis, file.getId()))
                    continue;

                SourcePreVerification verification = get(file.getId());
                boolean verify = Analysis.AnalysisType.toVerification().contains(analysis.getType());
                if(verification == null && verify)
                    createVerification(file, source);
                else if(!verify || (verification.isCorrect()))
                    createDetail(analysis, file);
            }
        }
        log.debug("END OF SCHEDULE");


    }

    private void createDetail(Analysis analysis, FileShortResDTO file) {
        Long roiId = analysis.getRoiId();
        analysisDetailRepository.save(
                                    new AnalysisDetail(analysis)
                                                        .create(file, roiId)
                                    );
    }
    private void createVerification(FileShortResDTO file, ImageSourceShortResDTO source) {
        sourcePreVerificationRepository.save(
                new SourcePreVerification()
                            .create(file)
                            .addSource(source)
                                            );
    }
    private List<FileShortResDTO> getFiles(Analysis analysis) {
        return fedoraService.getFileImageToSource(analysis.getSourceId(),
                                                        from,
                                                        to,
                                                        analysis.getRealm());
    }

    private SourcePreVerification get(Long imageId) {
        return sourcePreVerificationRepository.findByImageId(imageId).orElse(null);
    }

    private ImageSourceShortResDTO get(Analysis analysis) {
        return dataSourceAdapterService.getSource(analysis.getSourceId());
    }


}
