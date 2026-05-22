# API Specification
> 버전: v1.0
> 모든 API는 인증 없이 호출 가능합니다.

---

## 공통 규칙

### Base URL

| Base URL |
|----------|
| `http://localhost:8080` |

> 모든 도메인(menu / picker / ai)은 단일 백엔드 포트 `8080`을 사용한다.

### 공통 응답 형식

모든 API는 `ApiResponse<T>` 래퍼로 응답한다.

```json
// 성공
{
  "success": true,
  "data": { },
  "message": null
}

// 실패
{
  "success": false,
  "data": null,
  "message": "에러 메시지"
}
```

### HTTP 상태 코드

| 코드 | 의미 |
|------|------|
| 200 | 성공 |
| 201 | 생성 성공 |
| 400 | 잘못된 요청 (유효성 검사 실패) |
| 404 | 리소스 없음 |
| 500 | 서버 내부 오류 |

### Content-Type
- 요청: `application/json`
- 응답: `application/json; charset=utf-8`

---

## 1. menu-service (Port 8081)

### 공통 타입 정의

**MenuResponse**
```json
{
  "id": 1,
  "name": "김치찌개",
  "restaurantName": "한솥뚝배기",
  "category": "한식",
  "priceRange": "1만원이하",
  "distance": "도보5분",
  "imageUrl": "https://example.com/image.jpg",
  "lastEatenAt": "2026-05-10T12:00:00",
  "avgRating": 4.4,
  "reviewCount": 5,
  "createdAt": "2026-05-01T09:00:00"
}
```

**ReviewResponse**
```json
{
  "id": 1,
  "menuId": 1,
  "nickname": "김철수",
  "rating": 5,
  "comment": "국물이 칼칼하니 딱 좋아요",
  "createdAt": "2026-05-10T12:30:00"
}
```

> `pin`은 응답에 포함하지 않는다.

---

### 1-1. 메뉴 목록 조회

```
GET /api/menus
```

**Query Parameters**

| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| `category` | String | No | `한식` \| `양식` \| `중식` |
| `priceRange` | String | No | `1만원이하` \| `1~2만원` \| `2만원이상` |
| `distance` | String | No | `도보5분` \| `도보10분` \| `배달가능` |

**요청 예시**
```
GET /api/menus?category=한식&priceRange=1만원이하
```

**응답 예시**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "name": "김치찌개",
      "restaurantName": "한솥뚝배기",
      "category": "한식",
      "priceRange": "1만원이하",
      "distance": "도보5분",
      "imageUrl": null,
      "lastEatenAt": "2026-05-10T12:00:00",
      "avgRating": 4.4,
      "reviewCount": 5,
      "createdAt": "2026-05-01T09:00:00"
    }
  ],
  "message": null
}
```

---

### 1-2. 메뉴 단건 조회

```
GET /api/menus/{id}
```

**Path Variables**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `id` | Long | 메뉴 ID |

**응답 예시**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "김치찌개",
    "restaurantName": "한솥뚝배기",
    "category": "한식",
    "priceRange": "1만원이하",
    "distance": "도보5분",
    "imageUrl": null,
    "lastEatenAt": "2026-05-10T12:00:00",
    "avgRating": 4.4,
    "reviewCount": 5,
    "createdAt": "2026-05-01T09:00:00"
  },
  "message": null
}
```

**에러 응답** (존재하지 않는 ID)
```json
{
  "success": false,
  "data": null,
  "message": "메뉴를 찾을 수 없습니다. id=999"
}
```

---

### 1-3. 메뉴 등록

```
POST /api/menus
```

**Request Body**

| 필드 | 타입 | 필수 | 유효성 | 설명 |
|------|------|------|--------|------|
| `name` | String | Yes | 최대 100자 | 메뉴 이름 |
| `restaurantName` | String | Yes | 최대 100자 | 가게 이름 |
| `category` | String | Yes | `한식`\|`양식`\|`중식` | 카테고리 |
| `priceRange` | String | Yes | `1만원이하`\|`1~2만원`\|`2만원이상` | 가격대 |
| `distance` | String | Yes | `도보5분`\|`도보10분`\|`배달가능` | 거리 |
| `imageUrl` | String | No | URL 형식, 최대 500자 | 사진 URL |

