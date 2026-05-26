# PRD (Product Requirements Document)
# 🍱 오늘 뭐 먹지? — 팀 점심 메뉴 서비스

> 바이브코딩 학습 프로젝트 | 4일 일정 | 팀원 4명 (PM/PL 1 + Dev 3)

---

## 1. 프로젝트 개요

### 배경
팀원들이 매일 반복되는 "오늘 뭐 먹지?" 문제를 해결하는 서비스를 바이브코딩으로 만든다.
이 프로젝트의 목적은 **실무에서 AI 코딩 툴을 활용하는 방법을 체험**하는 것이며, 완성도보다 흐름과 경험을 중시한다.

### 목표
- AI 코딩 툴(Cursor / Claude Code / Codex)을 사용한 실무형 개발 경험
- 서비스 단위로 역할을 나눠 독립적으로 개발하고 느슨하게 통합하는 경험
- 2일(각 4시간) 안에 동작하는 서비스를 완성하는 경험

### 범위 제한 (중요)
- 로그인 / 회원가입 **없음**
- 실시간 동기화 **없음**
- 배포 **없음** (로컬 실행만)
- 완벽한 예외처리 **없음** (Happy Path만 구현)

---

## 2. 서비스 구성

단일 백엔드 + 단일 프론트엔드로 구성하고, 3개 도메인을 URL/라우트로 구분한다. **모든 도메인은 단일 `menu_db`를 공유**하며, 서비스 간 HTTP 호출 없이 DB에서 직접 데이터를 조회한다.

```
┌──────────────────────────────────────────────────────────┐
│            Spring Boot 단일 백엔드 (Port 8080)            │
│                                                          │
│  /api/menus/*   /api/reviews/*   → menu    패키지 (Dev A) │
│  /api/picker/*                   → picker  패키지 (Dev B) │
│  /api/ai/*                       → aipicker패키지 (Dev C) │
└──────────────────────┬───────────────────────────────────┘
                       │
                       ▼
                    menu_db
        (단일 DB — 전체 도메인 공유)
   menu: 읽기/쓰기  ·  picker: 읽기전용  ·  aipicker: 읽기/쓰기

┌──────────────────────────────────────────────────────────┐
│            React 단일 프론트엔드 (Port 3000)              │
│                                                          │
│  /           /menus/:id  → menu    페이지 (Dev A)         │
│  /picker                 → picker  페이지 (Dev B)         │
│  /ai                     → aipicker페이지 (Dev C)         │
└──────────────────────────────────────────────────────────┘
```

### SQL 초기화 파일 제공

PM/PL이 아래 두 파일을 사전에 준비해 팀원에게 배포한다. 각자 로컬 MySQL에 실행하면 바로 개발 시작 가능.

```
sql/
├── 01_schema.sql        -- DB 생성 + 테이블 DDL (menus, reviews, ai_recommendations, ai_recommendation_results)
└── 02_sample_data.sql   -- 메뉴 20개 + 메뉴별 리뷰 (일부 메뉴는 리뷰 없음)
```

---

## 3. 서비스별 요구사항

---

### 3-1. 🍱 메뉴 관리 서비스 (Dev A) — 라우트 `/`, `/menus/:id`

#### 기능 목록

**메뉴 CRUD**
- 메뉴 목록 조회 (카드형 UI)
- 메뉴 등록 (이름, 카테고리, 가격대, 거리, 사진 URL)
- 메뉴 수정
- 메뉴 삭제

**별점·한줄평**
- 누구나 이름 + 4자리 PIN + 별점(1~5) + 한줄평 작성 가능
- 본인 PIN 입력 시 수정/삭제 가능
- 메뉴별 평균 별점 표시

#### 데이터 모델

```sql
-- menus 테이블
id              BIGINT AUTO_INCREMENT PRIMARY KEY
name            VARCHAR(100) NOT NULL   -- 메뉴 이름 (예: 김치찌개)
restaurant_name VARCHAR(100) NOT NULL   -- 가게 이름 (예: 한솥뚝배기)
category        VARCHAR(20)  NOT NULL   -- 한식 | 양식 | 중식
price_range     VARCHAR(20)  NOT NULL   -- 1만원이하 | 1~2만원 | 2만원이상
distance        VARCHAR(20)  NOT NULL   -- 도보5분 | 도보10분 | 배달가능
image_url       VARCHAR(500)            -- 사진 URL (선택)
last_eaten_at   DATETIME                -- 마지막으로 먹은 날짜
created_at      DATETIME DEFAULT CURRENT_TIMESTAMP

-- reviews 테이블
id           BIGINT AUTO_INCREMENT PRIMARY KEY
menu_id      BIGINT NOT NULL         -- menus.id 참조 (FK, ON DELETE CASCADE)
nickname     VARCHAR(50) NOT NULL    -- 작성자 이름
pin          CHAR(4) NOT NULL        -- 4자리 숫자 (평문 저장, 학습용)
rating       TINYINT NOT NULL        -- 1 ~ 5
comment      TEXT                    -- 한줄평 (선택)
created_at   DATETIME DEFAULT CURRENT_TIMESTAMP
```

