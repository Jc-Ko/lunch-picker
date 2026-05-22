package com.lunchpicker.up.picker.dto;

public record PickerRequest(
        String categoryMode,   // "simple" | "weighted"
        String category,       // simple 모드: "한식" | "양식" | "중식" | "상관없음"
        Integer koreanWeight,  // weighted 모드: 한식 비중
        Integer westernWeight, // weighted 모드: 양식 비중
        Integer chineseWeight, // weighted 모드: 중식 비중
        Double minRating,      // null이면 리뷰 없는 메뉴 포함, 값이 있으면 해당 별점 이상만
        String priceRange,     // "1만원이하" | "1~2만원" | "2만원이상" | "상관없음"
        String distance        // "도보5분" | "도보10분" | "배달가능" | "상관없음"
) {
}
