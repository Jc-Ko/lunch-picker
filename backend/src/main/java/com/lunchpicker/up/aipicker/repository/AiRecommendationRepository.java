package com.lunchpicker.up.aipicker.repository;

import com.lunchpicker.up.aipicker.entity.AiRecommendation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiRecommendationRepository extends JpaRepository<AiRecommendation, Long> {

    List<AiRecommendation> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
