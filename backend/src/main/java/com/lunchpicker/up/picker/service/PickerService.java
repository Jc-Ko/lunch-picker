package com.lunchpicker.up.picker.service;

import com.lunchpicker.up.commoncode.CommonCode;
import com.lunchpicker.up.commoncode.CommonCodeRepository;
import com.lunchpicker.up.picker.dto.CodeItemResponse;
import com.lunchpicker.up.picker.dto.PickerMenuDto;
import com.lunchpicker.up.picker.dto.PickerRequest;
import com.lunchpicker.up.picker.dto.PickerResponse;
import com.lunchpicker.up.picker.dto.PickerWeightedRequest;
import com.lunchpicker.up.picker.repository.PickerMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PickerService {

    private final PickerMenuRepository pickerMenuRepository;
    private final CommonCodeRepository commonCodeRepository;
    private final Random random = new Random();

    public PickerResponse pickSimple(PickerRequest request) {
        Map<String, CommonCode> categoryMap = loadCodeMap("category");
        Map<String, CommonCode> priceRangeMap = loadCodeMap("price_range");
        Map<String, CommonCode> distanceMap = loadCodeMap("distance");

        String categoryCode = blankToNull(request.category());
        String priceRangeCode = blankToNull(request.priceRange());
        String distanceCode = blankToNull(request.distance());

        if (categoryCode != null && !categoryMap.containsKey(categoryCode)) {
            throw new IllegalArgumentException("유효하지 않은 카테고리 코드입니다: " + categoryCode);
        }
        if (priceRangeCode != null && !priceRangeMap.containsKey(priceRangeCode)) {
            throw new IllegalArgumentException("유효하지 않은 가격대 코드입니다: " + priceRangeCode);
        }
        if (distanceCode != null && !distanceMap.containsKey(distanceCode)) {
            throw new IllegalArgumentException("유효하지 않은 거리 코드입니다: " + distanceCode);
        }

        List<PickerMenuDto> candidates = pickerMenuRepository.findFiltered(categoryCode, priceRangeCode, distanceCode, request.minRating());
        if (candidates.isEmpty()) {
            throw new NoSuchElementException("조건에 맞는 메뉴가 없습니다");
        }
        return toResponse(candidates.get(random.nextInt(candidates.size())), categoryMap, priceRangeMap, distanceMap);
    }

    public PickerResponse pickWeighted(PickerWeightedRequest request) {
        Map<String, CommonCode> categoryMap = loadCodeMap("category");
        Map<String, CommonCode> priceRangeMap = loadCodeMap("price_range");
        Map<String, CommonCode> distanceMap = loadCodeMap("distance");

        Map<String, Integer> weights = request.weights() != null ? request.weights() : Map.of();

        String priceRangeCode = blankToNull(request.priceRange());
        String distanceCode = blankToNull(request.distance());

        for (String code : weights.keySet()) {
            if (!categoryMap.containsKey(code)) {
                throw new IllegalArgumentException("유효하지 않은 카테고리 코드입니다: " + code);
            }
        }
        if (priceRangeCode != null && !priceRangeMap.containsKey(priceRangeCode)) {
            throw new IllegalArgumentException("유효하지 않은 가격대 코드입니다: " + priceRangeCode);
        }
        if (distanceCode != null && !distanceMap.containsKey(distanceCode)) {
            throw new IllegalArgumentException("유효하지 않은 거리 코드입니다: " + distanceCode);
        }

        List<PickerMenuDto> all = pickerMenuRepository.findFiltered(null, priceRangeCode, distanceCode, request.minRating());
        Map<String, List<PickerMenuDto>> byCategory = all.stream()
                .collect(Collectors.groupingBy(PickerMenuDto::categoryCode));

        Map<String, Integer> effectiveWeights = buildEffectiveWeights(weights, byCategory);

        int total = effectiveWeights.values().stream().mapToInt(Integer::intValue).sum();
        if (total == 0) {
            throw new NoSuchElementException("조건에 맞는 메뉴가 없습니다");
        }

        String selectedCode = pickCategoryByWeight(effectiveWeights, total);
        List<PickerMenuDto> candidates = byCategory.get(selectedCode);
        return toResponse(candidates.get(random.nextInt(candidates.size())), categoryMap, priceRangeMap, distanceMap);
    }

    private Map<String, Integer> buildEffectiveWeights(Map<String, Integer> weights,
                                                        Map<String, List<PickerMenuDto>> byCategory) {
        boolean allZero = weights.isEmpty()
                || weights.values().stream().allMatch(v -> v == null || v <= 0);

        if (allZero) {
            Map<String, Integer> equal = new LinkedHashMap<>();
            for (String code : byCategory.keySet()) {
                if (!byCategory.get(code).isEmpty()) {
                    equal.put(code, 1);
                }
            }
            return equal;
        }

        Map<String, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> e : weights.entrySet()) {
            int w = (e.getValue() != null && e.getValue() > 0) ? e.getValue() : 0;
            if (w > 0 && !byCategory.getOrDefault(e.getKey(), List.of()).isEmpty()) {
                result.put(e.getKey(), w);
            }
        }
        return result;
    }

    private String pickCategoryByWeight(Map<String, Integer> weights, int total) {
        int roll = random.nextInt(total);
        int cumulative = 0;
        for (Map.Entry<String, Integer> e : weights.entrySet()) {
            cumulative += e.getValue();
            if (roll < cumulative) return e.getKey();
        }
        return weights.keySet().iterator().next();
    }

    private Map<String, CommonCode> loadCodeMap(String codeGroup) {
        return commonCodeRepository.findByCodeGroupOrderBySortOrderAsc(codeGroup)
                .stream().collect(Collectors.toMap(CommonCode::getCode, c -> c));
    }

    private CodeItemResponse toCodeItemResponse(String code, Map<String, CommonCode> codeMap) {
        CommonCode cc = codeMap.get(code);
        return new CodeItemResponse(cc.getId(), cc.getCode(), cc.getLabel());
    }

    private PickerResponse toResponse(PickerMenuDto dto, Map<String, CommonCode> categoryMap,
                                       Map<String, CommonCode> priceRangeMap,
                                       Map<String, CommonCode> distanceMap) {
        return new PickerResponse(
                dto.id(), dto.name(), dto.restaurantName(),
                toCodeItemResponse(dto.categoryCode(), categoryMap),
                toCodeItemResponse(dto.priceRangeCode(), priceRangeMap),
                toCodeItemResponse(dto.distanceCode(), distanceMap),
                dto.imageUrl(), dto.lastEatenAt(),
                dto.avgRating(), dto.reviewCount()
        );
    }

    private static String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }
}
