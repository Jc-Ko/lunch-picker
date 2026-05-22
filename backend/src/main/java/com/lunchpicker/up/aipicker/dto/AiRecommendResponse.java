package com.lunchpicker.up.aipicker.dto;

import java.time.LocalDateTime;
import java.util.List;

public record AiRecommendResponse(
        Long id,
        String userInput,
        List<ResultItem> results,
        LocalDateTime createdAt
) {
    public record ResultItem(
            Integer rank,
            Long menuId,
            String menuName,
            String restaurantName,
            String category,
            String priceRange,
            String distance,
            Double avgRating,
            String reason
    ) {
    }
}
