package com.lunchpicker.up.aipicker.repository;

import com.lunchpicker.up.aipicker.dto.AiMenuDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AiMenuReadRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final String ALL_MENUS_QUERY = """
            SELECT m.id, m.name, m.restaurant_name, m.category, m.price_range, m.distance,
                   ROUND(AVG(r.rating), 1) AS avg_rating
            FROM menus m
            LEFT JOIN reviews r ON r.menu_id = m.id
            GROUP BY m.id, m.name, m.restaurant_name, m.category, m.price_range, m.distance
            ORDER BY m.id
            """;

    private static final String MENUS_BY_IDS_QUERY = """
            SELECT m.id, m.name, m.restaurant_name, m.category, m.price_range, m.distance,
                   ROUND(AVG(r.rating), 1) AS avg_rating
            FROM menus m
            LEFT JOIN reviews r ON r.menu_id = m.id
            WHERE m.id IN (:ids)
            GROUP BY m.id, m.name, m.restaurant_name, m.category, m.price_range, m.distance
            """;

    public List<AiMenuDto> findAllWithAvgRating() {
        return jdbcTemplate.query(ALL_MENUS_QUERY, new MapSqlParameterSource(), this::mapRow);
    }

    public Map<Long, AiMenuDto> findByIds(List<Long> ids) {
        if (ids.isEmpty()) return Collections.emptyMap();
        MapSqlParameterSource params = new MapSqlParameterSource("ids", ids);
        return jdbcTemplate.query(MENUS_BY_IDS_QUERY, params, this::mapRow)
                .stream()
                .collect(Collectors.toMap(AiMenuDto::id, dto -> dto));
    }

    private AiMenuDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new AiMenuDto(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("restaurant_name"),
                rs.getString("category"),
                rs.getString("price_range"),
                rs.getString("distance"),
                rs.getObject("avg_rating", Double.class)
        );
    }
}
