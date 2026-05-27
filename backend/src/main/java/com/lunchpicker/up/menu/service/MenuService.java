package com.lunchpicker.up.menu.service;

import com.lunchpicker.up.commoncode.CommonCode;
import com.lunchpicker.up.commoncode.CommonCodeRepository;
import com.lunchpicker.up.menu.dto.CodeItemResponse;
import com.lunchpicker.up.menu.dto.MenuRequest;
import com.lunchpicker.up.menu.dto.MenuResponse;
import com.lunchpicker.up.menu.dto.MenuWithStatsDto;
import com.lunchpicker.up.menu.entity.Menu;
import com.lunchpicker.up.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final CommonCodeRepository commonCodeRepository;

    public List<MenuResponse> getMenus(String categoryCode, String priceRangeCode, String distanceCode) {
        List<MenuWithStatsDto> dtos = menuRepository.findAllWithStatsAndFilter(categoryCode, priceRangeCode, distanceCode);
        Map<String, CommonCode> categoryMap = loadCodeMap("category");
        Map<String, CommonCode> priceRangeMap = loadCodeMap("price_range");
        Map<String, CommonCode> distanceMap = loadCodeMap("distance");
        return dtos.stream()
                .map(dto -> toResponse(dto, categoryMap, priceRangeMap, distanceMap))
                .toList();
    }

    public MenuResponse getMenu(Long id) {
        MenuWithStatsDto dto = menuRepository.findByIdWithStats(id)
                .orElseThrow(() -> new NoSuchElementException("메뉴를 찾을 수 없습니다. id=" + id));
        Map<String, CommonCode> categoryMap = loadCodeMap("category");
        Map<String, CommonCode> priceRangeMap = loadCodeMap("price_range");
        Map<String, CommonCode> distanceMap = loadCodeMap("distance");
        return toResponse(dto, categoryMap, priceRangeMap, distanceMap);
    }

    @Transactional
    public MenuResponse createMenu(MenuRequest request) {
        Map<String, CommonCode> categoryMap = loadCodeMap("category");
        Map<String, CommonCode> priceRangeMap = loadCodeMap("price_range");
        Map<String, CommonCode> distanceMap = loadCodeMap("distance");

        validateCode(request.category(), categoryMap, "카테고리");
        validateCode(request.priceRange(), priceRangeMap, "가격대");
        validateCode(request.distance(), distanceMap, "거리");

        String categoryLabel = categoryMap.get(request.category()).getLabel();
        String priceRangeLabel = priceRangeMap.get(request.priceRange()).getLabel();
        String distanceLabel = distanceMap.get(request.distance()).getLabel();

        Menu menu = Menu.create(
                request.name(), request.restaurantName(),
                categoryLabel, request.category(),
                priceRangeLabel, request.priceRange(),
                distanceLabel, request.distance(),
                request.imageUrl()
        );
        return toResponse(menuRepository.save(menu), categoryMap, priceRangeMap, distanceMap);
    }

    @Transactional
    public MenuResponse updateMenu(Long id, MenuRequest request) {
        Map<String, CommonCode> categoryMap = loadCodeMap("category");
        Map<String, CommonCode> priceRangeMap = loadCodeMap("price_range");
        Map<String, CommonCode> distanceMap = loadCodeMap("distance");

        validateCode(request.category(), categoryMap, "카테고리");
        validateCode(request.priceRange(), priceRangeMap, "가격대");
        validateCode(request.distance(), distanceMap, "거리");

        String categoryLabel = categoryMap.get(request.category()).getLabel();
        String priceRangeLabel = priceRangeMap.get(request.priceRange()).getLabel();
        String distanceLabel = distanceMap.get(request.distance()).getLabel();

        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("메뉴를 찾을 수 없습니다. id=" + id));
        menu.update(
                request.name(), request.restaurantName(),
                categoryLabel, request.category(),
                priceRangeLabel, request.priceRange(),
                distanceLabel, request.distance(),
                request.imageUrl()
        );
        return toResponse(menu, categoryMap, priceRangeMap, distanceMap);
    }

    @Transactional
    public void deleteMenu(Long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("메뉴를 찾을 수 없습니다. id=" + id));
        menuRepository.delete(menu);
    }

    @Transactional
    public MenuResponse eatMenu(Long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("메뉴를 찾을 수 없습니다. id=" + id));
        menu.updateLastEatenAt();
        Map<String, CommonCode> categoryMap = loadCodeMap("category");
        Map<String, CommonCode> priceRangeMap = loadCodeMap("price_range");
        Map<String, CommonCode> distanceMap = loadCodeMap("distance");
        return toResponse(menu, categoryMap, priceRangeMap, distanceMap);
    }

    private Map<String, CommonCode> loadCodeMap(String codeGroup) {
        return commonCodeRepository.findByCodeGroupOrderBySortOrderAsc(codeGroup)
                .stream().collect(Collectors.toMap(CommonCode::getCode, c -> c));
    }

    private void validateCode(String code, Map<String, CommonCode> codeMap, String fieldName) {
        if (!codeMap.containsKey(code)) {
            throw new IllegalArgumentException("유효하지 않은 " + fieldName + " 코드입니다: " + code);
        }
    }

    private CodeItemResponse toCodeItemResponse(String code, Map<String, CommonCode> codeMap) {
        CommonCode cc = codeMap.get(code);
        return new CodeItemResponse(cc.getId(), cc.getCode(), cc.getLabel());
    }

    private MenuResponse toResponse(Menu menu, Map<String, CommonCode> categoryMap,
                                     Map<String, CommonCode> priceRangeMap,
                                     Map<String, CommonCode> distanceMap) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getRestaurantName(),
                toCodeItemResponse(menu.getCategoryCode(), categoryMap),
                toCodeItemResponse(menu.getPriceRangeCode(), priceRangeMap),
                toCodeItemResponse(menu.getDistanceCode(), distanceMap),
                menu.getImageUrl(),
                menu.getLastEatenAt(),
                null,
                0L,
                menu.getCreatedAt()
        );
    }

    private MenuResponse toResponse(MenuWithStatsDto dto, Map<String, CommonCode> categoryMap,
                                     Map<String, CommonCode> priceRangeMap,
                                     Map<String, CommonCode> distanceMap) {
        return new MenuResponse(
                dto.id(),
                dto.name(),
                dto.restaurantName(),
                toCodeItemResponse(dto.categoryCode(), categoryMap),
                toCodeItemResponse(dto.priceRangeCode(), priceRangeMap),
                toCodeItemResponse(dto.distanceCode(), distanceMap),
                dto.imageUrl(),
                dto.lastEatenAt(),
                dto.avgRating(),
                dto.reviewCount(),
                dto.createdAt()
        );
    }
}
