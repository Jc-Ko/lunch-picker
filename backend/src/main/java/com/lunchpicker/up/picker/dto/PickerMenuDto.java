package com.lunchpicker.up.picker.dto;

import java.time.LocalDateTime;

public record PickerMenuDto(
        Long id,
        String name,
        String restaurantName,
        String categoryCode,
        String priceRangeCode,
        String distanceCode,
        String imageUrl,
        LocalDateTime lastEatenAt,
        Double avgRating,
        Long reviewCount
) {}
