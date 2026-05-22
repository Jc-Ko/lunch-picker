package com.lunchpicker.up.aipicker.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "ai_recommendation_results")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class AiRecommendationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommendation_id", nullable = false)
    private AiRecommendation recommendation;

    @Column(name = "`rank`", nullable = false)
    private Integer rank;

    @Column(nullable = false)
    private Long menuId;

    @Column(nullable = false)
    private String menuName;

    @Column(columnDefinition = "TEXT")
    private String reason;

    // TODO: Dev C - 팩토리 메서드 구현
    public static AiRecommendationResult create(AiRecommendation recommendation, Integer rank,
                                                Long menuId, String menuName, String reason) {
        AiRecommendationResult result = new AiRecommendationResult();
        // TODO: Dev C
        return result;
    }
}
