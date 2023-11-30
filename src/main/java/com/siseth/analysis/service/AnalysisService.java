package com.siseth.analysis.service;

import com.siseth.analysis.component.paggingSorting.SortingConverter;
import com.siseth.analysis.dto.analysis.request.AnalysisCreateReqDTO;
import com.siseth.analysis.dto.analysis.request.AnalysisDetailReqDTO;
import com.siseth.analysis.dto.analysis.request.AnalysisIdReqDTO;
import com.siseth.analysis.dto.analysis.request.AnalysisUpdateReqDTO;
import com.siseth.analysis.dto.analysis.response.AnalysisDetailsResDTO;
import com.siseth.analysis.dto.analysis.response.AnalysisResDTO;
import com.siseth.analysis.entity.Analysis;
import com.siseth.analysis.entity.AnalysisDetail;
import com.siseth.analysis.entity.AnalysisParameter;
import com.siseth.analysis.feign.dataSource.DataSourceAdapterService;
import com.siseth.analysis.feign.dataSource.dto.ImageSourceShortResDTO;
import com.siseth.analysis.repository.AnalysisDetailRepository;
import com.siseth.analysis.repository.AnalysisParameterRepository;
import com.siseth.analysis.repository.AnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final AnalysisRepository analysisRepository;
    private final AnalysisDetailRepository analysisDetailRepository;
    private final AnalysisParameterRepository analysisParameterRepository;

    private final DataSourceAdapterService sourceAdapterService;


    public Boolean isIncremental(Long sourceId, String userId, String realm) {
        List<Analysis> analysisList = analysisRepository.findAllByUserIdAndSourceIdAndRealmAndIncrementalIsTrueAndToProcessIsTrue(userId, sourceId, realm);

        return !analysisList.isEmpty();
    }

    public Object create(AnalysisCreateReqDTO api,
                         String userId,
                         String realm) {

        Analysis analysis = new Analysis(userId, realm)
                                                .create(api)
                                                .valid();
        analysisRepository.save(analysis);
        return analysis.getId();
    }

    public Object update(AnalysisUpdateReqDTO api,
                         String userId,
                         String realm) {

        Analysis analysis =  analysisRepository.findByIdAndRealmAndUserId(api.getAnalysisId(), realm, userId)
                                        .orElseThrow( () -> new RuntimeException("Analysis not found"));

        analysis.create(api).valid();
        analysisRepository.save(analysis);
        return analysis.getId();
    }

    public Object startAnalysis(AnalysisIdReqDTO api,
                                String userId,
                                String realm) {
        Analysis analysis =  analysisRepository.findByIdAndRealmAndUserId(api.getAnalysisId(), realm, userId)
                                            .orElseThrow( () -> new RuntimeException("Analysis not found"));
        analysis.start();
        analysisRepository.save(analysis);
        return analysis.getId();
    }

    public Object enableIncremental(Long sourceId,
                                    String userId,
                                    String realm) {
        List<Analysis> analysisList = analysisRepository.findAllByUserIdAndSourceIdAndRealmAndIncrementalIsTrueAndToProcessIsTrue(userId, sourceId, realm);
        if (!analysisList.isEmpty()){
            throw new RuntimeException("Only 1 incremental analysis possible");
        }

        ImageSourceShortResDTO source = sourceAdapterService.getSource(sourceId);

        if (!userId.equals(source.getUserId())){
            throw new RuntimeException("User is not assign to this data source");
        }




        Analysis analysis =
                analysisRepository.findAllByUserIdAndSourceIdAndRealmAndIncrementalIsTrueAndToProcessIsFalse(userId, sourceId, realm)
                        .stream()
                        .findFirst()
                        .orElse(new Analysis(userId, realm)
                                                    .createIncremental(source)
                                                    .valid());

            analysis.enableIncremental();

        analysisRepository.save(analysis);
        return analysis.getId();
    }

    public Object disableIncremental(Long sourceId,
                                    String userId,
                                    String realm) {
        Analysis analysis = analysisRepository.findByUserIdAndRealmAndSourceIdAndIncrementalIsTrue(userId, realm, sourceId).orElseThrow( () -> new RuntimeException("Not found incremental analysis"));

        analysis.disableIncremental();
        analysisRepository.save(analysis);

        return analysis.getId();
    }


    public AnalysisResDTO getAnalysis(Long analysisId, String realm, String userId) {
        Analysis analysis =  analysisRepository.findByIdAndRealmAndUserId(analysisId, realm, userId).orElseThrow( () -> new RuntimeException("Analysis not found"));

        return new AnalysisResDTO(analysis);
    }

    public void deleteAnalysis(Long analysisId, String realm, String userId) {
        Analysis analysis = analysisRepository.findByIdAndRealmAndUserId(analysisId, realm, userId).orElseThrow( () -> new RuntimeException("Analysis not found"));
        analysis.delete();
        analysisRepository.save(analysis);
    }

    public List<AnalysisResDTO> getUserAnalysis(String id, String realm,
                                                Long sourceId,
                                                String sorting,
                                                Analysis.AnalysisType type, Analysis.AnalysisStatus status,
                                                LocalDate dateFrom, LocalDate dateTo) {
        Comparator<AnalysisResDTO> dynamicComparator = createComparator(sorting);

        return analysisRepository.getAllAnalysis(id, realm, sourceId, dateFrom, dateTo)
                .stream()
                .map(AnalysisResDTO::new)
                .filter(x -> status == null || x.getStatus().equals(status))
                .filter(x -> type == null || x.getType().equals(type))
                .sorted(dynamicComparator)
                .collect(Collectors.toList());
    }

    public List<AnalysisDetailsResDTO> getAnalysisDetails(Long analysisId) {
        return analysisDetailRepository
                .findAllByAnalysisIdOrderByCreatedAtDesc(analysisId)
                .stream()
                .map(AnalysisDetailsResDTO::new)
                .collect(Collectors.toList());
    }

    public List<AnalysisResDTO> getUserAnalysisBySource(String id, String realm, Long sourceId) {
        List<Analysis> analysisList =  analysisRepository.findAllByUserIdAndRealmAndSourceId(id, realm, sourceId);

        return analysisList.stream().map(AnalysisResDTO::new)
                .collect(Collectors.toList());
    }

    public String addDetail(AnalysisDetailReqDTO api, String userId, String realm) {
        Analysis analysis = analysisRepository.findById(api.getAnalysisId())
                            .orElseThrow( () -> new RuntimeException("Analysis not found!"));
        AnalysisDetail analysisDetail = new AnalysisDetail(analysis).create(api.getImageId(), api.getRoiId());
        analysisDetailRepository.save(analysisDetail);
        return "Successfully added detail to analysis: " + analysis.getName() + " of id: " + analysis.getId();
    }


    @SuppressWarnings("unchecked")
    private Comparator<AnalysisResDTO> createComparator(String sort) {
        Comparator<AnalysisResDTO> comparator = (analysis1, analysis2) -> 0;
        if(sort == null || sort.equals(""))
            return comparator;

        for (String s : sort.split(",")) {

            String field = s.split(":")[0];
            String order = s.split(":")[1];

            Comparator<AnalysisResDTO> critComparator = Comparator.comparing(
                    analysis -> (Comparable) getFieldValue(analysis, field)
            );

            if (order.equals("DESC")) {
                critComparator = critComparator.reversed();
            }
            comparator = comparator.thenComparing(critComparator);
        }

        return comparator;
    }

    public Object getFieldValue(AnalysisResDTO analysisResDTO, String fieldName) {
        if(fieldName.equals("algorithmName"))
            return analysisResDTO.getAlgorithmName();
        if(fieldName.equals("type"))
            return analysisResDTO.getType();
        if(fieldName.equals("createdAt"))
            return analysisResDTO.getCreatedAt();
        if(fieldName.equals("status"))
            return analysisResDTO.getStatus();
        return analysisResDTO.getId();
    }

}
