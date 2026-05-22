package com.lunchpicker.up.menu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "menus")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String restaurantName;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String priceRange;

    @Column(nullable = false)
    private String distance;

    private String imageUrl;

    private LocalDate lastEatenAt;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // TODO: Dev A - 팩토리 메서드 구현
    public static Menu create(String name, String restaurantName, String category,
                              String priceRange, String distance, String imageUrl) {
        Menu menu = new Menu();
        // TODO: Dev A
        return menu;
    }

    // TODO: Dev A - 메뉴 정보 수정
    public void update(String name, String restaurantName, String category,
                       String priceRange, String distance, String imageUrl) {
        // TODO: Dev A
    }

    // TODO: Dev A - 오늘 먹었어요 처리
    public void updateLastEatenAt() {
        // TODO: Dev A
    }
}
