package com.lunchpicker.up.menu.service;

import com.lunchpicker.up.menu.dto.MenuRequest;
import com.lunchpicker.up.menu.dto.MenuResponse;
import com.lunchpicker.up.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;

    // TODO: Dev A - 필터(category, priceRange, distance) 적용 후 MenuResponse 리스트 반환
    public List<MenuResponse> getMenus(String category, String priceRange, String distance) {
        // TODO: Dev A
        return List.of();
    }

    // TODO: Dev A - id로 메뉴 단건 조회, 없으면 NoSuchElementException
    public MenuResponse getMenu(Long id) {
        // TODO: Dev A
        return null;
    }

    // TODO: Dev A - 메뉴 등록
    @Transactional
    public MenuResponse createMenu(MenuRequest request) {
        // TODO: Dev A
        return null;
    }

    // TODO: Dev A - 메뉴 수정
    @Transactional
    public MenuResponse updateMenu(Long id, MenuRequest request) {
        // TODO: Dev A
        return null;
    }

    // TODO: Dev A - 메뉴 삭제
    @Transactional
    public void deleteMenu(Long id) {
        // TODO: Dev A
    }

    // TODO: Dev A - 오늘 먹었어요: lastEatenAt을 오늘 날짜로 갱신
    @Transactional
    public MenuResponse eatMenu(Long id) {
        // TODO: Dev A
        return null;
    }
}
