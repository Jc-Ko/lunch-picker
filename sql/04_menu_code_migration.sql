-- menus 한글 컬럼 → 공통코드 _code 컬럼 데이터 마이그레이션. 스키마 변경은 PM이 이미 수행.

UPDATE menus m
SET
    category_code = (
        SELECT code FROM common_codes
        WHERE code_group = 'category' AND label = m.category
    ),
    price_range_code = (
        SELECT code FROM common_codes
        WHERE code_group = 'price_range' AND label = m.price_range
    ),
    distance_code = (
        SELECT code FROM common_codes
        WHERE code_group = 'distance' AND label = m.distance
    );
