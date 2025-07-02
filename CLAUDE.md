# 아무거나 요정 - 뭐 먹을래? 📱🍽️

## 1. 프로젝트 개요

### 앱 이름
**아무거나 요정 - 뭐 먹을래?**

### 앱 설명
현재 위치 기반으로 다양한 음식 카테고리별 맛집을 추천해주는 모바일 애플리케이션입니다. "오늘 뭐 먹지?"라는 일상적인 고민을 해결해주는 당신만의 음식 요정이 되어드립니다.

### 개발 목표
- 사용자의 현재 위치를 기반으로 한 맞춤형 맛집 추천
- 직관적이고 사용하기 쉬운 UI/UX 제공
- 다양한 음식 카테고리를 통한 폭넓은 선택권 제공
- 회원 및 비회원 모두가 사용할 수 있는 접근성
- SOLID 원칙을 준수해서 개발

---

## 2. 타겟 사용자

### 주요 타겟
- **연령대**: 20~40대
- **특성**: 
  - 음식 선택에 어려움을 겪는 사람들
  - 새로운 맛집을 발견하고 싶어하는 사람들
  - 위치 기반 서비스를 자주 이용하는 사람들
  - 외식을 자주 하는 직장인, 학생, 커플 등

### 사용 시나리오
- 점심시간 직장 근처 맛집 찾기
- 데이트 장소 근처 분위기 좋은 레스토랑 찾기
- 여행지에서 현지 맛집 발견하기
- 새로운 동네로 이사 후 주변 맛집 탐색

---

## 3. 핵심 기능

### 3.1 메인 기능

#### 🍜 음식 카테고리 분류
- **한식**: 한정식, 찌개류, 구이류, 면요리 등
- **일식**: 초밥, 라멘, 돈부리, 야키토리 등
- **중식**: 짜장면, 탕수육, 마라탕, 딤섬 등
- **양식**: 파스타, 피자, 스테이크, 햄버거 등
- **기타 아시아**: 태국, 베트남, 인도 요리 등
- **카페/디저트**: 커피, 베이커리, 아이스크림 등
- **술집/바**: 호프집, 와인바, 칵테일바 등

#### 📍 위치 기반 맛집 추천
- **현재 위치 자동 감지**: GPS를 통한 실시간 위치 파악
- **반경 설정**: 500m, 1km, 2km 선택 가능
- **거리순 정렬**: 가까운 순서대로 맛집 나열
- **길찾기 연동**: 네이버 지도, 카카오맵 연결

#### 🎲 랜덤 추천 기능
- **"아무거나" 버튼**: 모든 카테고리에서 랜덤 추천
- **카테고리별 랜덤**: 선택한 카테고리 내에서 랜덤 추천
- **필터 적용 랜덤**: 가격대, 평점 등 조건을 설정한 랜덤 추천

### 3.2 사용자 관리

#### 👤 회원 시스템
- **회원가입/로그인**: 이메일, 소셜 로그인(카카오, 네이버, 구글)
- **비회원 이용**: 기본 검색 및 조회 기능 이용 가능
- **프로필 관리**: 닉네임, 프로필 사진, 선호 음식 카테고리 설정

#### ⭐ 리뷰 및 평점 시스템 (회원 전용)
- **평점**: 5점 만점 별점 시스템
- **리뷰 작성**: 텍스트 리뷰 및 사진 첨부 가능
- **태그 시스템**: #맛있어요 #분위기좋아요 #가성비 등
- **좋아요/신고**: 다른 사용자 리뷰에 대한 반응

### 3.3 부가 기능

#### 🔖 즐겨찾기 및 방문 기록 (회원 전용)
- **찜하기**: 관심 있는 맛집 저장
- **방문 기록**: 다녀온 맛집 히스토리
- **재방문 의사**: 다시 가고 싶은 곳 표시

