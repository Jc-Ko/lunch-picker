# 🍱 오늘 뭐 먹지?

> 팀 점심 메뉴 추천 서비스 — 바이브코딩 학습 프로젝트

---

## 프로젝트 소개

매일 반복되는 "오늘 뭐 먹지?" 고민을 해결하는 서비스입니다.
메뉴를 등록하고 별점을 남기고, 조건에 따라 랜덤으로 뽑거나 AI에게 추천받을 수 있습니다.

**이 프로젝트는 실무형 바이브코딩 학습을 목적으로 합니다.**
완성도보다 흐름과 경험을 중시하며, AI 코딩 툴(Cursor / Claude Code / GitHub Copilot)을 활용해 개발합니다.

---

## 서비스 구성

| 도메인 | 설명 | 담당 | 백엔드 URL | 프론트 라우트 |
|--------|------|------|-----------|--------------|
| 🍱 **메뉴 관리** | 메뉴 CRUD + 별점·한줄평 | Dev A | `/api/menus` | `/`, `/menus/:id` |
| 🎲 **오늘의 메뉴 뽑기** | 조건 기반 랜덤 추천 | Dev B | `/api/picker` | `/picker` |
| 🤖 **AI 메뉴 추천** | 자연어 입력 → TOP 3 추천 | Dev C | `/api/ai` | `/ai` |

- 백엔드: `http://localhost:8080`
- 프론트엔드: `http://localhost:3000`

---

## 기술 스택

**Backend**
- Java 17 / Spring Boot 3.5.14 / Gradle
- Spring Data JPA + Hibernate
- MySQL 8.x

**Frontend**
- React 18 + Vite
- React Router v6 / Zustand / TanStack Query / Axios
- Tailwind CSS v3

**AI**
- Google Gemini 2.0 Flash API

**Infrastructure**
- Docker Desktop + Docker Compose (MySQL)
- WSL2 Ubuntu 24.04 (소스코드 위치)

---

## 팀 구성

| 역할 | 담당 도메인 |
|------|------------|
| PM/PL | 프로젝트 설계, 문서, 환경 세팅 |
| Dev A | 🍱 menu 도메인 |
| Dev B | 🎲 picker 도메인 |
| Dev C | 🤖 aipicker 도메인 |

---

## 프로젝트 구조

```
lunch-picker/
├── CLAUDE.md                          # AI 코딩 툴 가이드
├── README.md                          # 이 파일
├── PRD.md                             # 제품 요구사항 정의서
├── docker-compose.yml                 # MySQL 컨테이너 설정
├── docs/
│   ├── CONVENTIONS.md                 # 개발 컨벤션
│   ├── SETUP.md                       # 로컬 환경 세팅 가이드
│   ├── PRE_SETUP_CHECKLIST.md         # Day 1 전 사전 설치 체크리스트
│   └── design/
│       ├── erd.md                     # DB 설계
│       └── api-spec.md                # API 명세
├── sql/
│   ├── 01_schema.sql                  # DB/테이블 DDL
│   └── 02_sample_data.sql             # 샘플 데이터 (메뉴 16개 + 리뷰 60개)
├── backend/                           # Spring Boot 단일 프로젝트 (Port 8080)
│   ├── build.gradle
│   ├── settings.gradle
│   └── src/main/java/com/lunchpicker/up/
│       ├── LunchPickerApplication.java
│       ├── menu/                      # Dev A
│       ├── picker/                    # Dev B
│       ├── aipicker/                  # Dev C
│       └── common/
└── frontend/                          # React 단일 프로젝트 (Port 3000)
    ├── package.json
    ├── vite.config.js
    └── src/
        ├── App.jsx                    # 전체 라우트 정의
        ├── api/                       # 도메인별 API 함수
        ├── components/                # 도메인별 컴포넌트
        ├── hooks/                     # TanStack Query 훅
        ├── pages/                     # 도메인별 페이지
        └── store/                     # Zustand 전역 상태
```

