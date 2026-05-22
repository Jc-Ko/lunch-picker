package com.lunchpicker.up.menu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MenuRequest(
        @NotBlank(message = "메뉴 이름은 필수입니다.")
        @Size(max = 100, message = "메뉴 이름은 100자 이하입니다.")
        String name,

        @NotBlank(message = "음식점 이름은 필수입니다.")
        @Size(max = 100, message = "음식점 이름은 100자 이하입니다.")
        String restaurantName,

        @NotBlank(message = "카테고리는 필수입니다.")
        String category,

        @NotBlank(message = "가격대는 필수입니다.")
        String priceRange,

        @NotBlank(message = "거리는 필수입니다.")
        String distance,

        String imageUrl
) {
}