#### 🔍 검색 및 필터
- **키워드 검색**: 맛집명, 메뉴명으로 검색
- **상세 필터**: 
  - 가격대 (저렴, 보통, 비싼)
  - 평점 (4점 이상, 3점 이상 등)
  - 영업시간 (현재 영업중)
  - 주차 가능 여부

### 3.4 관리자 웹 시스템

#### 🔧 관리자 권한 체계
- **슈퍼 관리자**: 모든 권한 (계정 생성/삭제, 시스템 설정)
- **일반 관리자**: 맛집/리뷰 관리, 회원 관리
- **콘텐츠 관리자**: 맛집 정보 수정, 리뷰 검토만 가능

#### 📊 대시보드 기능
- **실시간 통계**: 오늘 가입자, 리뷰 수, 앱 사용량
- **트렌드 분석**: 인기 맛집 TOP 10, 카테고리별 검색량
- **지역별 현황**: 지역별 맛집 분포 및 이용률
- **사용자 활동**: 월별 MAU, DAU 추이
- **수익 현황**: 광고 수익, 프리미엄 구독 현황

#### 🏪 맛집 관리 시스템
- **맛집 등록**: 수동 맛집 정보 입력 (이름, 주소, 카테고리, 연락처)
- **위치 설정**: 지도에서 직접 클릭하여 정확한 좌표 설정
- **사진 업로드**: 맛집 대표 사진 및 메뉴 사진 관리
- **영업시간 설정**: 요일별 상세 영업시간 입력
- **일괄 수정**: CSV 파일을 통한 대량 맛집 정보 수정
- **맛집 상태 관리**: 임시 폐업, 영구 폐업, 정보 수정 필요 등

#### 👥 회원 관리 시스템
- **회원 목록**: 페이징, 검색, 필터링 (가입일, 활동 상태)
- **회원 상세 정보**: 리뷰 작성 이력, 찜한 맛집, 방문 기록
- **계정 상태 관리**: 활성화/비활성화, 임시 정지, 영구 정지
- **신고 처리**: 부적절한 행동 신고 검토 및 처리
- **통계**: 연령대별, 지역별 회원 분포 분석

#### 📝 리뷰 관리 시스템
- **리뷰 모니터링**: 최신 리뷰 실시간 확인
- **신고된 리뷰**: 사용자 신고 리뷰 검토 및 처리
- **부적절 리뷰 필터링**: 욕설, 광고성 리뷰 자동 감지
- **리뷰 통계**: 평점 분포, 리뷰 작성 트렌드
- **사진 관리**: 부적절한 사진 검토 및 삭제

#### ⚙️ 시스템 설정
- **카테고리 관리**: 음식 카테고리 추가/수정/삭제
- **공지사항**: 앱 내 공지사항 작성 및 발송
- **푸시 알림**: 전체/개별 사용자 푸시 알림 발송
- **앱 버전 관리**: 최신 버전 정보, 강제 업데이트 설정
- **이용약관 관리**: 개인정보처리방침, 이용약관 수정

---

## 4. 화면 구성

### 4.1 메인 화면
```
┌─────────────────────────────┐
│  🧚‍♀️ 아무거나 요정           │
│     뭐 먹을래?               │
├─────────────────────────────┤
│  📍 현재 위치: 강남구        │
│  🎲 [아무거나 추천받기]      │
├─────────────────────────────┤
│  음식 카테고리              │
│  🍚 한식  🍣 일식  🍜 중식   │
│  🍝 양식  ☕ 카페  🍺 술집   │
└─────────────────────────────┘
```

### 4.2 카테고리별 맛집 리스트
```
┌─────────────────────────────┐
│  🍚 한식 맛집 (반경 1km)     │
├─────────────────────────────┤
│  📍 500m  ⭐ 4.5  💰💰      │
│  🏪 할머니 손맛 한정식       │
│  📱 리뷰 45개 | 👍 찜 123   │
├─────────────────────────────┤
│  📍 800m  ⭐ 4.2  💰        │
│  🏪 맛있는 김치찌개          │
│  📱 리뷰 28개 | 👍 찜 89    │
└─────────────────────────────┘
```

