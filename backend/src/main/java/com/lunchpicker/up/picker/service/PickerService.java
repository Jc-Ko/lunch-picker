package com.lunchpicker.up.picker.service;

import com.lunchpicker.up.picker.dto.PickerMenuDto;
import com.lunchpicker.up.picker.dto.PickerRequest;
import com.lunchpicker.up.picker.dto.PickerResponse;
import com.lunchpicker.up.picker.repository.PickerMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final Random random = new Random();

    public PickerResponse pick(PickerRequest request) {
        String priceRange = blankToNull(request.priceRange());
        String distance = blankToNull(request.distance());

        if ("simple".equals(request.categoryMode())) {
            return pickSimple(request.category(), priceRange, distance, request.minRating());
        } else if ("weighted".equals(request.categoryMode())) {
            return pickWeighted(request.koreanWeight(), request.westernWeight(), request.chineseWeight(),
                    priceRange, distance, request.minRating());
        } else {
            throw new IllegalArgumentException("categoryMode는 simple 또는 weighted여야 합니다.");
        }
    }

    private PickerResponse pickSimple(String category, String priceRange, String distance, Double minRating) {
        String cat = (category == null || category.isBlank() || "전체".equals(category)) ? null : category;
        List<PickerMenuDto> candidates = pickerMenuRepository.findFiltered(cat, priceRange, distance, minRating);
        if (candidates.isEmpty()) {
            throw new NoSuchElementException("조건에 맞는 메뉴가 없습니다");
        }
        return toResponse(candidates.get(random.nextInt(candidates.size())));
    }

    private PickerResponse pickWeighted(Integer kw, Integer ww, Integer cw,
                                        String priceRange, String distance, Double minRating) {
        List<PickerMenuDto> all = pickerMenuRepository.findFiltered(null, priceRange, distance, minRating);

        Map<String, List<PickerMenuDto>> byCategory = all.stream()
                .collect(Collectors.groupingBy(PickerMenuDto::category));

        int korean = (kw != null && kw > 0) ? kw : 0;
        int western = (ww != null && ww > 0) ? ww : 0;
        int chinese = (cw != null && cw > 0) ? cw : 0;

        if (korean == 0 && western == 0 && chinese == 0) {
            korean = 1; western = 1; chinese = 1;
        }

        // 후보가 없는 카테고리는 추첨 전에 제외
        int effectiveKorean  = byCategory.getOrDefault("한식", List.of()).isEmpty() ? 0 : korean;
        int effectiveWestern = byCategory.getOrDefault("양식", List.of()).isEmpty() ? 0 : western;
        int effectiveChinese = byCategory.getOrDefault("중식", List.of()).isEmpty() ? 0 : chinese;

        if (effectiveKorean == 0 && effectiveWestern == 0 && effectiveChinese == 0) {
            throw new NoSuchElementException("조건에 맞는 메뉴가 없습니다");
        }

        String selectedCategory = pickCategoryByWeight(effectiveKorean, effectiveWestern, effectiveChinese);
        List<PickerMenuDto> candidates = byCategory.get(selectedCategory);
        return toResponse(candidates.get(random.nextInt(candidates.size())));
    }

    private String pickCategoryByWeight(int korean, int western, int chinese) {
        int total = korean + western + chinese;
        int roll = random.nextInt(total);
        if (roll < korean) return "한식";
        if (roll < korean + western) return "양식";
        return "중식";
    }

    private PickerResponse toResponse(PickerMenuDto dto) {
        return new PickerResponse(
                dto.id(), dto.name(), dto.restaurantName(), dto.category(),
                dto.priceRange(), dto.distance(), dto.imageUrl(),
                dto.lastEatenAt(), dto.avgRating(), dto.reviewCount()
        );
    }

    private static String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }
}
