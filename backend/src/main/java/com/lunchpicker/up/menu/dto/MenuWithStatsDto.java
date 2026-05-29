package com.lunchpicker.up.menu.dto;

import java.time.LocalDateTime;

public record MenuWithStatsDto(
        Long id,
        String name,
        String restaurantName,
        String categoryCode,
        String priceRangeCode,
        String distanceCode,
        String imageUrl,
        LocalDateTime lastEatenAt,
        Double avgRating,
        Long reviewCount,
        LocalDateTime createdAt
) {}
