# ERD.md — 데이터베이스 설계

> 최종 수정: v1.2
> 상세 DDL → `sql/01_schema.sql` | 샘플 데이터 → `sql/02_sample_data.sql`

---

## 데이터베이스 구성

| DB | 설명 |
|----|------|
| `menu_db` | 전체 서비스 단일 DB (menus, reviews, ai_recommendations, ai_recommendation_results) |

---

## 1. menu_db

### 다이어그램

```
┌──────────────────────────────────┐
│              menus               │
├──────────────────────────────────┤
│ PK  id              BIGINT       │
│     name            VARCHAR(100) │
│     restaurant_name VARCHAR(100) │
│     category        VARCHAR(20)  │
│     price_range     VARCHAR(20)  │
│     distance        VARCHAR(20)  │
│     image_url       VARCHAR(500) │
│     last_eaten_at   DATETIME     │
│     created_at      DATETIME     │
└──────────────────┬───────────────┘
                   │ 1
                   │
                   │ N
┌──────────────────┴───────────────┐
│             reviews              │
├──────────────────────────────────┤
│ PK  id            BIGINT         │
│ FK  menu_id       BIGINT         │
│     nickname      VARCHAR(50)    │
│     pin           CHAR(4)        │
│     rating        TINYINT        │
│     comment       TEXT           │
│     created_at    DATETIME       │
└──────────────────────────────────┘
```

### menus 테이블

| 컬럼 | 타입 | NULL | 기본값 | 설명 |
|------|------|------|--------|------|
| `id` | BIGINT | NO | AUTO_INCREMENT | PK |
| `name` | VARCHAR(100) | NO | — | 메뉴 이름 (예: 김치찌개) |
| `restaurant_name` | VARCHAR(100) | NO | — | 가게 이름 (예: 한솥뚝배기) |
| `category` | VARCHAR(20) | NO | — | `한식` \| `양식` \| `중식` |
| `price_range` | VARCHAR(20) | NO | — | `1만원이하` \| `1~2만원` \| `2만원이상` |
| `distance` | VARCHAR(20) | NO | — | `도보5분` \| `도보10분` \| `배달가능` |
| `image_url` | VARCHAR(500) | YES | NULL | 메뉴 사진 URL |
| `last_eaten_at` | DATETIME | YES | NULL | 마지막으로 먹은 날짜 |
| `created_at` | DATETIME | NO | CURRENT_TIMESTAMP | 등록일시 |

**제약조건**
- `category` CHECK: `한식`, `양식`, `중식`
- `price_range` CHECK: `1만원이하`, `1~2만원`, `2만원이상`
- `distance` CHECK: `도보5분`, `도보10분`, `배달가능`

**인덱스**
- `idx_menus_category` ON `category`
- `idx_menus_price_range` ON `price_range`
- `idx_menus_distance` ON `distance`
- `idx_menus_restaurant_name` ON `restaurant_name`

---

### reviews 테이블

| 컬럼 | 타입 | NULL | 기본값 | 설명 |
|------|------|------|--------|------|
| `id` | BIGINT | NO | AUTO_INCREMENT | PK |
| `menu_id` | BIGINT | NO | — | FK → menus.id |
| `nickname` | VARCHAR(50) | NO | — | 작성자 이름 |
| `pin` | CHAR(4) | NO | — | 4자리 숫자 (평문 저장) |
| `rating` | TINYINT | NO | — | 별점 1 ~ 5 |
| `comment` | TEXT | YES | NULL | 한줄평 |
| `created_at` | DATETIME | NO | CURRENT_TIMESTAMP | 작성일시 |

**제약조건**
- `rating` CHECK: 1 이상 5 이하
- `pin` CHECK: 숫자 4자리 (`[0-9]{4}`)
- FK: `menu_id` → `menus(id)` ON DELETE CASCADE

**인덱스**
- `idx_reviews_menu_id` ON `menu_id`

---

### 평균 별점 계산 방식

평균 별점은 `menus` 테이블에 컬럼으로 저장하지 않는다.
조회 시마다 `reviews` 테이블에서 집계한다.

