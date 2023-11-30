package com.siseth.analysis.repository;

import com.siseth.analysis.entity.AnalysisDetail;
import com.siseth.analysis.entity.AnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnalysisResultRepository extends JpaRepository<AnalysisResult, Long> {

    Optional<List<AnalysisResult>> findAllByDetail(AnalysisDetail detail);

    @Query("SELECT ar " +
            "FROM AnalysisResult ar " +
            "INNER JOIN AnalysisParameter p ON ar.parameter = p " +
            "INNER JOIN AnalysisDetail ad ON ar.detail = ad " +
            "INNER JOIN Analysis a ON ad.analysis = a " +
            "WHERE a.isActive = TRUE AND ar.isActive = TRUE AND ad.isActive = TRUE AND " +
            "      a.id = :analysisId AND p.id IN :parameterIds ")
    List<AnalysisResult> getAnalysisResultToAnalysisAndParams(@Param("analysisId") Long analysisId,
                                                              @Param("parameterIds") List<Long> parameterIds);


}
