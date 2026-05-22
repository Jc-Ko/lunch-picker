package com.lunchpicker.up.picker.controller;

import com.lunchpicker.up.common.ApiResponse;
import com.lunchpicker.up.picker.dto.PickerRequest;
import com.lunchpicker.up.picker.dto.PickerResponse;
import com.lunchpicker.up.picker.service.PickerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/picker")
@RequiredArgsConstructor
public class PickerController {

    private final PickerService pickerService;

    @GetMapping("/pick")
    public ApiResponse<PickerResponse> pick(
            @RequestParam(defaultValue = "simple") String categoryMode,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer koreanWeight,
            @RequestParam(required = false) Integer westernWeight,
            @RequestParam(required = false) Integer chineseWeight,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) String priceRange,
            @RequestParam(required = false) String distance) {

        PickerRequest request = new PickerRequest(
                categoryMode, category,
                koreanWeight, westernWeight, chineseWeight,
                minRating, priceRange, distance
        );
        return ApiResponse.ok(pickerService.pick(request));
    }
}