#### API 엔드포인트

| Method | Path | 설명 |
|--------|------|------|
| GET | /api/menus | 메뉴 목록 조회 (카테고리, 가격대, 거리 필터 가능) |
| POST | /api/menus | 메뉴 등록 |
| PUT | /api/menus/:id | 메뉴 수정 |
| DELETE | /api/menus/:id | 메뉴 삭제 |
| GET | /api/menus/:id/reviews | 특정 메뉴의 리뷰 목록 |
| POST | /api/menus/:id/reviews | 리뷰 작성 |
| PUT | /api/reviews/:id | 리뷰 수정 (PIN 검증 필요) |
| DELETE | /api/reviews/:id | 리뷰 삭제 (PIN 검증 필요) |

#### UI 화면

1. **메뉴 목록 페이지** — 카드형 그리드, 카테고리/가격대/거리 필터, 평균 별점 표시
2. **메뉴 상세 페이지** — 메뉴 정보 + 리뷰 목록 + 리뷰 작성 폼
3. **메뉴 등록/수정 폼** — 이름, 카테고리, 가격대, 거리, 사진 URL 입력

---

### 3-2. 🎲 오늘의 메뉴 뽑기 서비스 (Dev B) — 라우트 `/picker`

#### 기능 목록

조건을 선택하면 **공유 DB(`menu_db`)에서 직접 메뉴를 조회**해 랜덤으로 1개를 추천한다.

**Step 1: 카테고리 선택 방식 택일**
- **단순 선택 모드**: 한식 / 양식 / 중식 / 전체 중 하나 클릭
- **비중 선택 모드**: 한식 N : 양식 N : 중식 N 숫자 입력 후 비중 기반 랜덤 (합산 100 불필요)

**Step 2: 평균 별점 선택**
- 4.5 이상 / 4.0 이상 / 3.5 이상 / 상관없음
- 리뷰가 없는 메뉴(별점 없음)는 "상관없음" 선택 시에만 포함

**Step 3: 가격대 선택**
- 1만원 이하 / 1~2만원 / 2만원 이상 / 상관없음

**Step 4: 거리 선택**
- 도보 5분 / 도보 10분 / 배달 가능 / 상관없음

**결과**
- 조건에 맞는 메뉴 중 랜덤 1개 표시 (이름, 사진, 평균 별점, 리뷰 수 포함)
- "다시 뽑기" 버튼
- 조건에 맞는 메뉴가 없으면 안내 메시지 표시

#### 데이터 모델

```
별도 DB 없음.
Dev B 백엔드는 menu_db(Dev A와 동일한 MySQL DB)에 읽기 전용으로 접근한다.
menus 테이블과 reviews 테이블을 JOIN해서 평균 별점을 계산한다.
쓰기 작업은 절대 하지 않는다.
```

```sql
-- Dev B가 사용하는 조회 쿼리 예시
SELECT
  m.*,
  ROUND(AVG(r.rating), 1) AS avg_rating,
  COUNT(r.id)             AS review_count
FROM menus m
LEFT JOIN reviews r ON r.menu_id = m.id
WHERE m.category   = :category    -- 단순 선택 모드일 때
  AND m.price_range = :priceRange  -- 상관없음이면 조건 제외
  AND m.distance    = :distance    -- 상관없음이면 조건 제외
GROUP BY m.id
HAVING avg_rating >= :minRating   -- 상관없음이면 조건 제외 (NULL 포함)
```

#### 비중 랜덤 알고리즘

비중 선택 모드일 때, 조회된 메뉴 풀에서 카테고리 비중에 따라 랜덤 선택한다.

```
예: 한식 70, 양식 20, 중식 10 입력
→ 전체 합산(100) 대비 각 카테고리 확률로 카테고리 먼저 선택
→ 선택된 카테고리 내에서 조건에 맞는 메뉴 중 균등 랜덤 1개 선택
```

#### UI 화면

1. **뽑기 설정 페이지** — 4단계 선택 UI (Step 1 → 2 → 3 → 4 → 뽑기 버튼)
2. **결과 페이지** — 결과 카드 + 다시 뽑기 버튼

---

### 3-3. 🤖 AI 메뉴 추천 서비스 (Dev C) — 라우트 `/ai`

