package com.lunchpicker.up.menu.service;

import com.lunchpicker.up.menu.dto.ReviewRequest;
import com.lunchpicker.up.menu.dto.ReviewResponse;
import com.lunchpicker.up.menu.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;

    // TODO: Dev A - menuId에 해당하는 리뷰 목록 반환
    public List<ReviewResponse> getReviews(Long menuId) {
        // TODO: Dev A
        return List.of();
    }

    // TODO: Dev A - 리뷰 등록
    @Transactional
    public ReviewResponse createReview(Long menuId, ReviewRequest request) {
        // TODO: Dev A
        return null;
    }

    // TODO: Dev A - 리뷰 수정 (PIN 검증 후 수정)
    @Transactional
    public ReviewResponse updateReview(Long id, ReviewRequest request) {
        // TODO: Dev A
        return null;
    }

    // TODO: Dev A - 리뷰 삭제 (PIN 검증 후 삭제)
    @Transactional
    public void deleteReview(Long id, String pin) {
        // TODO: Dev A
    }
}
