package com.lunchpicker.up.picker.service;

import com.lunchpicker.up.picker.dto.PickerRequest;
import com.lunchpicker.up.picker.dto.PickerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PickerService {

    // TODO: Dev B - menu_db 읽기 전용 접근을 위한 DataSource 또는 EntityManager 주입

    /**
     * 조건에 맞는 메뉴 중 랜덤으로 1개를 반환한다.
     *
     * categoryMode:
     *   - "simple": category 파라미터로 단일 카테고리 또는 "상관없음" 선택
     *   - "weighted": koreanWeight / westernWeight / chineseWeight 비중으로 카테고리 확률 결정
     *                 합산이 100일 필요 없음. 상대 비중으로 계산.
     *
     * minRating:
     *   - null: 리뷰가 없는 메뉴(avg_rating NULL) 포함하여 전체 대상
     *   - 값 존재: 해당 별점 이상인 메뉴만 대상 (리뷰 없는 메뉴 제외)
     */
    // TODO: Dev B - 구현
    public PickerResponse pick(PickerRequest request) {
        // TODO: Dev B
        return null;
    }
}