**요청 예시**
```json
{
  "name": "부대찌개",
  "restaurantName": "한솥뚝배기",
  "category": "한식",
  "priceRange": "1만원이하",
  "distance": "도보5분",
  "imageUrl": null
}
```

**응답 예시** `201 Created`
```json
{
  "success": true,
  "data": {
    "id": 17,
    "name": "부대찌개",
    "restaurantName": "한솥뚝배기",
    "category": "한식",
    "priceRange": "1만원이하",
    "distance": "도보5분",
    "imageUrl": null,
    "lastEatenAt": null,
    "avgRating": null,
    "reviewCount": 0,
    "createdAt": "2026-05-22T10:00:00"
  },
  "message": null
}
```

---

### 1-4. 메뉴 수정

```
PUT /api/menus/{id}
```

**Path Variables**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `id` | Long | 메뉴 ID |

**Request Body** — 1-3과 동일. 전체 필드 전송.

**응답 예시**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "김치찌개 (매운맛)",
    "restaurantName": "한솥뚝배기",
    "category": "한식",
    "priceRange": "1만원이하",
    "distance": "도보5분",
    "imageUrl": null,
    "lastEatenAt": "2026-05-10T12:00:00",
    "avgRating": 4.4,
    "reviewCount": 5,
    "createdAt": "2026-05-01T09:00:00"
  },
  "message": null
}
```

---

### 1-5. 메뉴 삭제

```
DELETE /api/menus/{id}
```

**Path Variables**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `id` | Long | 메뉴 ID |

**응답 예시**
```json
{
  "success": true,
  "data": null,
  "message": null
}
```

> 메뉴 삭제 시 연결된 `reviews`도 CASCADE 삭제된다.

---

### 1-6. 리뷰 목록 조회

```
GET /api/menus/{id}/reviews
```

**Path Variables**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `id` | Long | 메뉴 ID |

**응답 예시**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "menuId": 1,
      "nickname": "김철수",
      "rating": 5,
      "comment": "국물이 칼칼하니 딱 좋아요",
      "createdAt": "2026-05-10T12:30:00"
    }
  ],
  "message": null
}
```

---

### 1-7. 리뷰 작성

```
POST /api/menus/{id}/reviews
```

**Path Variables**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `id` | Long | 메뉴 ID |

**Request Body**

| 필드 | 타입 | 필수 | 유효성 | 설명 |
|------|------|------|--------|------|
| `nickname` | String | Yes | 최대 50자 | 작성자 이름 |
| `pin` | String | Yes | 숫자 4자리 | 수정/삭제용 PIN |
| `rating` | Integer | Yes | 1 ~ 5 | 별점 |
| `comment` | String | No | — | 한줄평 |

**요청 예시**
```json
{
  "nickname": "홍길동",
  "pin": "1234",
  "rating": 5,
  "comment": "정말 맛있어요!"
}
```

**응답 예시** `201 Created`
```json
{
  "success": true,
  "data": {
    "id": 61,
    "menuId": 1,
    "nickname": "홍길동",
    "rating": 5,
    "comment": "정말 맛있어요!",
    "createdAt": "2026-05-22T11:00:00"
  },
  "message": null
}
```

**에러 응답** (잘못된 별점)
```json
{
  "success": false,
  "data": null,
  "message": "별점은 1에서 5 사이여야 합니다"
}
```

---

### 1-8. 리뷰 수정

```
PUT /api/reviews/{id}
```

**Path Variables**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `id` | Long | 리뷰 ID |

**Request Body**

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `pin` | String | Yes | 작성 시 등록한 4자리 PIN |
| `rating` | Integer | Yes | 별점 1 ~ 5 |
| `comment` | String | No | 한줄평 |

**요청 예시**
```json
{
  "pin": "1234",
  "rating": 4,
  "comment": "재방문했는데 여전히 맛있어요"
}
```

**에러 응답** (PIN 불일치)
```json
{
  "success": false,
  "data": null,
  "message": "PIN이 일치하지 않습니다"
}
```

