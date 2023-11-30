package com.siseth.analysis.repository;

import com.siseth.analysis.entity.AnalysisParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnalysisParameterRepository extends JpaRepository<AnalysisParameter, Long> {

    Optional<AnalysisParameter> findByName(String name);

    List<AnalysisParameter> findAllByNameIn(List<String> names);

    List<AnalysisParameter> findAllByValue(String value);

    List<AnalysisParameter> findAllByOrderByOrderNumber();

    @Query("SELECT DISTINCT p " +
            "FROM AnalysisParameter p " +
            "INNER JOIN AnalysisResult r ON r.parameter = p " +
            "INNER JOIN AnalysisDetail d ON r.detail = d " +
            "INNER JOIN Analysis a ON d.analysis = a " +
            "WHERE p.isActive = TRUE AND " +
            "       r.isActive = TRUE AND " +
            "       d.isActive = TRUE AND " +
            "       a.id = :analysisId AND " +
            "       COALESCE(p.orderNumber, 0) > 0 " +
            "ORDER BY p.orderNumber ")
    List<AnalysisParameter> getAllToAnalysis(@Param("analysisId") Long analysisId);

}
