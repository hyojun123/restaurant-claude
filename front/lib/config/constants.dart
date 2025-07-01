class ApiConstants {
  // 개발 환경에 따라 적절한 URL 선택
  static const String _emulatorBaseUrl = 'http://10.0.2.2:8080/api';
  static const String _localhostBaseUrl = 'http://localhost:8080/api';
  
  // 실제 PC의 IP 주소로 변경 필요 (ipconfig 명령어로 확인)
  static const String _deviceBaseUrl = 'http://192.168.1.100:8080/api';
  
  // 현재 사용할 기본 URL (에뮬레이터용)
  static const String baseUrl = _emulatorBaseUrl;
  
  // 인증 관련 엔드포인트
  static const String authBaseUrl = '$baseUrl/auth';
  
  // 기타 상수들
  static const Duration requestTimeout = Duration(seconds: 10);
  static const String tokenKey = 'auth_token';
}