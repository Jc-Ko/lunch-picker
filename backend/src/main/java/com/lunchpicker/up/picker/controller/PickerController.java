package com.lunchpicker.up.picker.controller;

import com.lunchpicker.up.common.ApiResponse;
import com.lunchpicker.up.picker.dto.PickerRequest;
import com.lunchpicker.up.picker.dto.PickerResponse;
import com.lunchpicker.up.picker.dto.PickerWeightedRequest;
import com.lunchpicker.up.picker.service.PickerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/picker")
@RequiredArgsConstructor
public class PickerController {

    private final PickerService pickerService;

    @GetMapping("/pick")
    public ApiResponse<PickerResponse> pickSimple(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) String priceRange,
            @RequestParam(required = false) String distance) {

        return ApiResponse.ok(pickerService.pickSimple(new PickerRequest(category, minRating, priceRange, distance)));
    }

    @PostMapping("/pick")
    public ApiResponse<PickerResponse> pickWeighted(@RequestBody PickerWeightedRequest request) {
        return ApiResponse.ok(pickerService.pickWeighted(request));
    }
}
