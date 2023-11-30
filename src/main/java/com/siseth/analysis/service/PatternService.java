package com.siseth.analysis.service;

import com.siseth.analysis.dto.analysis.request.AnalysisCreateReqDTO;
import com.siseth.analysis.dto.analysis.request.AnalysisDetailReqDTO;
import com.siseth.analysis.dto.analysis.request.AnalysisIdReqDTO;
import com.siseth.analysis.dto.analysis.request.AnalysisUpdateReqDTO;
import com.siseth.analysis.dto.analysis.response.AnalysisDetailsResDTO;
import com.siseth.analysis.dto.analysis.response.AnalysisResDTO;
import com.siseth.analysis.dto.pattern.CreatePatternReqDTO;
import com.siseth.analysis.dto.pattern.UpdatePatternReqDTO;
import com.siseth.analysis.entity.Analysis;
import com.siseth.analysis.entity.AnalysisDetail;
import com.siseth.analysis.entity.Pattern;
import com.siseth.analysis.feign.dataSource.DataSourceAdapterService;
import com.siseth.analysis.repository.AnalysisDetailRepository;
import com.siseth.analysis.repository.AnalysisRepository;
import com.siseth.analysis.repository.PatternRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatternService {

    private final AnalysisRepository analysisRepository;
    private final AnalysisDetailRepository analysisDetailRepository;

    private final DataSourceAdapterService sourceAdapterService;

    private final PatternRepository patternRepository;

    public Object create(CreatePatternReqDTO api, String userId, String realm){
        Pattern pattern = new Pattern(userId, realm).update(api);
        return patternRepository.save(pattern);
    }

    public Object update(UpdatePatternReqDTO api, String userId, String realm){
        Pattern pattern = patternRepository.findByIdAndUserIdAndRealm(api.getId(), userId, realm)
                                        .orElseThrow( () -> new RuntimeException("Not found!"));
        pattern.update(api);
        return patternRepository.save(pattern);
    }

    public void delete(Long patternId, String userId, String realm) {
        Pattern pattern = patternRepository.findByIdAndUserIdAndRealm(patternId, userId, realm)
                .orElseThrow( () -> new RuntimeException("Pattern not found!"));

        pattern.setIsActive(false);
        patternRepository.save(pattern);
    }

    public Pattern getPattern(Long patternId, String userId, String realm) {
        return patternRepository.findByIdAndUserIdAndRealm(patternId, userId, realm)
                        .orElseThrow( () -> new RuntimeException("Pattern not found!"));
    }

    public List<Pattern> getAllPattern(String userId, String realm) {
        return patternRepository.findAllByUserIdAndRealm(userId, realm);
    }

}
