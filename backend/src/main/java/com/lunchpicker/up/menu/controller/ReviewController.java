package com.lunchpicker.up.menu.controller;

import com.lunchpicker.up.common.ApiResponse;
import com.lunchpicker.up.menu.dto.PinRequest;
import com.lunchpicker.up.menu.dto.ReviewRequest;
import com.lunchpicker.up.menu.dto.ReviewUpdateRequest;
import com.lunchpicker.up.menu.dto.ReviewResponse;
import com.lunchpicker.up.menu.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/api/menus/{menuId}/reviews")
    public ApiResponse<List<ReviewResponse>> getReviews(@PathVariable Long menuId) {
        return ApiResponse.ok(reviewService.getReviews(menuId));
    }

    @PostMapping("/api/menus/{menuId}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ReviewResponse> createReview(@PathVariable Long menuId,
                                                    @Valid @RequestBody ReviewRequest request) {
        return ApiResponse.created(reviewService.createReview(menuId, request));
    }

    @PutMapping("/api/reviews/{id}")
    public ApiResponse<ReviewResponse> updateReview(@PathVariable Long id,
                                                    @Valid @RequestBody ReviewUpdateRequest request) {
        return ApiResponse.ok(reviewService.updateReview(id, request));
    }

    @DeleteMapping("/api/reviews/{id}")
    public ApiResponse<Void> deleteReview(@PathVariable Long id,
                                          @Valid @RequestBody PinRequest request) {
        reviewService.deleteReview(id, request.pin());
        return ApiResponse.ok(null);
    }
}
