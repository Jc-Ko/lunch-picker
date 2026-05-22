# CLAUDE.md
> 이 파일은 Claude Code가 본 프로젝트에서 작업할 때 자동으로 읽는 가이드입니다.
> AI 행동 규칙(영문) + 프로젝트 컨텍스트(한국어)로 구성됩니다.

---

# Part 1. Behavioral Guidelines
_Adapted from Andrej Karpathy's CLAUDE.md_

## 1. Think Before Coding
- State assumptions explicitly. If uncertain, ask.
- If multiple interpretations exist, present them — don't pick silently.
- If something is unclear, stop. Name what's confusing. Ask.
- If a simpler approach exists, say so.

## 2. Simplicity First
- No features beyond what was asked.
- No abstractions for single-use code.
- No "flexibility" or "configurability" that wasn't requested.
- If you write 200 lines and it could be 50, rewrite it.

## 3. Surgical Changes
- Don't "improve" adjacent code, comments, or formatting unless asked.
- Don't refactor things that aren't broken.
- Match existing style, even if you'd do it differently.
- Every changed line should trace directly to the user's request.

## 4. Goal-Driven Execution
Transform tasks into verifiable goals:
- "Add validation" → "Write tests for invalid inputs, then make them pass"
- "Fix the bug" → "Write a test that reproduces it, then make it pass"

For multi-step tasks, state a brief plan:
```
1. [Step] → verify: [check]
2. [Step] → verify: [check]
```

---

# Part 2. Project Context

## 프로젝트 개요

**프로젝트명**: 오늘 뭐 먹지? — 팀 점심 메뉴 서비스
**목적**: 바이브코딩 학습 프로젝트. 완성도보다 흐름과 경험을 중시한다.
**기간**: 4일 (실제 코딩 2일, 각 4시간)
**팀 구성**: PM/PL 1명 + Dev 3명 (각자 담당 도메인 풀스택 개발)

단일 백엔드 + 단일 프론트엔드로 구성된다. 도메인은 URL과 라우트로 구분한다.

| 도메인 | 담당 | 백엔드 URL prefix | 프론트 라우트 |
|--------|------|------------------|--------------|
| 🍱 메뉴 관리 | Dev A | `/api/menus`, `/api/reviews` | `/`, `/menus/:id` |
| 🎲 오늘의 메뉴 뽑기 | Dev B | `/api/picker` | `/picker` |
| 🤖 AI 메뉴 추천 | Dev C | `/api/ai` | `/ai` |

**포트**
- 백엔드: `8080`
- 프론트엔드: `3000`

---

## 기술 스택

| 구분 | 스택 |
|------|------|
| 백엔드 | Spring Boot 3.5.14, Java 17, Gradle |
| ORM | Spring Data JPA + Hibernate |
| DB | MySQL 8.x |
| 프론트엔드 | React 18 + Vite |
| 라우팅 | React Router v6 |
| 상태 관리 | Zustand |
| 서버 상태 | TanStack Query (React Query) |
| 스타일 | Tailwind CSS v3 |
| HTTP 클라이언트 | Axios |
| AI API | Google Gemini 2.0 Flash |

---

## 프로젝트 구조

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
│   ├── 01_schema.sql
│   └── 02_sample_data.sql
├── backend/
│   ├── build.gradle
│   ├── settings.gradle
│   └── src/main/java/com/lunchpicker/up/
│       ├── LunchPickerApplication.java
│       ├── menu/                          # Dev A
│       │   ├── controller/
│       │   ├── service/
│       │   ├── repository/
│       │   ├── entity/
│       │   └── dto/
│       ├── picker/                        # Dev B
│       │   ├── controller/
│       │   ├── service/
│       │   └── dto/
│       ├── aipicker/                      # Dev C
│       │   ├── controller/
│       │   ├── service/
│       │   ├── repository/
│       │   ├── entity/
│       │   └── dto/
│       └── common/
│           ├── ApiResponse.java
│           ├── GlobalExceptionHandler.java
│           └── config/
│               └── CorsConfig.java
└── frontend/
    ├── package.json
    ├── vite.config.js
    └── src/
        ├── App.jsx
        ├── main.jsx
        ├── index.css
        ├── api/
        │   ├── axiosInstance.js
        │   ├── menuApi.js             # Dev A
        │   ├── pickerApi.js           # Dev B
        │   └── aiApi.js               # Dev C
        ├── components/
        │   ├── common/
        │   ├── menu/                  # Dev A
        │   ├── picker/                # Dev B
        │   └── aipicker/              # Dev C
        ├── hooks/
        │   ├── useMenus.js            # Dev A
        │   ├── usePicker.js           # Dev B
        │   └── useAiPicker.js         # Dev C
        ├── pages/
        │   ├── menu/                  # Dev A
        │   ├── picker/                # Dev B
        │   └── aipicker/              # Dev C
        └── store/
            ├── useMenuStore.js        # Dev A
            ├── usePickerStore.js      # Dev B
            └── useAiPickerStore.js    # Dev C
