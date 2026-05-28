-- ============================================================
-- 02_common_code.sql
-- 오늘 뭐 먹지? 공통 코드
-- ============================================================

-- 테이블 CREATE는 PM이 직접 실행함. 이 파일은 시드 데이터 INSERT만 담는다.
-- 실행 시 반드시 --default-character-set=utf8mb4 옵션 필요:
--   docker exec -i lunch-picker-mysql mysql -u lunchpicker -p1234 --default-character-set=utf8mb4 menu_db < sql/03_common_code.sql

DELETE FROM common_codes;

INSERT INTO common_codes (code_group, code, label, sort_order) VALUES
    ('category', 'KOREAN',  '한식', 1),
    ('category', 'WESTERN', '양식', 2),
    ('category', 'CHINESE', '중식', 3),

    ('price_range', 'UNDER_10000',         '1만원이하', 1),
    ('price_range', 'BETWEEN_10000_20000', '1~2만원',   2),
    ('price_range', 'OVER_20000',          '2만원이상', 3),

    ('distance', 'WALK_5MIN',  '도보5분',  1),
    ('distance', 'WALK_10MIN', '도보10분', 2),
    ('distance', 'DELIVERY',   '배달가능', 3);
