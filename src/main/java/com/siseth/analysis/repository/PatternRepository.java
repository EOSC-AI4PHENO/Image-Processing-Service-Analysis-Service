package com.siseth.analysis.repository;

import com.siseth.analysis.entity.Analysis;
import com.siseth.analysis.entity.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatternRepository extends JpaRepository<Pattern, Long> {


    Optional<Pattern> findByIdAndUserIdAndRealm(Long id, String userId, String realm);

    List<Pattern> findAllByUserIdAndRealm(String userId, String realm);
}
