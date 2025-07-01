import 'package:flutter/material.dart';
import '../models/user.dart';
import '../services/auth_service.dart';

class AuthProvider extends ChangeNotifier {
  User? _user;
  bool _isLoading = false;
  String? _error;
  bool _initialized = false;

  User? get user => _user;
  bool get isLoggedIn => _user != null;
  bool get isLoading => _isLoading;
  String? get error => _error;

  Future<void> loadUser() async {
    if (_initialized) return; // 이미 초기화된 경우 재실행 방지
    
    _initialized = true;
    await AuthService.loadToken();
    _user = AuthService.currentUser;
    
    // 토큰은 있지만 사용자 정보가 없는 경우에만 getCurrentUser 호출
    if (AuthService.token != null && _user == null) {
      await AuthService.getCurrentUser();
      _user = AuthService.currentUser;
    }
    
    notifyListeners();
  }

  Future<bool> login(String email, String password) async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      final result = await AuthService.login(email, password);
      
      if (result['success']) {
        _user = result['user'];
        _initialized = true; // 로그인/회원가입 성공 시 초기화 상태 설정
        notifyListeners();
        return true;
      } else {
        _error = result['message'];
        notifyListeners();
        return false;
      }
    } catch (e) {
      _error = '로그인 중 오류가 발생했습니다.';
      notifyListeners();
      return false;
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<bool> signup(String email, String password, String nickname) async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      final result = await AuthService.signup(email, password, nickname);
      
      if (result['success']) {
        _user = result['user'];
        _initialized = true; // 로그인/회원가입 성공 시 초기화 상태 설정
        notifyListeners();
        return true;
      } else {
        _error = result['message'];
        notifyListeners();
        return false;
      }
    } catch (e) {
      _error = '회원가입 중 오류가 발생했습니다.';
      notifyListeners();
      return false;
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> logout() async {
    await AuthService.logout();
    _user = null;
    _error = null;
    _initialized = false; // 초기화 상태 리셋
    notifyListeners();
  }

  void clearError() {
    _error = null;
    notifyListeners();
  }
}