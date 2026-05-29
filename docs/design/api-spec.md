# API Specification
> 버전: v1.1
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

### 공통 타입 — CodeItem

카테고리·가격대·거리 필드는 응답에서 아래 객체 형식으로 반환된다.

```json
{ "id": 1, "code": "KOREAN", "label": "한식" }
```

> `id`는 common_codes 테이블의 PK, `code`는 코드값, `label`은 화면 표시용 한글 레이블이다.

---

## 1. menu 도메인

### 공통 타입 정의

**MenuResponse**
```json
{
  "id": 1,
  "name": "김치찌개",
  "restaurantName": "한솥뚝배기",
  "category": { "id": 1, "code": "KOREAN", "label": "한식" },
  "priceRange": { "id": 4, "code": "UNDER_10000", "label": "1만원이하" },
  "distance": { "id": 7, "code": "WALK_5MIN", "label": "도보5분" },
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
| `category` | String | No | `KOREAN` \| `WESTERN` \| `CHINESE` |
| `priceRange` | String | No | `UNDER_10000` \| `BETWEEN_10000_20000` \| `OVER_20000` |
| `distance` | String | No | `WALK_5MIN` \| `WALK_10MIN` \| `DELIVERY` |

**요청 예시**
```
GET /api/menus?category=KOREAN&priceRange=UNDER_10000
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
      "category": { "id": 1, "code": "KOREAN", "label": "한식" },
      "priceRange": { "id": 4, "code": "UNDER_10000", "label": "1만원이하" },
      "distance": { "id": 7, "code": "WALK_5MIN", "label": "도보5분" },
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
    "category": { "id": 1, "code": "KOREAN", "label": "한식" },
    "priceRange": { "id": 4, "code": "UNDER_10000", "label": "1만원이하" },
    "distance": { "id": 7, "code": "WALK_5MIN", "label": "도보5분" },
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
| `category` | String | Yes | `KOREAN`\|`WESTERN`\|`CHINESE` | 카테고리 코드값 |
| `priceRange` | String | Yes | `UNDER_10000`\|`BETWEEN_10000_20000`\|`OVER_20000` | 가격대 코드값 |
| `distance` | String | Yes | `WALK_5MIN`\|`WALK_10MIN`\|`DELIVERY` | 거리 코드값 |
| `imageUrl` | String | No | URL 형식, 최대 500자 | 사진 URL |

**요청 예시**
```json
{
  "name": "부대찌개",
  "restaurantName": "한솥뚝배기",
  "category": "KOREAN",
  "priceRange": "UNDER_10000",
  "distance": "WALK_5MIN",
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
    "category": { "id": 1, "code": "KOREAN", "label": "한식" },
    "priceRange": { "id": 4, "code": "UNDER_10000", "label": "1만원이하" },
    "distance": { "id": 7, "code": "WALK_5MIN", "label": "도보5분" },
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
    "category": { "id": 1, "code": "KOREAN", "label": "한식" },
    "priceRange": { "id": 4, "code": "UNDER_10000", "label": "1만원이하" },
    "distance": { "id": 7, "code": "WALK_5MIN", "label": "도보5분" },
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

## 2. picker 도메인

### 2-1. 단순 선택 — 조건 기반 랜덤 메뉴 뽑기

```
GET /api/picker/pick
```

**Query Parameters**

| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| `category` | String | No | `KOREAN` \| `WESTERN` \| `CHINESE`. 미전달 = 전체 |
| `minRating` | Double | No | 최소 평균 별점. 미전달 = 상관없음 |
| `priceRange` | String | No | `UNDER_10000` \| `BETWEEN_10000_20000` \| `OVER_20000`. 미전달 = 상관없음 |
| `distance` | String | No | `WALK_5MIN` \| `WALK_10MIN` \| `DELIVERY`. 미전달 = 상관없음 |

**요청 예시**
```
GET /api/picker/pick?category=KOREAN&minRating=4.0&priceRange=UNDER_10000
```

**응답 예시**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "김치찌개",
    "restaurantName": "한솥뚝배기",
    "category": { "id": 1, "code": "KOREAN", "label": "한식" },
    "priceRange": { "id": 4, "code": "UNDER_10000", "label": "1만원이하" },
    "distance": { "id": 7, "code": "WALK_5MIN", "label": "도보5분" },
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

### 2-2. 비중 선택 — 카테고리 가중치 기반 랜덤 메뉴 뽑기

```
POST /api/picker/pick
```

**Request Body**

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `weights` | Map\<String, Integer\> | Yes | 카테고리별 비중. 키는 코드값(`KOREAN`, `WESTERN`, `CHINESE`), 값은 0 이상 정수. 빈 Map 또는 합계 0이면 전 카테고리 균등 선택 |
| `minRating` | Double | No | 최소 평균 별점. null = 상관없음 |
| `priceRange` | String | No | `UNDER_10000` \| `BETWEEN_10000_20000` \| `OVER_20000`. null = 상관없음 |
| `distance` | String | No | `WALK_5MIN` \| `WALK_10MIN` \| `DELIVERY`. null = 상관없음 |

**요청 예시**
```json
{
  "weights": {
    "KOREAN": 70,
    "WESTERN": 20,
    "CHINESE": 10
  },
  "minRating": null,
  "priceRange": "UNDER_10000",
  "distance": null
}
```

**응답 예시** — 2-1과 동일 형식
```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "김치찌개",
    "restaurantName": "한솥뚝배기",
    "category": { "id": 1, "code": "KOREAN", "label": "한식" },
    "priceRange": { "id": 4, "code": "UNDER_10000", "label": "1만원이하" },
    "distance": { "id": 7, "code": "WALK_5MIN", "label": "도보5분" },
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