#### 기능 목록

자연어로 상황을 입력하면 Gemini API가 **`menu_db`의 메뉴 목록을 JSON으로 컨텍스트로 제공**받아 상위 3개를 순위로 추천한다.

**자연어 입력 예시**
- "오늘 날씨도 덥고 어제 한식 먹었어, 가볍게 먹고 싶어"
- "회식인데 예산은 1인당 2만원, 10명이야"
- "비 오는 날에 따뜻하게 먹고 싶어"

**추천 결과 (순위 TOP 3)**
- 1위 / 2위 / 3위 순서로 카드 표시
- 각 카드: 메뉴 이름, 카테고리, 가격대, 거리, 평균 별점, 추천 이유 (1~2문장)
- AI가 `menu_id`를 기반으로 응답하므로 DB에 존재하는 메뉴만 결과로 노출

**추천 히스토리**
- 오늘 했던 질문과 TOP 3 추천 결과 목록 표시

#### 데이터 모델

```sql
-- ai_recommendations 테이블 (menu_db)
id            BIGINT AUTO_INCREMENT PRIMARY KEY
user_input    TEXT         NOT NULL        -- 사용자 자연어 입력
created_at    DATETIME     DEFAULT CURRENT_TIMESTAMP

-- ai_recommendation_results 테이블 (menu_db)
id                BIGINT AUTO_INCREMENT PRIMARY KEY
recommendation_id BIGINT       NOT NULL   -- ai_recommendations.id 참조
rank              INT          NOT NULL   -- 1, 2, 3
menu_id           BIGINT       NOT NULL   -- menu_db.menus.id (숫자 참조, FK 없음)
menu_name         VARCHAR(100) NOT NULL   -- 추천 시점 메뉴명 스냅샷
reason            TEXT                   -- AI 추천 이유
```

#### Gemini API 프롬프트 구조

백엔드에서 `menu_db`의 메뉴 전체를 조회한 뒤 JSON으로 직렬화해 프롬프트에 삽입한다.

```
[시스템 프롬프트]
당신은 점심 메뉴 추천 전문가입니다.
반드시 아래 메뉴 목록 중에서만 추천해야 합니다.
사용자의 자연어를 분석하여 메뉴의 카테고리, 가격대, 거리, 평균 별점, 메뉴 이름을 종합적으로 고려해 가장 적합한 메뉴 3개를 순위로 추천하세요.
반드시 아래 JSON 형식으로만 응답하세요. 다른 텍스트는 절대 포함하지 마세요.

{
  "recommendations": [
    { "rank": 1, "menu_id": 3, "menu_name": "김치찌개", "reason": "추천 이유" },
    { "rank": 2, "menu_id": 7, "menu_name": "파스타",   "reason": "추천 이유" },
    { "rank": 3, "menu_id": 1, "menu_name": "짜장면",   "reason": "추천 이유" }
  ]
}

[메뉴 목록 - JSON]
[
  { "id": 1, "name": "김치찌개", "category": "한식", "price_range": "1만원이하", "distance": "도보5분", "avg_rating": 4.3 },
  ...
]

[사용자 요청]
{사용자 자연어 입력}
```

#### API 엔드포인트

| Method | Path | 설명 |
|--------|------|------|
| POST | /api/ai/recommend | 자연어 입력 받아 AI 추천 실행, TOP 3 반환 |
| GET | /api/ai/history | 추천 히스토리 조회 (질문 + TOP 3 결과) |

#### UI 화면

1. **추천 입력 페이지** — 텍스트 입력창 + 추천 버튼
2. **추천 결과** — TOP 3 카드 (1위/2위/3위 뱃지 + 메뉴 정보 + 추천 이유) 인라인 표시
3. **히스토리 목록** — 오늘의 질문 목록, 클릭 시 결과 펼침

---

## 4. 기술 스택

| 구분 | 스택 | 비고 |
|------|------|------|
| 백엔드 | Spring Boot 3.5.14 | 2026.04.23 릴리즈 |
| JDK | Java 17 | LTS 버전 |
| 빌드 도구 | Gradle | |
| ORM | Spring Data JPA + Hibernate | |
| DB | MySQL 8.x | 단일 `menu_db` 공유 |
| 프론트엔드 | React 18 + Vite | |
| 라우팅 | React Router v6 | SPA 라우팅 |
| 상태 관리 | Zustand | 가볍고 boilerplate 적음, 학습 비용 낮음 |
| 서버 상태 | TanStack Query (React Query) | API 호출 / 캐싱 / 로딩 상태 관리 |
| 스타일 | Tailwind CSS v3 | React와 궁합 좋음, 유틸리티 클래스 |
| HTTP 클라이언트 | Axios | 프론트 → 백엔드 API 호출 |
| AI API | Google Gemini 2.0 Flash | 무료 티어 (하루 1500회) |
| AI 코딩 툴 | 각자 선택 | Cursor / Claude Code / Codex |