```sql
SELECT
    m.*,
    ROUND(AVG(r.rating), 1) AS avg_rating,
    COUNT(r.id)             AS review_count
FROM menus m
LEFT JOIN reviews r ON r.menu_id = m.id
GROUP BY m.id;
```

> LEFT JOIN을 사용하므로 리뷰가 없는 메뉴도 조회된다.
> 리뷰 없는 메뉴의 `avg_rating`은 `NULL`로 반환된다.

---

## 2. ai_recommendations / ai_recommendation_results

### 다이어그램

```
┌──────────────────────────────────────┐
│         ai_recommendations           │
├──────────────────────────────────────┤
│ PK  id            BIGINT             │
│     user_input    TEXT               │
│     created_at    DATETIME           │
│     deleted_at    DATETIME           │
└────────────────────┬─────────────────┘
                     │ 1
                     │
                     │ N (최대 3)
┌────────────────────┴─────────────────┐
│      ai_recommendation_results       │
├──────────────────────────────────────┤
│ PK  id                BIGINT         │
│ FK  recommendation_id BIGINT         │
│     rank              TINYINT        │
│     menu_id           BIGINT         │◀ ── menu_db.menus.id 참조
│     menu_name         VARCHAR(100)   │    (물리적 FK 없음)
│     reason            TEXT           │
└──────────────────────────────────────┘
```

### ai_recommendations 테이블

| 컬럼 | 타입 | NULL | 기본값 | 설명 |
|------|------|------|--------|------|
| `id` | BIGINT | NO | AUTO_INCREMENT | PK |
| `user_input` | TEXT | NO | — | 사용자 자연어 입력 원문 |
| `created_at` | DATETIME | NO | CURRENT_TIMESTAMP | 요청 일시 |
| `deleted_at` | DATETIME | YES | NULL | 히스토리 삭제 시각 (soft delete). NULL이면 미삭제 |

**히스토리 삭제 방식 (soft delete)**
- 히스토리 삭제 시 행을 물리적으로 DELETE하지 않고 `deleted_at`에 삭제 시각을 기록한다.
- 히스토리 조회는 `deleted_at IS NULL`인 행만 반환한다.
- `ai_recommendation_results`에는 별도 `deleted_at`을 두지 않는다. 부모 `ai_recommendations`의 `deleted_at`으로 삭제 여부를 판단한다.

---

### ai_recommendation_results 테이블

| 컬럼 | 타입 | NULL | 기본값 | 설명 |
|------|------|------|--------|------|
| `id` | BIGINT | NO | AUTO_INCREMENT | PK |
| `recommendation_id` | BIGINT | NO | — | FK → ai_recommendations.id |
| `rank` | TINYINT | NO | — | 추천 순위 1, 2, 3 |
| `menu_id` | BIGINT | NO | — | menu_db.menus.id 숫자 참조 |
| `menu_name` | VARCHAR(100) | NO | — | 추천 시점 메뉴명 스냅샷 |
| `reason` | TEXT | YES | NULL | AI 추천 이유 |

**제약조건**
- `rank` CHECK: 1 이상 3 이하
- FK: `recommendation_id` → `ai_recommendations(id)` ON DELETE CASCADE
- `menu_id`는 `menu_db.menus.id`를 숫자로만 참조. **물리적 FK 없음** (크로스 DB 제약 불가)
- `menu_name` 저장 이유: 추천 이후 메뉴가 삭제되더라도 히스토리 보존

**인덱스**
- `idx_results_recommendation_id` ON `recommendation_id`
- `idx_recommendations_created_at` ON `created_at` (히스토리 조회용)

---

## 3. 공통 설계 원칙

- **AUTO_INCREMENT**: 모든 PK는 BIGINT AUTO_INCREMENT 사용
- **DATETIME**: 타임존 없이 서버 로컬 시간 저장 (학습용 프로젝트)
- **ddl-auto: none**: Spring Boot에서 테이블 자동 생성 비활성화. `sql/01_schema.sql`로만 관리
- **Soft delete**: 기본적으로 삭제 시 실제 DELETE한다. 예외로 `ai_recommendations`만 soft delete를 사용한다 (`deleted_at` 컬럼). 그 외 테이블은 물리적 삭제다.
- **Audit 컬럼**: `created_at`만 관리. `updated_at` 없음 (학습 범위)
