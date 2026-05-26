package com.lunchpicker.up.menu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PinRequest(
        @NotBlank(message = "PIN은 필수입니다.")
        @Pattern(regexp = "\\d{4}", message = "PIN은 숫자 4자리입니다.")
        String pin
) {
}
