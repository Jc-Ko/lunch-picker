package com.lunchpicker.up.menu.dto;

import jakarta.validation.constraints.*;

public record ReviewUpdateRequest(
        @NotBlank(message = "PIN은 필수입니다.")
        @Pattern(regexp = "\\d{4}", message = "PIN은 숫자 4자리입니다.")
        String pin,

        @NotNull(message = "별점은 필수입니다.")
        @Min(value = 1, message = "별점은 최소 1입니다.")
        @Max(value = 5, message = "별점은 최대 5입니다.")
        Integer rating,

        String comment
) {
}
