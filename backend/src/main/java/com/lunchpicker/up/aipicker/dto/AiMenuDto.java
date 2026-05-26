package com.lunchpicker.up.aipicker.dto;

public record AiMenuDto(
        Long id,
        String name,
        String restaurantName,
        String category,
        String priceRange,
        String distance,
        Double avgRating
) {}
