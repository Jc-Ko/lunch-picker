package com.lunchpicker.up.aipicker.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "ai_recommendations")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class AiRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String userInput;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "recommendation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AiRecommendationResult> results = new ArrayList<>();

    // TODO: Dev C - 팩토리 메서드 구현
    public static AiRecommendation create(String userInput) {
        AiRecommendation recommendation = new AiRecommendation();
        // TODO: Dev C
        return recommendation;
    }
}
