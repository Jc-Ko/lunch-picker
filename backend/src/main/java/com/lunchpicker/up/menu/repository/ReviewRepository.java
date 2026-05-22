package com.lunchpicker.up.menu.repository;

import com.lunchpicker.up.menu.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByMenuId(Long menuId);
}
