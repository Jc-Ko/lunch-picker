package com.lunchpicker.up.commoncode;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommonCodeRepository extends JpaRepository<CommonCode, Long> {

    List<CommonCode> findByCodeGroupOrderBySortOrderAsc(String codeGroup);

    List<CommonCode> findAllByOrderBySortOrderAsc();
}
