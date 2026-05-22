-- ============================================================
-- 01_schema.sql
-- 오늘 뭐 먹지? 프로젝트 스키마 초기화
-- ============================================================

CREATE DATABASE IF NOT EXISTS menu_db
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE menu_db;

-- ------------------------------------------------------------
-- menus
-- ------------------------------------------------------------
DROP TABLE IF EXISTS ai_recommendation_results;
DROP TABLE IF EXISTS ai_recommendations;
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS menus;

CREATE TABLE menus (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    name            VARCHAR(100)    NOT NULL                COMMENT '메뉴 이름',
    restaurant_name VARCHAR(100)    NOT NULL                COMMENT '가게 이름',
    category        VARCHAR(20)     NOT NULL                COMMENT '한식 | 양식 | 중식',
    price_range     VARCHAR(20)     NOT NULL                COMMENT '1만원이하 | 1~2만원 | 2만원이상',
    distance        VARCHAR(20)     NOT NULL                COMMENT '도보5분 | 도보10분 | 배달가능',
    image_url       VARCHAR(500)    NULL                    COMMENT '메뉴 사진 URL',
    last_eaten_at   DATETIME        NULL                    COMMENT '마지막으로 먹은 날짜',
    created_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    CONSTRAINT chk_category
        CHECK (category IN ('한식', '양식', '중식')),
    CONSTRAINT chk_price_range
        CHECK (price_range IN ('1만원이하', '1~2만원', '2만원이상')),
    CONSTRAINT chk_distance
        CHECK (distance IN ('도보5분', '도보10분', '배달가능'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_menus_category        ON menus (category);
CREATE INDEX idx_menus_price_range     ON menus (price_range);
CREATE INDEX idx_menus_distance        ON menus (distance);
CREATE INDEX idx_menus_restaurant_name ON menus (restaurant_name);

-- ------------------------------------------------------------
-- reviews
-- ------------------------------------------------------------
CREATE TABLE reviews (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    menu_id    BIGINT       NOT NULL,
    nickname   VARCHAR(50)  NOT NULL,
    pin        CHAR(4)      NOT NULL,
    rating     TINYINT      NOT NULL,
    comment    TEXT         NULL,
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    CONSTRAINT fk_reviews_menu_id
        FOREIGN KEY (menu_id) REFERENCES menus (id)
        ON DELETE CASCADE,

    CONSTRAINT chk_rating
        CHECK (rating BETWEEN 1 AND 5),
    CONSTRAINT chk_pin
        CHECK (pin REGEXP '^[0-9]{4}$')
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_reviews_menu_id ON reviews (menu_id);

-- ------------------------------------------------------------
-- ai_recommendations
-- ------------------------------------------------------------
CREATE TABLE ai_recommendations (
    id          BIGINT   NOT NULL AUTO_INCREMENT,
    user_input  TEXT     NOT NULL,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_recommendations_created_at ON ai_recommendations (created_at);

-- ------------------------------------------------------------
-- ai_recommendation_results
-- ------------------------------------------------------------
CREATE TABLE ai_recommendation_results (
    id                BIGINT       NOT NULL AUTO_INCREMENT,
    recommendation_id BIGINT       NOT NULL,
    rank              TINYINT      NOT NULL,
    menu_id           BIGINT       NOT NULL,
    menu_name         VARCHAR(100) NOT NULL,
    reason            TEXT         NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_results_recommendation_id
        FOREIGN KEY (recommendation_id) REFERENCES ai_recommendations (id)
        ON DELETE CASCADE,

    CONSTRAINT chk_rank
        CHECK (rank BETWEEN 1 AND 3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_results_recommendation_id ON ai_recommendation_results (recommendation_id);
