package com.lunchpicker.up.picker.dto;

import java.time.LocalDateTime;

public record PickerResponse(
        Long id,
        String name,
        String restaurantName,
        CodeItemResponse category,
        CodeItemResponse priceRange,
        CodeItemResponse distance,
        String imageUrl,
        LocalDateTime lastEatenAt,
        Double avgRating,
        Long reviewCount
) {}
