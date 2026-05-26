package com.lunchpicker.up.picker.repository;

import com.lunchpicker.up.picker.dto.PickerMenuDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PickerMenuRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final String QUERY = """
            SELECT m.id, m.name, m.restaurant_name, m.category, m.price_range, m.distance,
                   m.image_url, m.last_eaten_at,
                   ROUND(AVG(r.rating), 1) AS avg_rating,
                   COUNT(r.id)             AS review_count
            FROM menus m
            LEFT JOIN reviews r ON r.menu_id = m.id
            WHERE (:category IS NULL OR m.category = :category)
              AND (:priceRange IS NULL OR m.price_range = :priceRange)
              AND (:distance IS NULL OR m.distance = :distance)
            GROUP BY m.id, m.name, m.restaurant_name, m.category,
                     m.price_range, m.distance, m.image_url, m.last_eaten_at
            HAVING (:minRating IS NULL OR ROUND(AVG(r.rating), 1) >= :minRating)
            """;

    public List<PickerMenuDto> findFiltered(String category, String priceRange, String distance, Double minRating) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("category", category)
                .addValue("priceRange", priceRange)
                .addValue("distance", distance)
                .addValue("minRating", minRating);

        return jdbcTemplate.query(QUERY, params, (rs, rowNum) -> new PickerMenuDto(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("restaurant_name"),
                rs.getString("category"),
                rs.getString("price_range"),
                rs.getString("distance"),
                rs.getString("image_url"),
                rs.getTimestamp("last_eaten_at") != null
                        ? rs.getTimestamp("last_eaten_at").toLocalDateTime()
                        : null,
                rs.getObject("avg_rating", Double.class),
                rs.getLong("review_count")
        ));
    }
}
