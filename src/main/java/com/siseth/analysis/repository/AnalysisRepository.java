package com.siseth.analysis.repository;

import com.siseth.analysis.entity.Analysis;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnalysisRepository extends JpaRepository<Analysis, Long> {

    List<Analysis> findAllByToProcessIsTrue();

    List<Analysis> findAllByUserIdAndRealmAndIncrementalIsTrue(String userId, String realm);
    List<Analysis> findAllByUserIdAndSourceIdAndRealmAndIncrementalIsTrueAndToProcessIsTrue(String userId, Long sourceId, String realm);

    List<Analysis> findAllByUserIdAndSourceIdAndRealmAndIncrementalIsTrueAndToProcessIsFalse(String userId, Long sourceId, String realm);

    Optional<Analysis> findByUserIdAndRealmAndSourceIdAndIncrementalIsTrue(String userId, String realm, Long sourceId);

    Optional<Analysis> findByUserIdAndRealmAndSourceIdAndIncrementalIsFalse(String userId, String realm, Long sourceId);

    List<Analysis> findAllByUserIdAndRealm(String id, String realm, Sort sort);

    List<Analysis> findAllByUserIdAndRealmAndSourceId(String id, String realm, Long sourceId);
    List<Analysis> findAllByUserIdAndRealmAndSourceId(String id, String realm, Long sourceId, Sort sort);
//
    Optional<Analysis> findByIdAndRealmAndUserId(Long id, String realm, String userId);

    @Query("SELECT a " +
            "FROM Analysis a " +
            "WHERE a.isActive = TRUE AND " +
            "      a.userId = :userId AND " +
            "      a.realm = :realm AND " +
            "      (:sourceId = 0L OR a.sourceId = :sourceId) AND " +
            "      CAST(a.createdAt AS LocalDate) >= :dateFrom AND " +
            "      CAST(a.createdAt AS LocalDate) <= :dateTo")
    List<Analysis> getAllAnalysis(String userId, String realm, Long sourceId,
                                  LocalDate dateFrom, LocalDate dateTo,
                                  Sort sort);

    @Query("SELECT a " +
            "FROM Analysis a " +
            "WHERE a.isActive = TRUE AND " +
            "      a.userId = :userId AND " +
            "      a.realm = :realm AND " +
            "      (:sourceId = 0L OR a.sourceId = :sourceId) AND " +
            "      CAST(a.createdAt AS LocalDate) >= :dateFrom AND " +
            "      CAST(a.createdAt AS LocalDate) <= :dateTo")
    List<Analysis> getAllAnalysis(String userId, String realm, Long sourceId,
                                  LocalDate dateFrom, LocalDate dateTo);

}
