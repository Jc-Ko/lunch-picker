# CONVENTIONS.md — 개발 컨벤션

> 이 문서는 팀 전체가 동일한 방식으로 코드를 작성하기 위한 규칙입니다.
> AI 툴이 코드를 생성할 때도 이 규칙을 프롬프트에 포함해서 사용하세요.

---

## 목차

1. [브랜치 전략](#1-브랜치-전략)
2. [커밋 컨벤션](#2-커밋-컨벤션)
3. [백엔드 컨벤션](#3-백엔드-컨벤션)
4. [프론트엔드 컨벤션](#4-프론트엔드-컨벤션)
5. [공통 규칙](#5-공통-규칙)

---

## 1. 브랜치 전략

### 브랜치 구조

```
main
└── feature/{service}-{기능명}
```

### 브랜치 규칙

| 브랜치 | 설명 |
|--------|------|
| `main` | 통합 브랜치. 각자 작업 완료 후 머지 |
| `feature/menu-{기능명}` | Dev A 작업 브랜치 |
| `feature/picker-{기능명}` | Dev B 작업 브랜치 |
| `feature/ai-{기능명}` | Dev C 작업 브랜치 |

### 브랜치 예시

```bash
feature/menu-crud
feature/menu-review
feature/picker-random
feature/ai-recommend
```

### 작업 흐름

```bash
# 작업 시작
git checkout main
git pull origin main
git checkout -b feature/menu-crud

# 작업 완료 후
git add .
git commit -m "feat(menu): 메뉴 CRUD API 구현"
git push origin feature/menu-crud

# main에 머지 (직접 머지, PR 없음 — 학습 프로젝트)
git checkout main
git merge feature/menu-crud
git push origin main
```

> PR/코드리뷰는 하지 않습니다. 학습 속도 우선.

---

## 2. 커밋 컨벤션

### 형식

```
{type}({scope}): {subject}
```

### type

| type | 설명 |
|------|------|
| `feat` | 새 기능 추가 |
| `fix` | 버그 수정 |
| `style` | UI/스타일 변경 |
| `refactor` | 기능 변경 없는 코드 개선 |
| `chore` | 설정, 의존성, 문서 등 |

### scope

| scope | 설명 |
|-------|------|
| `menu` | menu-service |
| `picker` | picker-service |
| `ai` | ai-service |
| `docs` | 문서 |

### 예시

```bash
feat(menu): 메뉴 목록 조회 API 구현
feat(menu): 리뷰 작성 및 PIN 검증 구현
fix(picker): 비중 랜덤 로직 오류 수정
style(menu): 메뉴 카드 UI 레이아웃 수정
feat(ai): Gemini API 연동 및 TOP3 추천 구현
chore(docs): api-spec.md 엔드포인트 추가
```

### 규칙

- subject는 한국어 사용
- 마침표 없음
- 명령형으로 작성 (`구현했다` ❌ → `구현` ✅)
- 한 커밋에 하나의 목적만

---

## 3. 백엔드 컨벤션

### 3-1. 패키지 구조

도메인별로 패키지를 분리한다.

```
com.lunchpicker.up.{domain}/
├── {domain}/
│   ├── {Domain}Controller.java
│   ├── {Domain}Service.java
│   ├── {Domain}Repository.java
│   ├── {Domain}Entity.java        # 또는 {Domain}.java
│   └── dto/
│       ├── {Domain}Request.java
│       └── {Domain}Response.java
├── config/
│   └── CorsConfig.java
└── common/
    ├── ApiResponse.java
    └── GlobalExceptionHandler.java
```

**예시 — menu 도메인**

```
com.lunchpicker.up.menu/
├── menu/
│   ├── MenuController.java
│   ├── MenuService.java
│   ├── MenuRepository.java
│   ├── Menu.java
│   └── dto/
│       ├── MenuRequest.java
│       └── MenuResponse.java
├── review/
│   ├── ReviewController.java
│   ├── ReviewService.java
│   ├── ReviewRepository.java
│   ├── Review.java
│   └── dto/
│       ├── ReviewRequest.java
│       └── ReviewResponse.java
├── config/
│   └── CorsConfig.java
└── common/
    ├── ApiResponse.java
    └── GlobalExceptionHandler.java
```

---

### 3-2. 공통 응답 래퍼

모든 API는 `ApiResponse<T>`로 감싸서 응답한다.

```java
// common/ApiResponse.java
@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private T data;
    private String message;

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static ApiResponse<Void> error(String message) {
        return new ApiResponse<>(false, null, message);
    }
}
```

**Controller 예시**

```java
@GetMapping("/api/menus")
public ResponseEntity<ApiResponse<List<MenuResponse>>> getMenus(...) {
    return ResponseEntity.ok(ApiResponse.ok(menuService.getMenus(...)));
}

@PostMapping("/api/menus")
public ResponseEntity<ApiResponse<MenuResponse>> createMenu(...) {
    return ResponseEntity.status(HttpStatus.CREATED)
                         .body(ApiResponse.created(menuService.createMenu(...)));
}
```

---

### 3-3. 전역 예외 처리

```java
// common/GlobalExceptionHandler.java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(ApiResponse.error(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                          .map(FieldError::getDefaultMessage)
                          .findFirst().orElse("유효성 검사 실패");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(ApiResponse.error(message));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(ApiResponse.error(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(ApiResponse.error("서버 오류가 발생했습니다"));
    }
}
```

---

### 3-4. Entity 규칙

```java
// 예시: Menu.java
@Entity
@Table(name = "menus")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "restaurant_name", nullable = false, length = 100)
    private String restaurantName;

    // ... 나머지 필드

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    // 생성 팩토리 메서드
    public static Menu create(String name, String restaurantName, ...) {
        Menu menu = new Menu();
        menu.name = name;
        menu.restaurantName = restaurantName;
        // ...
        return menu;
    }

    // 수정 메서드
    public void update(String name, String restaurantName, ...) {
        this.name = name;
        this.restaurantName = restaurantName;
        // ...
    }
}
```

**Entity 규칙 요약**
- `@NoArgsConstructor(access = AccessLevel.PROTECTED)` — 직접 생성 금지
- `@Setter` 사용 금지 — 수정 메서드로만 상태 변경
- DTO는 `record` 사용 권장 (Java 17)
- `@CreationTimestamp`로 `created_at` 자동 관리
- `updated_at` 없음 (학습 범위)

---

### 3-5. DTO 규칙

```java
// Request — record 사용
public record MenuRequest(
    @NotBlank(message = "메뉴 이름은 필수입니다")
    @Size(max = 100)
    String name,

    @NotBlank(message = "가게 이름은 필수입니다")
    @Size(max = 100)
    String restaurantName,

    @NotBlank(message = "카테고리는 필수입니다")
    String category,

    @NotBlank(message = "가격대는 필수입니다")
    String priceRange,

    @NotBlank(message = "거리는 필수입니다")
    String distance,

    String imageUrl
) {}

// Response — record 사용
public record MenuResponse(
    Long id,
    String name,
    String restaurantName,
    String category,
    String priceRange,
    String distance,
    String imageUrl,
    LocalDateTime lastEatenAt,
    Double avgRating,
    Long reviewCount,
    LocalDateTime createdAt
) {}
```

---

### 3-6. CORS 설정

```java
// config/CorsConfig.java
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
```

---

### 3-7. 네이밍 규칙

| 구분 | 규칙 | 예시 |
|------|------|------|
| 클래스 | PascalCase | `MenuController`, `MenuService` |
| 메서드 | camelCase | `getMenuList`, `createMenu` |
| 변수 | camelCase | `restaurantName`, `avgRating` |
| DB 컬럼 | snake_case | `restaurant_name`, `created_at` |
| URL | kebab-case (소문자) | `/api/menus`, `/api/picker/pick` |
| 상수 | UPPER_SNAKE_CASE | `MAX_LIMIT` |

---

## 4. 프론트엔드 컨벤션

### 4-1. 폴더 구조

```
src/
├── api/              # Axios 인스턴스 + API 호출 함수
│   ├── axiosInstance.js
│   ├── menuApi.js
│   ├── pickerApi.js  # picker-service만
│   └── aiApi.js      # ai-service만
├── components/       # 재사용 컴포넌트
│   ├── common/       # 공통 (Button, Modal 등)
│   └── {domain}/     # 도메인별 (MenuCard, ReviewItem 등)
├── hooks/            # TanStack Query 훅
│   └── useMenus.js
├── pages/            # 페이지 컴포넌트 (라우트 단위)
│   ├── MenuListPage.jsx
│   ├── MenuDetailPage.jsx
│   └── ...
├── store/            # Zustand 전역 상태
│   └── useMenuStore.js
├── App.jsx
└── main.jsx
```

---

### 4-2. 네이밍 규칙

| 구분 | 규칙 | 예시 |
|------|------|------|
| 컴포넌트 파일 | PascalCase | `MenuCard.jsx`, `ReviewItem.jsx` |
| 페이지 파일 | PascalCase + Page | `MenuListPage.jsx` |
| 훅 파일 | camelCase + use | `useMenus.js`, `useReviews.js` |
| 스토어 파일 | camelCase + use + Store | `useMenuStore.js` |
| API 파일 | camelCase + Api | `menuApi.js` |
| 변수/함수 | camelCase | `menuList`, `handleSubmit` |
| 상수 | UPPER_SNAKE_CASE | `API_BASE_URL` |

---

### 4-3. API 호출 패턴

```javascript
// api/menuApi.js — API 함수 정의
import axiosInstance from './axiosInstance'

export const menuApi = {
  getMenus: (params) =>
    axiosInstance.get('/api/menus', { params }).then(r => r.data.data),

  getMenu: (id) =>
    axiosInstance.get(`/api/menus/${id}`).then(r => r.data.data),

  createMenu: (body) =>
    axiosInstance.post('/api/menus', body).then(r => r.data.data),

  updateMenu: (id, body) =>
    axiosInstance.put(`/api/menus/${id}`, body).then(r => r.data.data),

  deleteMenu: (id) =>
    axiosInstance.delete(`/api/menus/${id}`).then(r => r.data),

  eatMenu: (id) =>
    axiosInstance.patch(`/api/menus/${id}/eat`).then(r => r.data.data),
}
```

---

### 4-4. TanStack Query 훅 패턴

```javascript
// hooks/useMenus.js
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { menuApi } from '../api/menuApi'

// 목록 조회
export function useMenus(params) {
  return useQuery({
    queryKey: ['menus', params],
    queryFn: () => menuApi.getMenus(params),
  })
}

// 등록
export function useCreateMenu() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: menuApi.createMenu,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['menus'] })
    },
  })
}
```

---

### 4-5. Zustand 스토어 패턴

```javascript
// store/useMenuStore.js
import { create } from 'zustand'

export const useMenuStore = create((set) => ({
  // 필터 상태 (서버 상태 아닌 UI 상태만 관리)
  filter: {
    category: '',
    priceRange: '',
    distance: '',
  },
  setFilter: (key, value) =>
    set((state) => ({
      filter: { ...state.filter, [key]: value },
    })),
  resetFilter: () =>
    set({ filter: { category: '', priceRange: '', distance: '' } }),
}))
```

> **서버 데이터(메뉴 목록, 리뷰 등)는 TanStack Query로 관리한다.**
> Zustand는 UI 상태(필터, 모달 열림 여부 등)만 관리한다.

---

### 4-6. 컴포넌트 작성 규칙

```jsx
// components/menu/MenuCard.jsx
// 1. import 순서: React → 라이브러리 → 내부 모듈
import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useEatMenu } from '../../hooks/useMenus'

// 2. props는 구조분해
export default function MenuCard({ menu }) {
  const navigate = useNavigate()
  const { mutate: eatMenu } = useEatMenu()

  // 3. 이벤트 핸들러는 handle 접두사
  const handleEat = () => {
    eatMenu(menu.id)
  }

  // 4. JSX 반환
  return (
    <div className="rounded-lg border p-4 shadow-sm">
      <h3 className="font-semibold text-lg">{menu.name}</h3>
      <p className="text-sm text-gray-500">{menu.restaurantName}</p>
      {/* ... */}
    </div>
  )
}
```

**컴포넌트 규칙 요약**
- `default export` 사용 (named export 지양)
- props 타입 검증은 생략 (학습 범위)
- 인라인 스타일 사용 금지 — Tailwind 클래스만 사용
- 컴포넌트 파일 1개에 컴포넌트 1개

---

### 4-7. React Router 라우트 구조

```jsx
// App.jsx — 전체 라우트 (단일 프론트엔드)
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import MenuListPage from './pages/menu/MenuListPage'
import MenuDetailPage from './pages/menu/MenuDetailPage'
import PickerPage from './pages/picker/PickerPage'
import AiPickerPage from './pages/aipicker/AiPickerPage'

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<MenuListPage />} />
        <Route path="/menus/:id" element={<MenuDetailPage />} />
        <Route path="/picker" element={<PickerPage />} />
        <Route path="/ai" element={<AiPickerPage />} />
      </Routes>
    </BrowserRouter>
  )
}
```

---

## 5. 공통 규칙

### 환경변수

| 위치 | 파일 | 용도 |
|------|------|------|
| 백엔드 | `application.yml` | DB 접속 정보 등 |
| 백엔드 | IntelliJ Run Configuration | `GEMINI_API_KEY` 등 민감 정보 |
| 프론트 | `.env.local` | `VITE_API_BASE_URL` 등 |

> `.env.local`은 `.gitignore`에 추가한다. `.env.example`만 커밋.

### .env.example (프론트)

```
VITE_API_BASE_URL=http://localhost:8081
```

### .gitignore 필수 항목

```
# 백엔드
build/
.gradle/
*.class

# 프론트
node_modules/
dist/
.env.local

# IDE
.idea/
.vscode/settings.json
*.iml
```

### 코드 스타일
- 들여쓰기: **4 spaces** (백엔드), **2 spaces** (프론트)
- 최대 줄 길이: 제한 없음 (학습 프로젝트)
- 주석: 복잡한 로직에만 한국어로 작성
- `console.log` 커밋 금지 (디버깅 후 제거)
