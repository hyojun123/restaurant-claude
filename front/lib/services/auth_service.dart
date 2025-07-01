import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import '../models/user.dart';
import '../config/constants.dart';

class AuthService {
  static const String baseUrl = ApiConstants.authBaseUrl;
  
  static String? _token;
  static User? _currentUser;

  static String? get token => _token;
  static User? get currentUser => _currentUser;
  static bool get isLoggedIn => _token != null;

  static Future<void> loadToken() async {
    final prefs = await SharedPreferences.getInstance();
    _token = prefs.getString(ApiConstants.tokenKey);
    // 토큰 로드 시에는 getCurrentUser를 자동 호출하지 않음
    // 필요한 경우에만 명시적으로 호출하도록 변경
  }

  static Future<Map<String, dynamic>> login(String email, String password) async {
    try {
      print('Attempting login to: $baseUrl/signin');
      final response = await http.post(
        Uri.parse('$baseUrl/signin'),
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
        },
        body: json.encode({
          'email': email,
          'password': password,
        }),
      ).timeout(ApiConstants.requestTimeout);

      print('Login response status: ${response.statusCode}');
      print('Login response body: ${response.body}');

      final data = json.decode(response.body);

      if (response.statusCode == 200) {
        _token = data['accessToken'];
        _currentUser = User.fromJson(data['user']);
        
        // 토큰 저장
        final prefs = await SharedPreferences.getInstance();
        await prefs.setString(ApiConstants.tokenKey, _token!);
        
        return {'success': true, 'user': _currentUser};
      } else {
        return {'success': false, 'message': data['message'] ?? '로그인에 실패했습니다.'};
      }
    } catch (e) {
      print('Login error: $e');
      return {'success': false, 'message': '네트워크 오류가 발생했습니다: $e'};
    }
  }

  static Future<Map<String, dynamic>> signup(
    String email, 
    String password, 
    String nickname
  ) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/signup'),
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
        },
        body: json.encode({
          'email': email,
          'password': password,
          'nickname': nickname,
        }),
      );

      final data = json.decode(response.body);

      if (response.statusCode == 200) {
        _token = data['accessToken'];
        _currentUser = User.fromJson(data['user']);
        
        // 토큰 저장
        final prefs = await SharedPreferences.getInstance();
        await prefs.setString(ApiConstants.tokenKey, _token!);
        
        return {'success': true, 'user': _currentUser};
      } else {
        return {'success': false, 'message': data['message'] ?? '회원가입에 실패했습니다.'};
      }
    } catch (e) {
      print('Signup error: $e');
      return {'success': false, 'message': '네트워크 오류가 발생했습니다: $e'};
    }
  }

  static Future<void> getCurrentUser() async {
    if (_token == null) return;

    try {
      final response = await http.get(
        Uri.parse('$baseUrl/me'),
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
          'Authorization': 'Bearer $_token',
        },
      );

      if (response.statusCode == 200) {
        final data = json.decode(response.body);
        _currentUser = User.fromJson(data);
      } else {
        await logout();
      }
    } catch (e) {
      // 네트워크 오류 등은 무시
    }
  }

  static Future<void> logout() async {
    _token = null;
    _currentUser = null;
    
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove(ApiConstants.tokenKey);
  }

  static Map<String, String> getAuthHeaders() {
    return {
      'Content-Type': 'application/json',
      if (_token != null) 'Authorization': 'Bearer $_token',
    };
  }
}