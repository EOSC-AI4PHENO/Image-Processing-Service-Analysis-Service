package com.siseth.analysis.repository;

import com.siseth.analysis.entity.Analysis;
import com.siseth.analysis.entity.AnalysisDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnalysisDetailRepository extends JpaRepository<AnalysisDetail, Long> {

    boolean existsByAnalysisAndImageId(Analysis analysis, Long imageId);

    Optional<List<AnalysisDetail>> findAllByStatus(AnalysisDetail.AnalysisStatus status);

    List<AnalysisDetail> findAllByStatusOrderByCreatedAt(AnalysisDetail.AnalysisStatus status);

    List<AnalysisDetail> findAllByStatusAndIsCashedIsTrue(AnalysisDetail.AnalysisStatus status);

    Optional<List<AnalysisDetail>> findAllByStatusAndTaskIdNotNull(AnalysisDetail.AnalysisStatus status);

    Optional<List<AnalysisDetail>> findAllByAnalysisIdAndStatus(Long id, AnalysisDetail.AnalysisStatus status);
    Optional<List<AnalysisDetail>> findAllByAnalysisIdAndStatusAndImageCreatedAtBetween(Long id, AnalysisDetail.AnalysisStatus status, LocalDateTime start, LocalDateTime end);

    Optional<List<AnalysisDetail>> findAllByImageIdAndStatus(Long imageId, AnalysisDetail.AnalysisStatus status);

    Optional<List<AnalysisDetail>> findAllByAnalysisId(Long id);

    List<AnalysisDetail> findAllByAnalysisIdOrderByCreatedAtDesc(Long id);

    Page<AnalysisDetail> findAllByAnalysisIdAndStatus(Long analysisId, AnalysisDetail.AnalysisStatus status, Pageable pageable);

}
