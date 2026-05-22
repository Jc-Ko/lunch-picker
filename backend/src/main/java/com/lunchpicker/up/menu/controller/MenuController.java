package com.lunchpicker.up.menu.controller;

import com.lunchpicker.up.common.ApiResponse;
import com.lunchpicker.up.menu.dto.MenuRequest;
import com.lunchpicker.up.menu.dto.MenuResponse;
import com.lunchpicker.up.menu.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public ApiResponse<List<MenuResponse>> getMenus(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String priceRange,
            @RequestParam(required = false) String distance) {
        return ApiResponse.ok(menuService.getMenus(category, priceRange, distance));
    }

    @GetMapping("/{id}")
    public ApiResponse<MenuResponse> getMenu(@PathVariable Long id) {
        return ApiResponse.ok(menuService.getMenu(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<MenuResponse> createMenu(@Valid @RequestBody MenuRequest request) {
        return ApiResponse.created(menuService.createMenu(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<MenuResponse> updateMenu(@PathVariable Long id,
                                                @Valid @RequestBody MenuRequest request) {
        return ApiResponse.ok(menuService.updateMenu(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMenu(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return ApiResponse.ok(null);
    }

    @PatchMapping("/{id}/eat")
    public ApiResponse<MenuResponse> eatMenu(@PathVariable Long id) {
        return ApiResponse.ok(menuService.eatMenu(id));
    }
}