### 4.3 상세 정보 화면
```
┌─────────────────────────────┐
│  🏪 할머니 손맛 한정식       │
│  ⭐ 4.5 (리뷰 45개)         │
│  📍 500m | 💰💰 보통가격    │
├─────────────────────────────┤
│  📞 02-1234-5678           │
│  🕐 11:00 - 21:00          │
│  🅿️ 주차 가능               │
├─────────────────────────────┤
│  [🗺️ 길찾기] [⭐ 찜하기]    │
│  [✍️ 리뷰쓰기] [📞 전화]     │
└─────────────────────────────┘
```

### 4.4 관리자 웹 화면 구성

#### 📋 대시보드
```
┌─────────────────────────────┐
│  🏪 아무거나 요정 - 관리자      │
├─────────────────────────────┤
│  📈 오늘 통계                   │
│  신규 가입: 23명 | 리뷰: 45개    │
│  현재 사용자: 1,234명             │
├─────────────────────────────┤
│  📅 월별 사용자 증가 차트       │
│  [===================] 15%↑   │
├─────────────────────────────┤
│  🍆 인기 맛집 TOP 5              │
│  1. 할머니 손맛 한정식         │
│  2. 맛있는 김치찌개              │
└─────────────────────────────┘
```

#### 🏪 맛집 관리 페이지
```
┌─────────────────────────────┐
│  🏪 맛집 관리                    │
├─────────────────────────────┤
│  [➕ 맛집 추가] [📄 CSV 업로드]  │
├─────────────────────────────┤
│  검색: [맛집명...] 카테고리: [한식▼] │
├─────────────────────────────┤
│  🏪 할머니 손맛 한정식         │
│  📍 서울 강남구 | ⭐ 4.5 | 📝 45개 │
│  [✏️ 수정] [❌ 삭제] [📈 통계]   │
├─────────────────────────────┤
│  🍜 맛있는 김치찌개             │
│  📍 서울 마포구 | ⭐ 4.2 | 📝 28개 │
└─────────────────────────────┘
```

#### 👥 회원 관리 페이지
```
┌─────────────────────────────┐
│  👥 회원 관리                     │
├─────────────────────────────┤
│  전체: 1,245명 | 활성: 1,123명      │
│  오늘 가입: 23명 | 신고: 3건       │
├─────────────────────────────┤
│  필터: [활성▼] 검색: [닉네임...] │
├─────────────────────────────┤
│  👤 김맛집러버 | 가입: 2024.01.15 │
│  ⭐ 리뷰 15개 | 👍 찜 23개        │
│  [📄 상세] [⏸️ 정지] [📧 메시지]  │
└─────────────────────────────┘
```

#### 📝 리뷰 관리 페이지
```
┌─────────────────────────────┐
│  📝 리뷰 관리                     │
├─────────────────────────────┤
│  전체: 3,456개 | 신고: 12개         │
│  평균 평점: 4.2점 | 사진 있음: 78%  │
├─────────────────────────────┤
│  필터: [신고됨▼] 평점: [1-5⭐]      │
├─────────────────────────────┤
│  ⭐ 5점 | 🏪 할머니 손맛 한정식   │
│  👤 김맛집러버 | 2024.06.25      │
│  "진짜 맛있어요! 추천합니다."     │
│  [📄 상세] [✅ 승인] [❌ 삭제]      │
└─────────────────────────────┘
```

---

## 5. 기술 스택

### 5.1 프론트엔드

#### 모바일 앱
- **프레임워크**: Flutter 3.x
- **상태관리**: Provider
- **지도 라이브러리**: flutter_naver_map
- **HTTP 통신**: Dio
- **로컬 저장소**: SharedPreferences, Hive

#### 관리자 웹
- **프레임워크**: React 18.x
- **상태관리**: Recoil
- **UI 라이브러리**: Material-UI
- **HTTP 통신**: Axios
- **라우팅**: React Router
- **차트**: Chart.js
- **빌드 도구**: Create React App