```

---

## 아키텍처 레이어 (백엔드 공통)

```
Controller → Service → Repository → Entity
```

- **Controller**: 요청/응답 처리, 입력값 검증 (`@Valid`)
- **Service**: 비즈니스 로직, 트랜잭션 (`@Transactional`)
- **Repository**: DB 접근 (Spring Data JPA)
- **Entity**: DB 테이블 매핑

**패키지 구조**: 도메인별 분리

```
com.lunchpicker.up
├── menu/
│   ├── controller/   MenuController, ReviewController
│   ├── service/      MenuService, ReviewService
│   ├── repository/   MenuRepository, ReviewRepository
│   ├── entity/       Menu, Review
│   └── dto/          MenuRequest, MenuResponse, ReviewRequest, ReviewResponse
├── picker/
│   ├── controller/   PickerController
│   ├── service/      PickerService
│   └── dto/          PickerRequest, PickerResponse
├── aipicker/
│   ├── controller/   AiPickerController
│   ├── service/      AiPickerService
│   ├── repository/   AiRecommendationRepository, AiRecommendationResultRepository
│   ├── entity/       AiRecommendation, AiRecommendationResult
│   └── dto/          AiRecommendRequest, AiRecommendResponse
└── common/
    ├── ApiResponse
    ├── GlobalExceptionHandler
    └── config/       CorsConfig
```

---

## DB 핵심 정보

### 스키마 구성
단일 DB `menu_db` 안에 모든 테이블이 존재한다.

### 테이블 요약
```
menu_db
├── menus                     (id, name, restaurant_name, category, price_range, distance, image_url, last_eaten_at, created_at)
├── reviews                   (id, menu_id, nickname, pin, rating, comment, created_at)
├── ai_recommendations        (id, user_input, created_at)
└── ai_recommendation_results (id, recommendation_id, rank, menu_id, menu_name, reason)
```

### DB 접속 정보
| 항목 | 값 |
|------|----|
| host | localhost |
| port | 3306 |
| database | menu_db |
| username | lunchpicker |
| password | 1234 |

> 상세 DDL → `sql/01_schema.sql` | 샘플 데이터 → `sql/02_sample_data.sql`

---

## API 핵심 정보

모든 API는 백엔드 단일 포트 `8080`으로 호출한다.

### menu 도메인 (Dev A)

| Method | Path | 설명 |
|--------|------|------|
| GET | /api/menus | 메뉴 목록 (필터: category, priceRange, distance) |
| GET | /api/menus/{id} | 메뉴 단건 조회 |
| POST | /api/menus | 메뉴 등록 |
| PUT | /api/menus/{id} | 메뉴 수정 |
| DELETE | /api/menus/{id} | 메뉴 삭제 |
| PATCH | /api/menus/{id}/eat | 오늘 먹었어요 |
| GET | /api/menus/{id}/reviews | 리뷰 목록 |
| POST | /api/menus/{id}/reviews | 리뷰 작성 |
| PUT | /api/reviews/{id} | 리뷰 수정 (PIN 검증) |
| DELETE | /api/reviews/{id} | 리뷰 삭제 (PIN 검증) |

### picker 도메인 (Dev B)

| Method | Path | 설명 |
|--------|------|------|
| GET | /api/picker/pick | 조건 기반 랜덤 메뉴 1개 반환 |

### aipicker 도메인 (Dev C)

| Method | Path | 설명 |
|--------|------|------|
| POST | /api/ai/recommend | 자연어 입력 → TOP 3 추천 |
| GET | /api/ai/history | 추천 히스토리 조회 |

> 상세 스펙 → `docs/design/api-spec.md`

---

## 비즈니스 규칙

### 공통
- 로그인/인증 없음. 모든 API는 인증 없이 호출 가능
- CORS: 프론트엔드 포트(`3000`) 허용

### menu 도메인 (Dev A)
- `category`: `한식` | `양식` | `중식` 만 허용
- `price_range`: `1만원이하` | `1~2만원` | `2만원이상` 만 허용
- `distance`: `도보5분` | `도보10분` | `배달가능` 만 허용
- `pin`: 4자리 숫자 문자열. 평문 저장 (학습용)
- `rating`: 1 이상 5 이하 정수
- 평균 별점은 DB에 저장하지 않고 조회 시 `reviews` 테이블에서 계산

### picker 도메인 (Dev B)
- `menu_db`에 읽기 전용 접근. INSERT/UPDATE/DELETE 금지
- 리뷰가 없는 메뉴(avg_rating NULL)는 별점 조건 선택 시 제외. `상관없음` 선택 시 포함
- 비중 선택 모드: 합산 100 불필요. 상대 비중으로 계산

### aipicker 도메인 (Dev C)
- Gemini API 호출 전 `menu_db` 메뉴 전체 조회 후 JSON으로 직렬화해 프롬프트에 삽입
- AI 응답은 반드시 JSON 파싱 후 저장. 파싱 실패 시 에러 반환
- 추천 결과는 TOP 3 순위로 반환
- `menu_id`는 실제 `menu_db.menus.id` 값이어야 함

---

## 코딩 컨벤션

> 상세 내용 → `docs/CONVENTIONS.md`

### 백엔드 요약
- 응답 형식: `ApiResponse<T>` 공통 래퍼 사용
  ```json
  { "success": true, "data": { ... }, "message": null }
  { "success": false, "data": null, "message": "에러 메시지" }
  ```
- `@RestControllerAdvice`로 전역 예외 처리
- Entity에 `@CreationTimestamp` 사용 (`created_at` 자동 관리)
- DTO는 record 사용 권장 (Java 17)

### 프론트엔드 요약
- 컴포넌트: PascalCase (`MenuCard.jsx`)
- 훅: camelCase + use prefix (`useMenus.js`)
- API 호출: `src/api/` 디렉토리에 모아서 관리
- 전역 상태(Zustand): `src/store/` 디렉토리
- 서버 상태(TanStack Query): `src/hooks/` 디렉토리

---

## 자주 쓰는 명령어

### 백엔드

```bash
# 프로젝트 루트에서
cd backend

