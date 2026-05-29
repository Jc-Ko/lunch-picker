package com.lunchpicker.up.picker.dto;

import java.util.Map;

public record PickerWeightedRequest(
        Map<String, Integer> weights,
        Double minRating,
        String priceRange,
        String distance
) {}
