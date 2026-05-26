package com.lunchpicker.up.menu.service;

import com.lunchpicker.up.menu.dto.MenuRequest;
import com.lunchpicker.up.menu.dto.MenuResponse;
import com.lunchpicker.up.menu.entity.Menu;
import com.lunchpicker.up.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private static final Set<String> VALID_CATEGORIES = Set.of("한식", "양식", "중식");
    private static final Set<String> VALID_PRICE_RANGES = Set.of("1만원이하", "1~2만원", "2만원이상");
    private static final Set<String> VALID_DISTANCES = Set.of("도보5분", "도보10분", "배달가능");

    private final MenuRepository menuRepository;

    public List<MenuResponse> getMenus(String category, String priceRange, String distance) {
        return menuRepository.findAllWithStatsAndFilter(category, priceRange, distance);
    }

    public MenuResponse getMenu(Long id) {
        return menuRepository.findByIdWithStats(id)
                .orElseThrow(() -> new NoSuchElementException("메뉴를 찾을 수 없습니다. id=" + id));
    }

    @Transactional
    public MenuResponse createMenu(MenuRequest request) {
        validateMenuFields(request);
        Menu menu = Menu.create(
                request.name(), request.restaurantName(), request.category(),
                request.priceRange(), request.distance(), request.imageUrl()
        );
        return toResponse(menuRepository.save(menu));
    }

    @Transactional
    public MenuResponse updateMenu(Long id, MenuRequest request) {
        validateMenuFields(request);
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("메뉴를 찾을 수 없습니다. id=" + id));
        menu.update(request.name(), request.restaurantName(), request.category(),
                request.priceRange(), request.distance(), request.imageUrl());
        return toResponse(menu);
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
        return toResponse(menu);
    }

    private void validateMenuFields(MenuRequest request) {
        if (!VALID_CATEGORIES.contains(request.category())) {
            throw new IllegalArgumentException("유효하지 않은 카테고리입니다: " + request.category());
        }
        if (!VALID_PRICE_RANGES.contains(request.priceRange())) {
            throw new IllegalArgumentException("유효하지 않은 가격대입니다: " + request.priceRange());
        }
        if (!VALID_DISTANCES.contains(request.distance())) {
            throw new IllegalArgumentException("유효하지 않은 거리입니다: " + request.distance());
        }
    }

    private MenuResponse toResponse(Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getRestaurantName(),
                menu.getCategory(),
                menu.getPriceRange(),
                menu.getDistance(),
                menu.getImageUrl(),
                menu.getLastEatenAt(),
                null,
                0L,
                menu.getCreatedAt()
        );
    }
}
