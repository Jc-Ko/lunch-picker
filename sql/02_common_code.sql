-- ============================================================
-- 02_common_code.sql
-- 오늘 뭐 먹지? 공통 코드
-- ============================================================

USE menu_db;
SET NAMES utf8mb4;

TRUNCATE TABLE common_codes;

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
