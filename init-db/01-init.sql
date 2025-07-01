-- 데이터베이스 초기화 스크립트
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 인덱스 생성 (위치 기반 검색 최적화)
CREATE INDEX IF NOT EXISTS idx_restaurants_location ON restaurants(latitude, longitude);
CREATE INDEX IF NOT EXISTS idx_restaurants_category ON restaurants(category);
CREATE INDEX IF NOT EXISTS idx_restaurants_active ON restaurants(is_active);
CREATE INDEX IF NOT EXISTS idx_restaurants_rating ON restaurants(average_rating);

-- 관리자 계정 생성 (기본 패스워드: admin123)
INSERT INTO admins (username, password, name, role, is_active) VALUES 
('admin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', '시스템 관리자', 'SUPER_ADMIN', true)
ON CONFLICT (username) DO NOTHING;