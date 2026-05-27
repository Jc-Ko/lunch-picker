package com.lunchpicker.up.menu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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

    @Column(name = "category_code")
    private String categoryCode;

    @Column(name = "price_range_code")
    private String priceRangeCode;

    @Column(name = "distance_code")
    private String distanceCode;

    private String imageUrl;

    private LocalDateTime lastEatenAt;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public static Menu create(String name, String restaurantName,
                              String category, String categoryCode,
                              String priceRange, String priceRangeCode,
                              String distance, String distanceCode,
                              String imageUrl) {
        Menu menu = new Menu();
        menu.name = name;
        menu.restaurantName = restaurantName;
        menu.category = category;
        menu.categoryCode = categoryCode;
        menu.priceRange = priceRange;
        menu.priceRangeCode = priceRangeCode;
        menu.distance = distance;
        menu.distanceCode = distanceCode;
        menu.imageUrl = imageUrl;
        return menu;
    }

    public void update(String name, String restaurantName,
                       String category, String categoryCode,
                       String priceRange, String priceRangeCode,
                       String distance, String distanceCode,
                       String imageUrl) {
        this.name = name;
        this.restaurantName = restaurantName;
        this.category = category;
        this.categoryCode = categoryCode;
        this.priceRange = priceRange;
        this.priceRangeCode = priceRangeCode;
        this.distance = distance;
        this.distanceCode = distanceCode;
        this.imageUrl = imageUrl;
    }

    public void updateLastEatenAt() {
        this.lastEatenAt = LocalDateTime.now();
    }
}
