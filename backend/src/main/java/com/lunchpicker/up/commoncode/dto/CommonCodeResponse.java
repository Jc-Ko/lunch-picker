package com.lunchpicker.up.commoncode.dto;

public record CommonCodeResponse(
        Long id,
        String codeGroup,
        String code,
        String label,
        Integer sortOrder
) {}