### 포트

| 구분 | 포트 |
|------|------|
| 백엔드 (Spring Boot) | 8080 |
| 프론트엔드 (React) | 3000 |

### DB 스키마 구성

전체 도메인이 단일 `menu_db` 하나를 공유한다. 별도 DB는 없다. menu/picker/aipicker의 모든 테이블이 `menu_db` 안에 함께 존재한다.

```sql
CREATE DATABASE menu_db;   -- 전체 도메인 공유 (menu / picker / aipicker)
```

---

## 5. 폴더 구조

```
lunch-picker/
├── CLAUDE.md
├── README.md
├── PRD.md
├── docker-compose.yml
├── docs/
│   ├── CONVENTIONS.md
│   ├── SETUP.md
│   ├── PRE_SETUP_CHECKLIST.md
│   └── design/
│       ├── erd.md
│       └── api-spec.md
├── sql/
│   ├── 01_schema.sql              # DB/테이블 생성 DDL
│   └── 02_sample_data.sql         # 메뉴 20개 + 리뷰 샘플 데이터
├── backend/                       # Spring Boot 단일 프로젝트 (Port 8080)
│   ├── build.gradle
│   └── src/main/java/com/lunchpicker/up/
│       ├── menu/                  # Dev A
│       ├── picker/                # Dev B
│       ├── aipicker/              # Dev C
│       └── common/
└── frontend/                      # React + Vite 단일 프로젝트 (Port 3000)
    ├── package.json
    └── src/
        ├── api/                   # 도메인별 API 함수
        ├── components/            # 도메인별 컴포넌트
        ├── hooks/                 # 도메인별 훅
        ├── pages/                 # 도메인별 페이지
        └── store/                 # 도메인별 Zustand 스토어
```

> 백엔드 패키지 구조와 프론트 디렉토리 구조 상세는 `CLAUDE.md` 참고.

---

## 6. 서비스 간 연동 규칙

- picker, aipicker는 menu/reviews 테이블에 **읽기 전용**으로 접근한다 (INSERT / UPDATE / DELETE 금지). aipicker는 자신의 `ai_recommendations` / `ai_recommendation_results` 테이블에만 쓰기 가능
- 도메인 간 HTTP API 호출 및 직접 Service 호출은 하지 않는다 — DB 직접 조회로 대체
- `ai_recommendation_results.menu_id`는 `menus.id`를 숫자로만 참조하며 FK 제약을 걸지 않는다 (메뉴 삭제 시 히스토리 보존을 위해 `menu_name` 스냅샷 저장)
- DB 접속 계정은 PM/PL이 사전에 생성해서 배포한다

```sql
-- PM/PL이 준비하는 DB 계정 (단일 계정, 전체 도메인 공용)
CREATE USER 'lunchpicker'@'%' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON menu_db.* TO 'lunchpicker'@'%';
```

> picker의 읽기 전용 제약은 DB 권한이 아닌 **애플리케이션 코드 규칙**으로 지킨다 (단일 계정 공유).

---

## 7. 4일 일정

| Day | 내용 | 시간 |
|-----|------|------|
| Day 1 | 설계 설명 + 환경 세팅 + AI 툴 시연 | 4시간 |
| Day 2 | 바이브코딩 Day 1 — 백엔드 API + 기본 UI | 4시간 |
| Day 3 | 바이브코딩 Day 2 — UI 완성 + 서비스 통합 | 4시간 |
| Day 4 | 데모 + 기술 회고 + AI 툴 경험 공유 | 2~3시간 |

---

## 8. 완료 기준 (Definition of Done)

각 서비스가 아래를 만족하면 성공이다.

- [ ] 로컬에서 서버가 뜬다
- [ ] 핵심 기능 1가지 이상이 동작한다
- [ ] 다른 팀원의 서비스와 API 연동이 된다 (Dev B, C)
- [ ] README.md가 있다 (AI로 생성해도 됨)

> 예쁘지 않아도 되고, 엣지케이스 없어도 된다. 플로우가 돌아가면 성공.

---

## 9. Known Issue (처음부터 포기하는 것들)

아래는 처음부터 구현하지 않는다. 욕심내지 말 것.

- 이미지 파일 업로드 (URL만 입력)
- PIN 암호화 (평문 저장)
- 에러 페이지
- 반응형 디자인 (데스크탑만)
- 테스트 코드

---

*문서 작성: PM/PL | 버전: v1.5*
