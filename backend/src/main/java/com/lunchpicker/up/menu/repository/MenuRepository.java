package com.lunchpicker.up.menu.repository;

import com.lunchpicker.up.menu.dto.MenuResponse;
import com.lunchpicker.up.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("SELECT m FROM Menu m WHERE " +
           "(:category IS NULL OR m.category = :category) AND " +
           "(:priceRange IS NULL OR m.priceRange = :priceRange) AND " +
           "(:distance IS NULL OR m.distance = :distance) " +
           "ORDER BY m.createdAt DESC")
    List<Menu> findAllWithFilter(
            @Param("category") String category,
            @Param("priceRange") String priceRange,
            @Param("distance") String distance
    );

    @Query("SELECT new com.lunchpicker.up.menu.dto.MenuResponse(" +
           "m.id, m.name, m.restaurantName, m.category, m.priceRange, m.distance, " +
           "m.imageUrl, m.lastEatenAt, " +
           "(SELECT ROUND(AVG(r.rating), 1) FROM Review r WHERE r.menuId = m.id), " +
           "(SELECT COUNT(r.id) FROM Review r WHERE r.menuId = m.id), " +
           "m.createdAt) " +
           "FROM Menu m " +
           "WHERE (:category IS NULL OR m.category = :category) " +
           "AND (:priceRange IS NULL OR m.priceRange = :priceRange) " +
           "AND (:distance IS NULL OR m.distance = :distance) " +
           "ORDER BY m.createdAt DESC")
    List<MenuResponse> findAllWithStatsAndFilter(
            @Param("category") String category,
            @Param("priceRange") String priceRange,
            @Param("distance") String distance
    );

    @Query("SELECT new com.lunchpicker.up.menu.dto.MenuResponse(" +
           "m.id, m.name, m.restaurantName, m.category, m.priceRange, m.distance, " +
           "m.imageUrl, m.lastEatenAt, " +
           "(SELECT ROUND(AVG(r.rating), 1) FROM Review r WHERE r.menuId = m.id), " +
           "(SELECT COUNT(r.id) FROM Review r WHERE r.menuId = m.id), " +
           "m.createdAt) " +
           "FROM Menu m WHERE m.id = :id")
    Optional<MenuResponse> findByIdWithStats(@Param("id") Long id);
}
