# 아무거나 요정 - 뭐 먹을래? 🧚‍♀️

위치 기반 맛집 추천 모바일 애플리케이션 프로젝트

## 📋 프로젝트 구조

```
claude-test/
├── back/              # Spring Boot 백엔드 API
├── front/             # Flutter 모바일 앱
├── admin/             # React 관리자 웹
├── init-db/           # 데이터베이스 초기화 스크립트
├── docker-compose.yml # 데이터베이스 설정
└── CLAUDE.md          # 프로젝트 상세 문서
```

## 🚀 실행 방법

### 1. 데이터베이스 설정 (PostgreSQL)

```bash
# Docker로 PostgreSQL 실행
docker-compose up -d
```

### 2. 백엔드 실행 (Spring Boot)

```bash
cd back
./mvnw spring-boot:run
```

- API 문서: http://localhost:8080/swagger-ui.html
- Base URL: http://localhost:8080/api

### 3. 관리자 웹 실행 (React)

```bash
cd admin
npm install
npm start
```

- 관리자 웹: http://localhost:3000
- 로그인: admin / admin123

### 4. 모바일 앱 실행 (Flutter)

```bash
cd front
flutter pub get
flutter run
```

## 🔑 주요 기능

### Phase 3 완료 ✅
- **관리자 인증 시스템**: JWT 기반 로그인/로그아웃
- **대시보드**: 실시간 통계 및 차트
- **맛집 관리**: CRUD, 검색, 필터링, CSV 업로드
- **회원 관리**: 사용자 목록, 상태 관리, 활동 내역
- **리뷰 관리**: 리뷰 검토, 신고 처리, 통계

### Phase 4 완료 ✅
- **랜덤 추천 알고리즘**: 가중치 기반 지능형 추천
- **개인화 추천**: 사용자 취향 분석 기반 맞춤 추천
- **고급 필터**: 다중 조건 검색, 정렬, 페이징
- **성능 최적화**: 캐싱, 인덱스, JPA 최적화

## 🛠 기술 스택

### 백엔드
- Java 17 + Spring Boot 3.x
- PostgreSQL + JPA/Hibernate
- Spring Security + JWT
- Swagger/OpenAPI

### 프론트엔드
- React 18 + Material-UI
- Recoil (상태관리)
- Axios (HTTP 클라이언트)
- Chart.js (차트)

### 모바일
- Flutter 3.x
- Provider (상태관리)
- Dio (HTTP 클라이언트)

## 📊 관리자 계정

- **Username**: admin
- **Password**: admin123
- **Role**: SUPER_ADMIN

## 🗃️ 데이터베이스 스키마

주요 테이블:
- `restaurants`: 맛집 정보
- `users`: 사용자 정보
- `reviews`: 리뷰 정보
- `favorites`: 즐겨찾기
- `admins`: 관리자 계정

## 🔧 API 엔드포인트

### 공개 API
- `GET /api/restaurants` - 맛집 목록
- `GET /api/restaurants/{id}` - 맛집 상세
- `GET /api/restaurants/random` - 랜덤 추천
- `POST /api/restaurants/search` - 고급 검색

### 관리자 API
- `POST /api/admin/auth/login` - 관리자 로그인
- `GET /api/admin/dashboard/stats` - 대시보드 통계
- `GET /api/admin/restaurants` - 맛집 관리
- `GET /api/admin/users` - 회원 관리
- `GET /api/admin/reviews` - 리뷰 관리

## 📈 성능 최적화

1. **캐싱**: Spring Cache로 자주 조회되는 데이터 캐싱
2. **인덱스**: 위치 검색, 카테고리, 평점 등 최적화
3. **JPA 최적화**: Batch 처리, 쿼리 최적화
4. **로깅**: 운영 환경 로그 레벨 조정

## 🎯 추천 알고리즘

### 가중치 랜덤 추천
- 평점과 리뷰 수를 고려한 가중치 계산
- 사용자 방문 이력 필터링
- 선호 카테고리 우선 추천

### 개인화 추천
- 사용자 즐겨찾기 패턴 분석
- 카테고리 선호도 학습
- 미방문 맛집 우선 추천

## 📱 모바일 앱 주요 화면

1. **메인 화면**: 위치 기반 카테고리 선택
2. **맛집 리스트**: 거리순, 평점순 정렬
3. **맛집 상세**: 정보, 리뷰, 길찾기
4. **랜덤 추천**: "아무거나" 버튼
5. **개인화 추천**: 로그인 사용자 맞춤

## 🌐 관리자 웹 주요 기능

1. **대시보드**: 실시간 통계, 인기 맛집
2. **맛집 관리**: 등록, 수정, 삭제, 상태 관리
3. **회원 관리**: 사용자 목록, 활동 내역
4. **리뷰 관리**: 검토, 신고 처리
5. **통계**: 차트 및 분석 데이터

---

**개발 완료**: Phase 3 (관리자 웹) + Phase 4 (고도화) ✅