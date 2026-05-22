package com.lunchpicker.up.picker.dto;

import java.time.LocalDate;

public record PickerResponse(
        Long id,
        String name,
        String restaurantName,
        String category,
        String priceRange,
        String distance,
        String imageUrl,
        LocalDate lastEatenAt,
        Double avgRating,
        Long reviewCount
) {
}
