package com.lunchpicker.up.aipicker.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lunchpicker.up.aipicker.AiRecommendationException;
import com.lunchpicker.up.aipicker.dto.AiMenuDto;
import com.lunchpicker.up.aipicker.dto.AiRecommendRequest;
import com.lunchpicker.up.aipicker.dto.AiRecommendResponse;
import com.lunchpicker.up.aipicker.dto.CodeItemResponse;
import com.lunchpicker.up.aipicker.entity.AiRecommendation;
import com.lunchpicker.up.aipicker.entity.AiRecommendationResult;
import com.lunchpicker.up.aipicker.repository.AiMenuReadRepository;
import com.lunchpicker.up.aipicker.repository.AiRecommendationRepository;
import com.lunchpicker.up.aipicker.repository.AiRecommendationResultRepository;
import com.lunchpicker.up.commoncode.CommonCode;
import com.lunchpicker.up.commoncode.CommonCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AiPickerService {

    private final AiRecommendationRepository aiRecommendationRepository;
    private final AiRecommendationResultRepository aiRecommendationResultRepository;
    private final AiMenuReadRepository aiMenuReadRepository;
    private final CommonCodeRepository commonCodeRepository;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Transactional
    public AiRecommendResponse recommend(AiRecommendRequest request) {
        List<AiMenuDto> menus = aiMenuReadRepository.findAllWithAvgRating();
        Map<Long, AiMenuDto> menuMap = menus.stream()
                .collect(Collectors.toMap(AiMenuDto::id, m -> m));

        Map<String, CodeItemResponse> categoryMap = loadCodeItemMap("category");
        Map<String, CodeItemResponse> priceRangeMap = loadCodeItemMap("price_range");
        Map<String, CodeItemResponse> distanceMap = loadCodeItemMap("distance");

        String prompt = buildPrompt(menus, request.userInput(), categoryMap, priceRangeMap, distanceMap);
        String geminiText = callGemini(prompt);
        List<GeminiItem> items = parseGeminiResponse(geminiText);

        List<GeminiItem> validItems = items.stream()
                .filter(item -> menuMap.containsKey(item.menuId()))
                .limit(3)
                .toList();

        AiRecommendation recommendation = AiRecommendation.create(request.userInput());
        for (GeminiItem item : validItems) {
            AiMenuDto menu = menuMap.get(item.menuId());
            recommendation.getResults().add(
                    AiRecommendationResult.create(recommendation, item.rank(), item.menuId(), menu.name(), item.reason())
            );
        }
        aiRecommendationRepository.save(recommendation);

        return toResponse(recommendation, menuMap, categoryMap, priceRangeMap, distanceMap);
    }

    public List<AiRecommendResponse> getHistory(int limit) {
        int cappedLimit = Math.min(limit, 50);
        List<AiRecommendation> recommendations = aiRecommendationRepository
                .findAllByDeletedAtIsNullOrderByCreatedAtDesc(PageRequest.of(0, cappedLimit));

        List<Long> allMenuIds = recommendations.stream()
                .flatMap(r -> r.getResults().stream().map(AiRecommendationResult::getMenuId))
                .distinct()
                .toList();
        Map<Long, AiMenuDto> menuMap = aiMenuReadRepository.findByIds(allMenuIds);

        Map<String, CodeItemResponse> categoryMap = loadCodeItemMap("category");
        Map<String, CodeItemResponse> priceRangeMap = loadCodeItemMap("price_range");
        Map<String, CodeItemResponse> distanceMap = loadCodeItemMap("distance");

        return recommendations.stream()
                .map(r -> toResponse(r, menuMap, categoryMap, priceRangeMap, distanceMap))
                .toList();
    }

    @Transactional
    public void deleteHistory() {
        aiRecommendationRepository.softDeleteAll(LocalDateTime.now());
    }

    private Map<String, CodeItemResponse> loadCodeItemMap(String codeGroup) {
        return commonCodeRepository.findByCodeGroupOrderBySortOrderAsc(codeGroup)
                .stream()
                .collect(Collectors.toMap(
                        CommonCode::getCode,
                        cc -> new CodeItemResponse(cc.getId(), cc.getCode(), cc.getLabel())
                ));
    }

    private String buildPrompt(List<AiMenuDto> menus, String userInput,
                                Map<String, CodeItemResponse> categoryMap,
                                Map<String, CodeItemResponse> priceRangeMap,
                                Map<String, CodeItemResponse> distanceMap) {
        try {
            List<Map<String, Object>> menuList = menus.stream().map(m -> {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("id", m.id());
                map.put("name", m.name());
                map.put("category", categoryMap.getOrDefault(m.categoryCode(),
                        new CodeItemResponse(null, m.categoryCode(), m.categoryCode())).label());
                map.put("price_range", priceRangeMap.getOrDefault(m.priceRangeCode(),
                        new CodeItemResponse(null, m.priceRangeCode(), m.priceRangeCode())).label());
                map.put("distance", distanceMap.getOrDefault(m.distanceCode(),
                        new CodeItemResponse(null, m.distanceCode(), m.distanceCode())).label());
                map.put("avg_rating", m.avgRating());
                return map;
            }).toList();
            String menuJson = objectMapper.writeValueAsString(menuList);

            return """
                    당신은 점심 메뉴 추천 전문가입니다.
                    반드시 아래 메뉴 목록 중에서만 추천해야 합니다.
                    사용자의 자연어를 분석하여 메뉴의 카테고리, 가격대, 거리, 평균 별점, 메뉴 이름을 종합적으로 고려해 가장 적합한 메뉴 3개를 순위로 추천하세요.
                    반드시 아래 JSON 형식으로만 응답하세요. 다른 텍스트는 절대 포함하지 마세요.

                    {
                      "recommendations": [
                        { "rank": 1, "menu_id": 3, "menu_name": "김치찌개", "reason": "추천 이유" },
                        { "rank": 2, "menu_id": 7, "menu_name": "파스타", "reason": "추천 이유" },
                        { "rank": 3, "menu_id": 1, "menu_name": "짜장면", "reason": "추천 이유" }
                      ]
                    }

                    [메뉴 목록 - JSON]
                    """ + menuJson + """

                    [사용자 요청]
                    """ + userInput;
        } catch (Exception e) {
            throw new AiRecommendationException("AI 추천 결과를 처리하는 중 오류가 발생했습니다", e);
        }
    }

    private String callGemini(String prompt) {
        try {
            Map<String, Object> requestBody = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(Map.of("text", prompt)))
                    )
            );

            String response = webClient.post()
                    .uri(geminiApiUrl + "?key=" + geminiApiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode root = objectMapper.readTree(response);
            return root.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText();
        } catch (AiRecommendationException e) {
            throw e;
        } catch (Exception e) {
            throw new AiRecommendationException("AI 추천 결과를 처리하는 중 오류가 발생했습니다", e);
        }
    }

    private List<GeminiItem> parseGeminiResponse(String text) {
        try {
            String cleaned = text.replaceAll("(?s)```json\\s*", "").replaceAll("(?s)```\\s*", "").trim();
            JsonNode root = objectMapper.readTree(cleaned);
            JsonNode recommendations = root.path("recommendations");

            List<GeminiItem> items = new ArrayList<>();
            for (JsonNode node : recommendations) {
                items.add(new GeminiItem(
                        node.path("rank").asInt(),
                        node.path("menu_id").asLong(),
                        node.path("menu_name").asText(),
                        node.path("reason").asText(null)
                ));
            }
            return items;
        } catch (Exception e) {
            throw new AiRecommendationException("AI 추천 결과를 처리하는 중 오류가 발생했습니다", e);
        }
    }

    private AiRecommendResponse toResponse(AiRecommendation recommendation, Map<Long, AiMenuDto> menuMap,
                                            Map<String, CodeItemResponse> categoryMap,
                                            Map<String, CodeItemResponse> priceRangeMap,
                                            Map<String, CodeItemResponse> distanceMap) {
        List<AiRecommendResponse.ResultItem> resultItems = recommendation.getResults().stream()
                .sorted(Comparator.comparingInt(AiRecommendationResult::getRank))
                .map(result -> {
                    AiMenuDto menu = menuMap.get(result.getMenuId());
                    return new AiRecommendResponse.ResultItem(
                            result.getRank(),
                            result.getMenuId(),
                            result.getMenuName(),
                            menu != null ? menu.restaurantName() : null,
                            menu != null ? categoryMap.get(menu.categoryCode()) : null,
                            menu != null ? priceRangeMap.get(menu.priceRangeCode()) : null,
                            menu != null ? distanceMap.get(menu.distanceCode()) : null,
                            menu != null ? menu.avgRating() : null,
                            result.getReason()
                    );
                })
                .toList();

        return new AiRecommendResponse(
                recommendation.getId(),
                recommendation.getUserInput(),
                resultItems,
                recommendation.getCreatedAt()
        );
    }

    private record GeminiItem(int rank, long menuId, String menuName, String reason) {}
}
