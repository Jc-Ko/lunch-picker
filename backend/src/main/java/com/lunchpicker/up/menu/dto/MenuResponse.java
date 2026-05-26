package com.lunchpicker.up.menu.dto;

import java.time.LocalDateTime;

public record MenuResponse(
        Long id,
        String name,
        String restaurantName,
        String category,
        String priceRange,
        String distance,
        String imageUrl,
        LocalDateTime lastEatenAt,
        Double avgRating,
        Long reviewCount,
        LocalDateTime createdAt
) {
}
