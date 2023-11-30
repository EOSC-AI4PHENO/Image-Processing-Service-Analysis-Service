package com.siseth.analysis.repository;

import com.siseth.analysis.entity.SourcePreVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SourcePreVerificationRepository extends JpaRepository<SourcePreVerification, Long> {


    Optional<List<SourcePreVerification>> findAllByStatusAndTaskIdIsNotNull(SourcePreVerification.VerificationStatus status);

    Optional<List<SourcePreVerification>> findAllByStatus(SourcePreVerification.VerificationStatus status);
    Optional<SourcePreVerification> findByImageId(Long imageId);

    Boolean existsByImageId(Long imageId);

}