---

### 1-9. 리뷰 삭제

```
DELETE /api/reviews/{id}
```

**Path Variables**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `id` | Long | 리뷰 ID |

**Request Body**

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `pin` | String | Yes | 작성 시 등록한 4자리 PIN |

**요청 예시**
```json
{
  "pin": "1234"
}
```

**응답 예시**
```json
{
  "success": true,
  "data": null,
  "message": null
}
```

---

### 1-10. 오늘 먹었어요 (last_eaten_at 업데이트)

```
PATCH /api/menus/{id}/eat
```

**Path Variables**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `id` | Long | 메뉴 ID |

**Request Body** 없음

**응답 예시**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "lastEatenAt": "2026-05-22T12:00:00"
  },
  "message": null
}
```

---

## 2. picker-service (Port 8082)

### 2-1. 조건 기반 랜덤 메뉴 뽑기

```
GET /api/picker/pick
```

**Query Parameters**

| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| `categoryMode` | String | Yes | `simple` \| `weighted` |
| `category` | String | No | `한식`\|`양식`\|`중식`\|`전체` — `simple` 모드일 때 |
| `koreanWeight` | Integer | No | 한식 비중 (0 이상) — `weighted` 모드일 때 |
| `westernWeight` | Integer | No | 양식 비중 (0 이상) — `weighted` 모드일 때 |
| `chineseWeight` | Integer | No | 중식 비중 (0 이상) — `weighted` 모드일 때 |
| `minRating` | Double | No | 최소 평균 별점. 미전달 또는 `null` = 상관없음 |
| `priceRange` | String | No | `1만원이하`\|`1~2만원`\|`2만원이상`. 미전달 = 상관없음 |
| `distance` | String | No | `도보5분`\|`도보10분`\|`배달가능`. 미전달 = 상관없음 |

**요청 예시 — simple 모드**
```
GET /api/picker/pick?categoryMode=simple&category=한식&minRating=4.0&priceRange=1만원이하
```

**요청 예시 — weighted 모드**
```
GET /api/picker/pick?categoryMode=weighted&koreanWeight=70&westernWeight=20&chineseWeight=10&distance=도보5분
```

**응답 예시**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "김치찌개",
    "restaurantName": "한솥뚝배기",
    "category": "한식",
    "priceRange": "1만원이하",
    "distance": "도보5분",
    "imageUrl": null,
    "lastEatenAt": "2026-05-10T12:00:00",
    "avgRating": 4.4,
    "reviewCount": 5
  },
  "message": null
}
```

**에러 응답** (조건에 맞는 메뉴 없음)
```json
{
  "success": false,
  "data": null,
  "message": "조건에 맞는 메뉴가 없습니다"
}
```

---

## 3. ai-service (Port 8083)

### 공통 타입 정의

**RecommendationResultItem**
```json
{
  "rank": 1,
  "menuId": 14,
  "menuName": "마라탕",
  "restaurantName": "마라하오",
  "category": "중식",
  "priceRange": "1~2만원",
  "distance": "도보5분",
  "avgRating": 4.8,
  "reason": "더운 날씨에 시원하면서 얼큰한 마라탕이 제격입니다."
}
```

---

### 3-1. AI 자연어 메뉴 추천

```
POST /api/ai/recommend
```

**Request Body**

| 필드 | 타입 | 필수 | 유효성 | 설명 |
|------|------|------|--------|------|
| `userInput` | String | Yes | 최대 500자 | 자연어 입력 |

**요청 예시**
```json
{
  "userInput": "오늘 날씨도 덥고 어제 한식 먹었어. 가볍게 먹고 싶어"
}
```

