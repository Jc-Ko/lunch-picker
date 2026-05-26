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

    public static Review create(Long menuId, String nickname, String pin, Integer rating, String comment) {
        Review review = new Review();
        review.menuId = menuId;
        review.nickname = nickname;
        review.pin = pin;
        review.rating = rating;
        review.comment = comment;
        return review;
    }

    public boolean matchPin(String inputPin) {
        return this.pin.equals(inputPin);
    }

    public void update(Integer rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }
}
