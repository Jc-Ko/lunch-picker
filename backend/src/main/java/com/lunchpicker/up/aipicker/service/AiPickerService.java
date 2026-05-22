package com.lunchpicker.up.aipicker.service;

import com.lunchpicker.up.aipicker.dto.AiRecommendRequest;
import com.lunchpicker.up.aipicker.dto.AiRecommendResponse;
import com.lunchpicker.up.aipicker.repository.AiRecommendationRepository;
import com.lunchpicker.up.aipicker.repository.AiRecommendationResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AiPickerService {

    private final AiRecommendationRepository aiRecommendationRepository;
    private final AiRecommendationResultRepository aiRecommendationResultRepository;
    private final WebClient webClient;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    /**
     * AI 메뉴 추천 구현 흐름:
     * 1. menu_db에서 전체 메뉴 목록 조회
     * 2. 메뉴 목록을 JSON으로 직렬화하여 프롬프트에 삽입
     * 3. Gemini API 호출 (WebClient 사용)
     * 4. 응답 JSON 파싱 → TOP 3 추천 결과 추출 (파싱 실패 시 예외 반환)
     * 5. AiRecommendation + AiRecommendationResult 저장 후 응답 반환
     */
    // TODO: Dev C - 구현
    @Transactional
    public AiRecommendResponse recommend(AiRecommendRequest request) {
        // TODO: Dev C
        return null;
    }

    /**
     * 추천 히스토리 최신순 조회
     * limit 개수만큼 반환
     */
    // TODO: Dev C - 구현
    public List<AiRecommendResponse> getHistory(int limit) {
        // TODO: Dev C
        return List.of();
    }
}
