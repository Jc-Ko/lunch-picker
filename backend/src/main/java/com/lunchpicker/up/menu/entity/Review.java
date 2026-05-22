package com.lunchpicker.up.menu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "reviews")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long menuId;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false, length = 4)
    private String pin;

    @Column(nullable = false)
    private Integer rating;

    private String comment;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // TODO: Dev A - 팩토리 메서드 구현
    public static Review create(Long menuId, String nickname, String pin, Integer rating, String comment) {
        Review review = new Review();
        // TODO: Dev A
        return review;
    }

    // TODO: Dev A - PIN 검증
    public boolean matchPin(String inputPin) {
        // TODO: Dev A
        return false;
    }

    // TODO: Dev A - 리뷰 수정
    public void update(Integer rating, String comment) {
        // TODO: Dev A
    }
}
