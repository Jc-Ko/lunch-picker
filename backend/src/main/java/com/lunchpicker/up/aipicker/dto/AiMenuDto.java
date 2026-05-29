package com.lunchpicker.up.aipicker.dto;

public record AiMenuDto(
        Long id,
        String name,
        String restaurantName,
        String categoryCode,
        String priceRangeCode,
        String distanceCode,
        Double avgRating
) {}
