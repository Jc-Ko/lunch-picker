package com.lunchpicker.up.aipicker.repository;

import com.lunchpicker.up.aipicker.entity.AiRecommendation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AiRecommendationRepository extends JpaRepository<AiRecommendation, Long> {

    List<AiRecommendation> findAllByDeletedAtIsNullOrderByCreatedAtDesc(Pageable pageable);

    @Modifying
    @Query("UPDATE AiRecommendation a SET a.deletedAt = :now WHERE a.deletedAt IS NULL")
    int softDeleteAll(@Param("now") LocalDateTime now);
}