**응답 예시** `201 Created`
```json
{
  "success": true,
  "data": {
    "id": 1,
    "userInput": "오늘 날씨도 덥고 어제 한식 먹었어. 가볍게 먹고 싶어",
    "results": [
      {
        "rank": 1,
        "menuId": 10,
        "menuName": "샌드위치",
        "restaurantName": "써브웨이",
        "category": "양식",
        "priceRange": "1만원이하",
        "distance": "도보5분",
        "avgRating": 3.4,
        "reason": "가볍고 시원하게 먹기 좋은 샌드위치로 더운 날씨에 잘 어울립니다."
      },
      {
        "rank": 2,
        "menuId": 7,
        "menuName": "파스타",
        "restaurantName": "라보카",
        "category": "양식",
        "priceRange": "1~2만원",
        "distance": "도보10분",
        "avgRating": 4.2,
        "reason": "한식 대신 양식으로 가볍게 즐길 수 있는 파스타를 추천합니다."
      },
      {
        "rank": 3,
        "menuId": 12,
        "menuName": "짜장면",
        "restaurantName": "홍콩반점",
        "category": "중식",
        "priceRange": "1만원이하",
        "distance": "배달가능",
        "avgRating": 3.0,
        "reason": "부담 없이 먹을 수 있는 중식으로 한식과 다른 맛을 즐길 수 있습니다."
      }
    ],
    "createdAt": "2026-05-22T12:00:00"
  },
  "message": null
}
```

**에러 응답** (Gemini API 파싱 실패)
```json
{
  "success": false,
  "data": null,
  "message": "AI 추천 결과를 처리하는 중 오류가 발생했습니다"
}
```

---

### 3-2. 추천 히스토리 조회

```
GET /api/ai/history
```

**Query Parameters**

| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| `limit` | Integer | No | 조회 개수. 기본값 20, 최대 50 |

**요청 예시**
```
GET /api/ai/history?limit=10
```

**응답 예시**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "userInput": "오늘 날씨도 덥고 어제 한식 먹었어. 가볍게 먹고 싶어",
      "results": [
        {
          "rank": 1,
          "menuId": 10,
          "menuName": "샌드위치",
          "restaurantName": "써브웨이",
          "category": "양식",
          "priceRange": "1만원이하",
          "distance": "도보5분",
          "avgRating": 3.4,
          "reason": "가볍고 시원하게 먹기 좋은 샌드위치입니다."
        },
        {
          "rank": 2,
          "menuId": 7,
          "menuName": "파스타",
          "restaurantName": "라보카",
          "category": "양식",
          "priceRange": "1~2만원",
          "distance": "도보10분",
          "avgRating": 4.2,
          "reason": "한식 대신 양식으로 즐기기 좋습니다."
        },
        {
          "rank": 3,
          "menuId": 12,
          "menuName": "짜장면",
          "restaurantName": "홍콩반점",
          "category": "중식",
          "priceRange": "1만원이하",
          "distance": "배달가능",
          "avgRating": 3.0,
          "reason": "부담 없이 먹을 수 있는 선택입니다."
        }
      ],
      "createdAt": "2026-05-22T12:00:00"
    }
  ],
  "message": null
}
```

---

## 4. CORS 설정

`common/config/CorsConfig.java`에 프론트엔드 origin을 허용한다.

| 허용 Origin |
|-------------|
| `http://localhost:3000` |

> 단일 프론트엔드(3000)에서 단일 백엔드(8080)로 호출한다.

---

## 5. API 엔드포인트 전체 요약

| 서비스 | Method | Path | 설명 |
|--------|--------|------|------|
| menu | GET | /api/menus | 메뉴 목록 조회 |
| menu | GET | /api/menus/{id} | 메뉴 단건 조회 |
| menu | POST | /api/menus | 메뉴 등록 |
| menu | PUT | /api/menus/{id} | 메뉴 수정 |
| menu | DELETE | /api/menus/{id} | 메뉴 삭제 |
| menu | PATCH | /api/menus/{id}/eat | 오늘 먹었어요 |
| menu | GET | /api/menus/{id}/reviews | 리뷰 목록 |
| menu | POST | /api/menus/{id}/reviews | 리뷰 작성 |
| menu | PUT | /api/reviews/{id} | 리뷰 수정 (PIN 검증) |
| menu | DELETE | /api/reviews/{id} | 리뷰 삭제 (PIN 검증) |
| picker | GET | /api/picker/pick | 조건 기반 랜덤 뽑기 |
| ai | POST | /api/ai/recommend | AI 자연어 추천 |
| ai | GET | /api/ai/history | 추천 히스토리 조회 |