# 서버 실행
./gradlew bootRun

# 빌드
./gradlew build
```

### 프론트엔드

```bash
# 프로젝트 루트에서
cd frontend

# 의존성 설치 (최초 1회)
npm install

# 개발 서버 실행
npm run dev
```

### DB

```bash
# MySQL 컨테이너 실행 (프로젝트 루트에서)
docker compose up -d

# 접속 확인
docker exec -it lunch-picker-mysql mysql -u root -prootpassword
```

---

## DO NOT ⚠️

- **picker/aipicker에서 menu_db 쓰기 금지** — SELECT만 허용
- **도메인 간 직접 Service 호출 금지** — 각 도메인은 독립적으로 동작
- **인증 로직 추가 금지** — 로그인/세션/JWT 없음
- **배포 설정 추가 금지** — 로컬 실행만
- **테스트 코드 작성 금지** — 스코프 초과
- **PIN 암호화 금지** — 평문 저장 (학습용 프로젝트)
- **이미지 파일 업로드 금지** — URL 입력만 허용

---

## 참고 문서

| 문서 | 경로 |
|------|------|
| 제품 요구사항 | `PRD.md` |
| ERD | `docs/design/erd.md` |
| API 스펙 | `docs/design/api-spec.md` |
| 컨벤션 | `docs/CONVENTIONS.md` |
| 환경 세팅 | `docs/SETUP.md` |
| DB 스키마 | `sql/01_schema.sql` |
| 샘플 데이터 | `sql/02_sample_data.sql` |

---

## 새 세션 시작 시

새 Claude Code 세션을 시작하면, 혹은 다른 AI도구를 이용하여 세션을 시작하면:

1. 이 파일(`CLAUDE.md`) 자동 로드됨
2. 본인 담당 도메인 확인: `backend/src/main/java/com/lunchpicker/up/{domain}/`
3. 최근 변경사항: `git log --oneline -5` 확인
4. 작업 목표만 말하면 바로 시작 가능

참고 문서 우선순위:
1. `CLAUDE.md` (이 파일)
2. `docs/CONVENTIONS.md`
3. `docs/design/erd.md`
4. `docs/design/api-spec.md`
