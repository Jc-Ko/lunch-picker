package com.lunchpicker.up.aipicker.controller;

import com.lunchpicker.up.aipicker.dto.AiRecommendRequest;
import com.lunchpicker.up.aipicker.dto.AiRecommendResponse;
import com.lunchpicker.up.aipicker.service.AiPickerService;
import com.lunchpicker.up.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiPickerController {

    private final AiPickerService aiPickerService;

    @PostMapping("/recommend")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AiRecommendResponse> recommend(@Valid @RequestBody AiRecommendRequest request) {
        return ApiResponse.created(aiPickerService.recommend(request));
    }

    @GetMapping("/history")
    public ApiResponse<List<AiRecommendResponse>> getHistory(
            @RequestParam(defaultValue = "20") int limit) {
        return ApiResponse.ok(aiPickerService.getHistory(limit));
    }
}