### 5.2 백엔드
- **프레임워크**: Spring Boot 3.x
- **언어**: Java 17
- **데이터베이스**: 
  - **메인 DB**: PostgreSQL 15+
  - **캐시**: Redis 7+
- **인증**: Spring Security + JWT
- **API 문서**: SpringDoc OpenAPI (Swagger)
- **파일 저장**: 로컬 파일 시스템

### 5.3 외부 API 및 서비스
- **위치 서비스**: 
  - GPS (디바이스 기본)
  - 네이버 지도 API
- **소셜 로그인**: 
  - 카카오 SDK
  - 네이버 로그인 API
  - 구글 로그인 API
- **맛집 데이터** (선택사항):
  - 카카오 로컬 API
  - 카카오 앱 키 
  1. 네이티브 앱 키 : 89ba71a65317fb573fda6a8c6f6fe32d
  2. REST API 키 : 77390de39f50e1057040c195d12abc1c
  3. JavaScript키 : 8e22cc708c792e386c4b5389e4eb8ba7
  4. Admin 키: 84a68db6b4ad5e19539f83a61b7eb530

### 5.4 개발 환경
- **버전 관리**: Git + GitHub
- **CI/CD**: GitHub Actions
- **배포**: 
  - **백엔드**: Docker
  - **앱**: Google Play Store, Apple App Store

### 5.5 api key
- **NAVER지도 API KEY** : Client ID : dm11rcv5h0, Client Secret: f4MMOWvsOAx9CEthx9vEN9nSJjfIlve3CAJfKj2I
     

---

## 6. 데이터베이스 설계

### 6.1 주요 테이블

