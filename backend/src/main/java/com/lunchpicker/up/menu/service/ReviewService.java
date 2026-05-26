package com.lunchpicker.up.menu.service;

import com.lunchpicker.up.menu.dto.ReviewRequest;
import com.lunchpicker.up.menu.dto.ReviewUpdateRequest;
import com.lunchpicker.up.menu.dto.ReviewResponse;
import com.lunchpicker.up.menu.entity.Review;
import com.lunchpicker.up.menu.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public List<ReviewResponse> getReviews(Long menuId) {
        return reviewRepository.findByMenuId(menuId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ReviewResponse createReview(Long menuId, ReviewRequest request) {
        Review review = Review.create(menuId, request.nickname(), request.pin(),
                request.rating(), request.comment());
        return toResponse(reviewRepository.save(review));
    }

    @Transactional
    public ReviewResponse updateReview(Long id, ReviewUpdateRequest request) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("리뷰를 찾을 수 없습니다. id=" + id));
        if (!review.matchPin(request.pin())) {
            throw new IllegalArgumentException("PIN이 일치하지 않습니다");
        }
        review.update(request.rating(), request.comment());
        return toResponse(review);
    }

    @Transactional
    public void deleteReview(Long id, String pin) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("리뷰를 찾을 수 없습니다. id=" + id));
        if (!review.matchPin(pin)) {
            throw new IllegalArgumentException("PIN이 일치하지 않습니다");
        }
        reviewRepository.delete(review);
    }

    private ReviewResponse toResponse(Review review) {
        return new ReviewResponse(review.getId(), review.getMenuId(), review.getNickname(),
                review.getRating(), review.getComment(), review.getCreatedAt());
    }
}
