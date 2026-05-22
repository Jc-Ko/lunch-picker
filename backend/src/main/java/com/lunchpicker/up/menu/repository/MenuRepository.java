package com.lunchpicker.up.menu.repository;

import com.lunchpicker.up.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

// TODO: Dev A - 필터 조건 쿼리 메서드 추가 (category, priceRange, distance)
public interface MenuRepository extends JpaRepository<Menu, Long> {
}
