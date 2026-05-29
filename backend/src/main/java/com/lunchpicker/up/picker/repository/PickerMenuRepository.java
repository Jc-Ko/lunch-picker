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
            SELECT m.id, m.name, m.restaurant_name,
                   m.category_code, m.price_range_code, m.distance_code,
                   m.image_url, m.last_eaten_at,
                   ROUND(AVG(r.rating), 1) AS avg_rating,
                   COUNT(r.id)             AS review_count
            FROM menus m
            LEFT JOIN reviews r ON r.menu_id = m.id
            WHERE (:categoryCode IS NULL OR m.category_code = :categoryCode)
              AND (:priceRangeCode IS NULL OR m.price_range_code = :priceRangeCode)
              AND (:distanceCode IS NULL OR m.distance_code = :distanceCode)
            GROUP BY m.id, m.name, m.restaurant_name,
                     m.category_code, m.price_range_code, m.distance_code,
                     m.image_url, m.last_eaten_at
            HAVING (:minRating IS NULL OR ROUND(AVG(r.rating), 1) >= :minRating)
            """;

    public List<PickerMenuDto> findFiltered(String categoryCode, String priceRangeCode, String distanceCode, Double minRating) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("categoryCode", categoryCode)
                .addValue("priceRangeCode", priceRangeCode)
                .addValue("distanceCode", distanceCode)
                .addValue("minRating", minRating);

        return jdbcTemplate.query(QUERY, params, (rs, rowNum) -> new PickerMenuDto(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("restaurant_name"),
                rs.getString("category_code"),
                rs.getString("price_range_code"),
                rs.getString("distance_code"),
                rs.getString("image_url"),
                rs.getTimestamp("last_eaten_at") != null
                        ? rs.getTimestamp("last_eaten_at").toLocalDateTime()
                        : null,
                rs.getObject("avg_rating", Double.class),
                rs.getLong("review_count")
        ));
    }
}