## 3. ai 도메인

### 공통 타입 정의

**RecommendationResultItem**
```json
{
  "rank": 1,
  "menuId": 14,
  "menuName": "마라탕",
  "restaurantName": "마라하오",
  "category": { "id": 3, "code": "CHINESE", "label": "중식" },
  "priceRange": { "id": 5, "code": "BETWEEN_10000_20000", "label": "1~2만원" },
  "distance": { "id": 7, "code": "WALK_5MIN", "label": "도보5분" },
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
        "category": { "id": 2, "code": "WESTERN", "label": "양식" },
        "priceRange": { "id": 4, "code": "UNDER_10000", "label": "1만원이하" },
        "distance": { "id": 7, "code": "WALK_5MIN", "label": "도보5분" },
        "avgRating": 3.4,
        "reason": "가볍고 시원하게 먹기 좋은 샌드위치로 더운 날씨에 잘 어울립니다."
      },
      {
        "rank": 2,
        "menuId": 7,
        "menuName": "파스타",
        "restaurantName": "라보카",
        "category": { "id": 2, "code": "WESTERN", "label": "양식" },
        "priceRange": { "id": 5, "code": "BETWEEN_10000_20000", "label": "1~2만원" },
        "distance": { "id": 8, "code": "WALK_10MIN", "label": "도보10분" },
        "avgRating": 4.2,
        "reason": "한식 대신 양식으로 가볍게 즐길 수 있는 파스타를 추천합니다."
      },
      {
        "rank": 3,
        "menuId": 12,
        "menuName": "짜장면",
        "restaurantName": "홍콩반점",
        "category": { "id": 3, "code": "CHINESE", "label": "중식" },
        "priceRange": { "id": 4, "code": "UNDER_10000", "label": "1만원이하" },
        "distance": { "id": 9, "code": "DELIVERY", "label": "배달가능" },
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
          "category": { "id": 2, "code": "WESTERN", "label": "양식" },
          "priceRange": { "id": 4, "code": "UNDER_10000", "label": "1만원이하" },
          "distance": { "id": 7, "code": "WALK_5MIN", "label": "도보5분" },
          "avgRating": 3.4,
          "reason": "가볍고 시원하게 먹기 좋은 샌드위치입니다."
        },
        {
          "rank": 2,
          "menuId": 7,
          "menuName": "파스타",
          "restaurantName": "라보카",
          "category": { "id": 2, "code": "WESTERN", "label": "양식" },
          "priceRange": { "id": 5, "code": "BETWEEN_10000_20000", "label": "1~2만원" },
          "distance": { "id": 8, "code": "WALK_10MIN", "label": "도보10분" },
          "avgRating": 4.2,
          "reason": "한식 대신 양식으로 즐기기 좋습니다."
        },
        {
          "rank": 3,
          "menuId": 12,
          "menuName": "짜장면",
          "restaurantName": "홍콩반점",
          "category": { "id": 3, "code": "CHINESE", "label": "중식" },
          "priceRange": { "id": 4, "code": "UNDER_10000", "label": "1만원이하" },
          "distance": { "id": 9, "code": "DELIVERY", "label": "배달가능" },
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

### 3-3. 추천 히스토리 삭제

```
DELETE /api/ai/history
```

추천 히스토리 전체를 삭제한다. 실제 행을 제거하지 않고 soft delete로 처리한다 (`ai_recommendations.deleted_at`에 삭제 시각을 기록). 삭제된 히스토리는 `GET /api/ai/history` 조회 결과에서 제외된다.

**Query Parameters**

없음.

**요청 예시**
```
DELETE /api/ai/history
```

**응답 예시**
```json
{
  "success": true,
  "data": null,
  "message": null
}
```

> 이미 삭제된(미삭제 히스토리가 없는) 상태에서 호출해도 정상 응답한다.

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
| picker | GET | /api/picker/pick | 단순 선택 랜덤 뽑기 |
| picker | POST | /api/picker/pick | 비중 선택 랜덤 뽑기 |
| ai | POST | /api/ai/recommend | AI 자연어 추천 |
| ai | GET | /api/ai/history | 추천 히스토리 조회 |
| ai | DELETE | /api/ai/history | 추천 히스토리 전체 삭제 (soft delete) |