---

## 빠른 시작

### 사전 요구사항

- Windows 10 21H2 이상 / Windows 11
- WSL2 + Ubuntu 24.04
- Docker Desktop (WSL Integration ON)
- JDK 17
- Node.js 20.x (WSL 내)

> 상세 설치 가이드 → [`docs/PRE_SETUP_CHECKLIST.md`](docs/PRE_SETUP_CHECKLIST.md)
> 상세 설정 가이드 → [`docs/SETUP.md`](docs/SETUP.md)

---

### 1. 저장소 클론

```bash
# WSL Ubuntu 터미널
cd ~
git clone git@github.com:{org}/lunch-picker.git
cd lunch-picker
```

### 2. MySQL 실행

```bash
# 프로젝트 루트에서
docker compose up -d

# 초기화 확인 (menus 16개)
docker exec -it lunch-picker-mysql \
  mysql -u root -prootpassword -e "USE menu_db; SELECT COUNT(*) FROM menus;"
```

### 3. 백엔드 실행

IntelliJ에서 `backend/` 폴더를 열고 `LunchPickerApplication` 실행.

```
http://localhost:8080/api/menus  →  메뉴 목록 확인
```

> Dev C는 IntelliJ Run Configuration에 `GEMINI_API_KEY` 환경변수 설정 필요

### 4. 프론트엔드 실행

```bash
cd frontend
npm install
npm run dev
```

```
http://localhost:3000  →  메인 화면 확인
```

---

## DB 구성

```
MySQL (Docker, Port 3306)
├── menu_db      # Dev A 소유 / Dev B·C 읽기 공유
│   ├── menus    # 메뉴 16개 샘플
│   └── reviews  # 리뷰 60개 샘플 (카테고리별 4개 메뉴 × 5개)
└── ai_db        # Dev C 전용
    ├── ai_recommendations
    └── ai_recommendation_results
```

**샘플 데이터 평균 별점 분포**

| | 4점대 (상) | 4점대 (하) | 3점대 | 2점대 |
|--|-----------|-----------|------|------|
| 한식 | 김치찌개 4.4 | 삼겹살 4.0 | 된장찌개 3.2 | 비빔밥 2.4 |
| 양식 | 스테이크 4.6 | 파스타 4.2 | 샌드위치 3.4 | 피자 2.6 |
| 중식 | 마라탕 4.8 | 짬뽕 4.0 | 짜장면 3.0 | 탕수육 2.2 |

---

## 개발 일정

| Day | 내용 |
|-----|------|
| Day 1 | 설계 설명 + 환경 세팅 + AI 툴 시연 |
| Day 2 | 바이브코딩 — 백엔드 API + 기본 UI |
| Day 3 | 바이브코딩 — UI 완성 + 통합 |
| Day 4 | 데모 + 기술 회고 + AI 툴 경험 공유 |

---

## 주요 문서

| 문서 | 경로 | 설명 |
|------|------|------|
| PRD | [`PRD.md`](PRD.md) | 제품 요구사항 |
| ERD | [`docs/design/erd.md`](docs/design/erd.md) | DB 설계 |
| API 명세 | [`docs/design/api-spec.md`](docs/design/api-spec.md) | 엔드포인트 |
| 컨벤션 | [`docs/CONVENTIONS.md`](docs/CONVENTIONS.md) | 코딩 규칙 |
| 환경 세팅 | [`docs/SETUP.md`](docs/SETUP.md) | 로컬 설정 |
| AI 가이드 | [`CLAUDE.md`](CLAUDE.md) | AI 툴 컨텍스트 |

---

## Known Issues

처음부터 구현하지 않는 것들:

- 이미지 파일 업로드 (URL 입력만 가능)
- PIN 암호화 (평문 저장)
- 에러 페이지
- 반응형 디자인 (데스크탑만)
- 테스트 코드
- 배포 설정
