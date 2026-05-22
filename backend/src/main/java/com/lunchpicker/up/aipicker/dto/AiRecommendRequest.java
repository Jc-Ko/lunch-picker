package com.lunchpicker.up.aipicker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AiRecommendRequest(
        @NotBlank(message = "추천 요청 내용은 필수입니다.")
        @Size(max = 500, message = "요청 내용은 500자 이하입니다.")
        String userInput
) {
}
