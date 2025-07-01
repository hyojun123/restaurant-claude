-- 성능 최적화를 위한 데이터베이스 인덱스 생성

-- 레스토랑 테이블 인덱스
CREATE INDEX IF NOT EXISTS idx_restaurants_category ON restaurants(category);
CREATE INDEX IF NOT EXISTS idx_restaurants_is_active ON restaurants(is_active);
CREATE INDEX IF NOT EXISTS idx_restaurants_category_active ON restaurants(category, is_active);
CREATE INDEX IF NOT EXISTS idx_restaurants_rating ON restaurants(average_rating DESC);
CREATE INDEX IF NOT EXISTS idx_restaurants_review_count ON restaurants(review_count DESC);
CREATE INDEX IF NOT EXISTS idx_restaurants_price_range ON restaurants(price_range);
CREATE INDEX IF NOT EXISTS idx_restaurants_parking ON restaurants(parking_available);
CREATE INDEX IF NOT EXISTS idx_restaurants_location ON restaurants(latitude, longitude);

-- 복합 인덱스 (위치 기반 검색용)
CREATE INDEX IF NOT EXISTS idx_restaurants_location_category ON restaurants(latitude, longitude, category, is_active);
CREATE INDEX IF NOT EXISTS idx_restaurants_location_active ON restaurants(latitude, longitude, is_active);

-- 사용자 테이블 인덱스
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_social ON users(social_type, social_id);
CREATE INDEX IF NOT EXISTS idx_users_is_active ON users(is_active);
CREATE INDEX IF NOT EXISTS idx_users_created_at ON users(created_at);

-- 리뷰 테이블 인덱스
CREATE INDEX IF NOT EXISTS idx_reviews_restaurant_id ON reviews(restaurant_id);
CREATE INDEX IF NOT EXISTS idx_reviews_user_id ON reviews(user_id);
CREATE INDEX IF NOT EXISTS idx_reviews_is_active ON reviews(is_active);
CREATE INDEX IF NOT EXISTS idx_reviews_rating ON reviews(rating);
CREATE INDEX IF NOT EXISTS idx_reviews_created_at ON reviews(created_at);
CREATE INDEX IF NOT EXISTS idx_reviews_restaurant_active ON reviews(restaurant_id, is_active);
CREATE INDEX IF NOT EXISTS idx_reviews_user_active ON reviews(user_id, is_active);

-- 즐겨찾기 테이블 인덱스
CREATE INDEX IF NOT EXISTS idx_favorites_user_id ON favorites(user_id);
CREATE INDEX IF NOT EXISTS idx_favorites_restaurant_id ON favorites(restaurant_id);
CREATE INDEX IF NOT EXISTS idx_favorites_user_restaurant ON favorites(user_id, restaurant_id);

-- 관리자 테이블 인덱스
CREATE INDEX IF NOT EXISTS idx_admins_username ON admins(username);
CREATE INDEX IF NOT EXISTS idx_admins_is_active ON admins(is_active);

-- 통계 조회를 위한 복합 인덱스
CREATE INDEX IF NOT EXISTS idx_users_created_at_active ON users(created_at, is_active);
CREATE INDEX IF NOT EXISTS idx_reviews_created_at_active ON reviews(created_at, is_active);

-- 전체 텍스트 검색을 위한 인덱스 (PostgreSQL)
CREATE INDEX IF NOT EXISTS idx_restaurants_name_gin ON restaurants USING gin(to_tsvector('korean', name));
CREATE INDEX IF NOT EXISTS idx_restaurants_address_gin ON restaurants USING gin(to_tsvector('korean', address));