#### Users (사용자)
```sql
CREATE TABLE users (
    user_id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255), -- 소셜 로그인 시 NULL 가능
    nickname VARCHAR(50) NOT NULL,
    profile_image_url VARCHAR(500),
    social_type VARCHAR(20), -- KAKAO, NAVER, GOOGLE, LOCAL
    social_id VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Restaurants (맛집)
```sql
CREATE TABLE restaurants (
    restaurant_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(50) NOT NULL, -- 한식, 일식, 중식 등
    address VARCHAR(500) NOT NULL,
    latitude DECIMAL(10, 8) NOT NULL,
    longitude DECIMAL(11, 8) NOT NULL,
    phone VARCHAR(20),
    business_hours TEXT,
    price_range VARCHAR(20), -- 저렴, 보통, 비싼
    parking_available BOOLEAN DEFAULT FALSE,
    average_rating DECIMAL(3, 2) DEFAULT 0.0,
    review_count INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Reviews (리뷰)
```sql
CREATE TABLE reviews (
    review_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(user_id),
    restaurant_id BIGINT REFERENCES restaurants(restaurant_id),
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    content TEXT,
    photos TEXT[], -- 사진 URL 배열
    tags VARCHAR(255), -- 해시태그 콤마 구분
    like_count INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Favorites (찜)
```sql
CREATE TABLE favorites (
    favorite_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(user_id),
    restaurant_id BIGINT REFERENCES restaurants(restaurant_id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, restaurant_id)
);
```

#### Admins (관리자)
```sql
CREATE TABLE admins (
    admin_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    role VARCHAR(20) DEFAULT 'ADMIN',
    is_active BOOLEAN DEFAULT TRUE,
    last_login_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Visit_History (방문 기록)
```sql
CREATE TABLE visit_history (
    visit_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(user_id),
    restaurant_id BIGINT REFERENCES restaurants(restaurant_id),
    visit_date DATE NOT NULL,
    want_to_revisit BOOLEAN DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## 7. API 설계

### 7.1 사용자 관련 API
```
POST /api/auth/register - 회원가입
POST /api/auth/login - 로그인
POST /api/auth/social-login - 소셜 로그인
GET /api/users/profile - 프로필 조회
PUT /api/users/profile - 프로필 수정
```

### 7.2 맛집 관련 API
```
GET /api/restaurants - 맛집 목록 조회 (위치, 카테고리 필터)
GET /api/restaurants/{id} - 맛집 상세 조회
GET /api/restaurants/random - 랜덤 맛집 추천
GET /api/restaurants/categories - 카테고리 목록
```

### 7.3 리뷰 관련 API
```
POST /api/reviews - 리뷰 작성
GET /api/reviews/restaurant/{restaurantId} - 특정 맛집 리뷰 목록
PUT /api/reviews/{id} - 리뷰 수정
DELETE /api/reviews/{id} - 리뷰 삭제
```

### 7.4 관리자 API

#### 인증 및 계정 관리
```
POST /api/admin/auth/login - 관리자 로그인
POST /api/admin/auth/logout - 로그아웃
GET /api/admin/auth/profile - 관리자 프로필 조회
PUT /api/admin/auth/password - 비밀번호 변경
```

#### 대시보드 및 통계
```
GET /api/admin/dashboard/stats - 대시보드 기본 통계
GET /api/admin/dashboard/trends - 사용자 증가 트렌드
GET /api/admin/dashboard/popular-restaurants - 인기 맛집 TOP 10
GET /api/admin/statistics/users - 사용자 통계 (연령대, 지역별)
GET /api/admin/statistics/restaurants - 맛집 통계 (카테고리별, 지역별)
GET /api/admin/statistics/reviews - 리뷰 통계 (평점 분포, 월별 증가)
```

#### 맛집 관리
```
GET /api/admin/restaurants - 맛집 목록 조회 (페이징, 검색, 필터)
GET /api/admin/restaurants/{id} - 맛집 상세 정보
POST /api/admin/restaurants - 맛집 등록
PUT /api/admin/restaurants/{id} - 맛집 정보 수정
DELETE /api/admin/restaurants/{id} - 맛집 삭제
POST /api/admin/restaurants/bulk-upload - CSV 파일을 통한 대량 등록
PUT /api/admin/restaurants/{id}/status - 맛집 상태 변경 (활성/비활성)
POST /api/admin/restaurants/{id}/photos - 맛집 사진 업로드
```

#### 회원 관리
```
GET /api/admin/users - 회원 목록 조회 (페이징, 검색, 필터)
GET /api/admin/users/{id} - 회원 상세 정보
PUT /api/admin/users/{id}/status - 회원 상태 변경 (활성/정지/탈퇴)
GET /api/admin/users/{id}/reviews - 회원의 리뷰 내역
GET /api/admin/users/{id}/favorites - 회원의 찜한 맛집
POST /api/admin/users/{id}/message - 회원에게 메시지 발송
GET /api/admin/reports/users - 사용자 신고 목록
PUT /api/admin/reports/users/{id} - 사용자 신고 처리
```

#### 리뷰 관리
```
GET /api/admin/reviews - 리뷰 목록 조회 (페이징, 검색, 필터)
GET /api/admin/reviews/{id} - 리뷰 상세 정보
DELETE /api/admin/reviews/{id} - 리뷰 삭제
PUT /api/admin/reviews/{id}/status - 리뷰 상태 변경 (승인/숨김)
GET /api/admin/reports/reviews - 신고된 리뷰 목록
PUT /api/admin/reports/reviews/{id} - 리뷰 신고 처리
DELETE /api/admin/reviews/{id}/photos/{photoId} - 리뷰 사진 삭제
```

#### 시스템 설정
```
GET /api/admin/categories - 음식 카테고리 목록
POST /api/admin/categories - 카테고리 추가
PUT /api/admin/categories/{id} - 카테고리 수정
DELETE /api/admin/categories/{id} - 카테고리 삭제
GET /api/admin/notices - 공지사항 목록
POST /api/admin/notices - 공지사항 작성
PUT /api/admin/notices/{id} - 공지사항 수정
DELETE /api/admin/notices/{id} - 공지사항 삭제
POST /api/admin/push-notifications - 푸시 알림 발송
GET /api/admin/app-settings - 앱 설정 조회
PUT /api/admin/app-settings - 앱 설정 수정
```

---

## 8. 개발 일정

### Phase 1 (6주) - MVP 개발
- **1-2주**: 백엔드 기본 구조 및 DB 설계
- **3-4주**: 모바일 앱 기본 UI 및 위치 기반 맛집 조회
- **5-6주**: 카테고리 시스템 및 기본 검색 기능

### Phase 2 (4주) - 회원 시스템
- **1-2주**: 회원가입/로그인 및 소셜 로그인
- **3-4주**: 리뷰 및 평점 시스템 구현

### Phase 3 (5주) - 관리자 웹 시스템
- **1주**: 관리자 인증 및 기본 백엔드 API 개발
- **2주**: React 관리자 웹 기본 설정 및 대시보드 개발
- **3주**: 맛집 관리 시스템 (등록, 수정, 삭제, CSV 업로드)
- **4주**: 회원 관리 및 리뷰 관리 시스템
- **5주**: 통계 및 차트, 시스템 설정 기능

### Phase 4 (2주) - 고도화
- **1주**: 랜덤 추천 알고리즘 및 고급 필터
- **2주**: 성능 최적화 및 UI/UX 개선

### Phase 5 (1주) - 테스트 및 배포
- QA 테스트 및 버그 수정
- 앱스토어 배포 준비

**총 개발 기간: 18주 (약 4.5개월)**

### 관리자 웹 상세 개발 일정

#### 1주차: 기초 설정
- React 프로젝트 설정 (Create React App)
- Material-UI
- React Router 라우팅 설정
- Axios HTTP 통신 설정
- 관리자 로그인 페이지 개발
- JWT 토큰 인증 시스템
- 상태 관리는 Recoil State

#### 2주차: 대시보드 개발
- 대시보드 레이아웃 및 네비게이션
- 실시간 통계 카드 개발
- Chart.js로 사용자 증가 차트
- 인기 맛집 TOP 10 리스트
- 오늘의 통계 요약 카드

#### 3주차: 맛집 관리
- 맛집 목록 페이지 (페이징, 검색, 필터)
- 맛집 등록 폼 (지도 연동 위치 선택)
- 맛집 수정 폼
- 사진 업로드 기능
- CSV 파일 대량 업로드
- 맛집 상태 관리 (활성/비활성)

#### 4주차: 회원 및 리뷰 관리
- 회원 목록 페이지
- 회원 상세 정보 모달
- 회원 상태 변경 (정지/활성화)
- 리뷰 목록 페이지
- 신고된 리뷰 관리
- 리뷰 삭제 및 숨김 처리
- 사용자/리뷰 신고 처리 시스템

#### 5주차: 통계 및 시스템 설정
- 상세 통계 페이지 (연령대별, 지역별 분석)
- 리뷰 통계 및 평점 분포 차트
- 음식 카테고리 관리
- 공지사항 작성 및 관리
- 푸시 알림 발송 기능
- 앱 설정 관리 (버전, 강제 업데이트 등)
- 관리자 계정 관리

---

## 9. 수익 모델

### 9.1 광고 수익
- **배너 광고**: 맛집 리스트 하단 배너
- **네이티브 광고**: 맛집 리스트 중간 삽입형 광고
- **스폰서 맛집**: 상위 노출 유료 광고

### 9.2 프리미엄 구독
- **광고 제거**: 월 2,900원 구독
- **무제한 찜하기**: 기본 50개 → 무제한
- **고급 필터**: 더 세밀한 검색 옵션

### 9.3 제휴 수익
- **배달 앱 연동**: 배달의민족, 요기요 연결 수수료
- **예약 시스템**: 테이블 예약 중개 수수료
- **맛집 홍보**: 맛집 사장님 대상 마케팅 서비스

---

## 10. 마케팅 전략

### 10.1 런칭 전략
- **베타 테스터 모집**: 대학가, 직장가 중심 100명
- **인플루언서 협업**: 맛집 유튜버, 인스타그래머 10명
- **지역 커뮤니티**: 동네 맛집 카페, 블로그 활용

### 10.2 성장 전략
- **리워드 시스템**: 리뷰 작성 시 포인트 적립
- **추천인 제도**: 친구 추천 시 혜택 제공
- **이벤트**: 맛집 탐방 챌린지, 사진 콘테스트
- **SEO**: 맛집 관련 키워드 검색 최적화

---

## 11. 향후 확장 계획

### 11.1 기능 확장
- **AI 추천**: 개인 취향 학습 기반 맞춤 추천
- **그룹 기능**: 친구들과 함께 맛집 선택 투표
- **배달 통합**: 배달 주문 기능 추가
- **예약 시스템**: 실시간 테이블 예약

### 11.2 시장 확장
- **지역 확장**: 수도권 → 전국 → 해외
- **B2B 서비스**: 회사 단체 급식 추천 서비스
- **관광 연계**: 관광지별 맛집 가이드 서비스

---

## 12. 리스크 및 대응방안

### 12.1 기술적 리스크
- **위치 정확도**: 실내 GPS 오차 → Wi-Fi 기반 위치 보정
- **서버 부하**: 사용자 급증 → 클라우드 오토스케일링
- **데이터 품질**: 잘못된 맛집 정보 → 사용자 신고 시스템

### 12.2 사업적 리스크
- **경쟁사 진입**: 기존 맛집 앱 → 차별화된 랜덤 추천 UX
- **초기 데이터 부족**: 맛집 정보 부족 → 크롤링 + 관리자 직접 입력
- **수익성**: 광고 수익 부족 → 다양한 수익 모델 실험

---

## 13. 성공 지표 (KPI)

### 13.1 사용자 지표
- **DAU/MAU**: 일/월 활성 사용자 수
- **리텐션**: 1주일 30%, 1개월 15% 목표
- **앱 스토어 평점**: 4.0점 이상 유지
- **다운로드 수**: 1년 내 100만 다운로드

### 13.2 서비스 지표
- **리뷰 작성률**: 회원 대비 10% 이상
- **맛집 방문 전환율**: 조회 → 실제 방문 5% 이상
- **랜덤 추천 사용률**: 전체 검색 중 30% 이상
- **프리미엄 전환율**: 전체 회원 중 5% 이상

### 13.3 수익 지표
- **월 광고 수익**: 6개월 후 월 1,000만원 목표
- **프리미엄 구독**: 1년 후 1만명 목표
- **제휴 수익**: 배달 앱 연동 후 월 500만원 목표

---

## 14. 개발팀 구성

### 14.1 필요 인력
- **프로젝트 매니저**: 1명
- **백엔드 개발자**: 2명 (Spring Boot, PostgreSQL)
- **모바일 개발자**: 2명 (Flutter)
- **프론트엔드 개발자**: 1명 (React - 관리자 웹)
- **UI/UX 디자이너**: 1명
- **QA 테스터**: 1명

**총 인원: 8명**

### 역할별 상세 업무

#### 프론트엔드 개발자 (React)
- 관리자 웹 전체 개발
- React + Material-UI
- 대시보드 및 통계 차트
- 맛집/회원/리뷰 관리 시스템
- 백엔드 API 연동 및 인증 처리

#### 모바일 개발자 (Flutter)
- iOS/Android 앱 개발
- 위치 기반 서비스 구현
- 소셜 로그인 연동
- 지도 API 연동
- 앱 스토어 배포

#### 백엔드 개발자
- Spring Boot API 개발
- 데이터베이스 설계 및 최적화
- 인증 및 보안 시스템
- 외부 API 연동
- 서버 배포 및 운영

### 14.2 외주 고려사항
- **지도 API 연동**: 전문 업체 협력
- **디자인**: 외부 디자인 스튜디오
- **마케팅**: 디지털 마케팅 에이전시

---

* "오늘 뭐 먹을까?" 고민은 이제 그만! 아무거나 요정이 해결해드릴게요! 🧚‍♀️✨*