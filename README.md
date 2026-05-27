# 🍱 오늘 뭐 먹지? — 팀 점심 메뉴 서비스

매일 반복되는 "오늘 뭐 먹지?" 고민을 덜어주는 팀 점심 메뉴 서비스입니다.
메뉴를 등록·평가하고, 조건에 맞는 메뉴를 랜덤으로 뽑거나, AI에게 자연어로 추천을 받을 수 있습니다.

> 바이브코딩 학습 프로젝트입니다. 완성도보다 개발 흐름과 AI 코딩 툴 활용 경험을 중시합니다.

---

## 주요 기능

서비스는 3개 도메인으로 구성되며, 단일 백엔드와 단일 프론트엔드 위에서 URL/라우트로 구분됩니다.

| 도메인 | 설명 | 라우트 |
|--------|------|--------|
| 🍱 메뉴 관리 | 메뉴 CRUD, 별점·한줄평 작성 (PIN 기반 수정/삭제) | `/`, `/menus/:id` |
| 🎲 오늘의 메뉴 뽑기 | 카테고리·별점·가격·거리 조건으로 랜덤 메뉴 1개 추천 | `/picker` |
| 🤖 AI 메뉴 추천 | 자연어 입력을 분석해 메뉴 TOP 3 추천 + 히스토리 | `/ai` |

---

## 기술 스택

| 구분 | 스택 |
|------|------|
| 백엔드 | Spring Boot 3.5.14, Java 17, Gradle |
| ORM | Spring Data JPA + Hibernate |
| DB | MySQL 8.x (단일 `menu_db`) |
| 프론트엔드 | React 18 + Vite |
| 라우팅 / 상태 관리 | React Router v6, Zustand, TanStack Query |
| 스타일 | Tailwind CSS v3 |
| HTTP 클라이언트 | Axios |
| AI API | Google Gemini 2.5 Flash |

**포트** — 백엔드 `8080` / 프론트엔드 `3000` / MySQL `3306`

---

## 프로젝트 구조

```
lunch-picker/
├── docs/                  # 설계 및 컨벤션 문서
│   └── design/            # ERD, API 스펙
├── sql/                   # DB 스키마 + 샘플 데이터
├── backend/               # Spring Boot 단일 프로젝트
│   └── src/main/java/com/lunchpicker/up/
│       ├── menu/          # 메뉴 관리 도메인
│       ├── picker/        # 메뉴 뽑기 도메인
│       ├── aipicker/      # AI 추천 도메인
│       └── common/        # 공통 응답·예외·설정
└── frontend/              # React + Vite 단일 프로젝트
    └── src/               # api · components · hooks · pages · store (도메인별 분리)
```

도메인은 별도 프로젝트가 아니라 단일 백엔드의 패키지, 단일 프론트의 디렉토리로 구분됩니다.

---

## 실행 방법

사전 준비물(WSL2, Node.js, Docker Desktop, JDK 17 등)과 상세 절차는 `docs/SETUP.md`를 참고하세요.

### 1. 데이터베이스 (Docker)

```bash
# 프로젝트 루트에서
docker compose up -d
```

`sql/01_schema.sql`(스키마)과 `sql/02_sample_data.sql`(샘플 데이터)이 컨테이너 최초 실행 시 자동 적용됩니다.

### 2. 백엔드

```bash
cd backend
./gradlew bootRun
```

> AI 추천 기능(aipicker)을 사용하려면 `backend/src/main/resources/application-secret.yml`에
> 본인의 Gemini API 키가 필요합니다. 키 발급 방법은 `docs/SETUP.md` 7번 항목을 참고하세요.
> 이 파일은 `.gitignore`에 등록되어 커밋되지 않습니다.

### 3. 프론트엔드

```bash
cd frontend
npm install   # 최초 1회
npm run dev
```

브라우저에서 `http://localhost:3000` 으로 접속합니다.

---

## API 요약

모든 API는 인증 없이 호출 가능하며, 공통 응답 래퍼 `ApiResponse<T>`로 응답합니다.
Base URL은 `http://localhost:8080` 입니다.

| 도메인 | Method | Path | 설명 |
|--------|--------|------|------|
| menu | GET | /api/menus | 메뉴 목록 조회 (카테고리·가격·거리 필터) |
| menu | GET | /api/menus/{id} | 메뉴 단건 조회 |
| menu | POST | /api/menus | 메뉴 등록 |
| menu | PUT | /api/menus/{id} | 메뉴 수정 |
| menu | DELETE | /api/menus/{id} | 메뉴 삭제 |
| menu | PATCH | /api/menus/{id}/eat | 오늘 먹었어요 |
| menu | GET | /api/menus/{id}/reviews | 리뷰 목록 조회 |
| menu | POST | /api/menus/{id}/reviews | 리뷰 작성 |
| menu | PUT | /api/reviews/{id} | 리뷰 수정 (PIN 검증) |
| menu | DELETE | /api/reviews/{id} | 리뷰 삭제 (PIN 검증) |
| picker | GET | /api/picker/pick | 조건 기반 랜덤 메뉴 뽑기 |
| aipicker | POST | /api/ai/recommend | AI 자연어 메뉴 추천 (TOP 3) |
| aipicker | GET | /api/ai/history | AI 추천 히스토리 조회 |

상세 요청/응답 스펙은 `docs/design/api-spec.md`를 참고하세요.

---

## 참고 문서

| 문서 | 경로 |
|------|------|
| 제품 요구사항 | `PRD.md` |
| AI 작업 가이드 / 프로젝트 컨텍스트 | `CLAUDE.md` |
| 환경 세팅 가이드 | `docs/SETUP.md` |
| 코딩 컨벤션 | `docs/CONVENTIONS.md` |
| ERD | `docs/design/erd.md` |
| API 스펙 | `docs/design/api-spec.md` |

---

## 범위 안내

학습 프로젝트로서 다음은 의도적으로 구현하지 않았습니다.

- 로그인 / 회원가입 등 인증
- 배포 (로컬 실행 전용)
- 테스트 코드
- 이미지 파일 업로드 (URL 입력만 지원)
- 반응형 디자인 (데스크탑 기준)
