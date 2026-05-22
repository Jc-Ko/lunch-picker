package com.lunchpicker.up.menu.dto;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        Long menuId,
        String nickname,
        Integer rating,
        String comment,
        LocalDateTime createdAt
) {
}
