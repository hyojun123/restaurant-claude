import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/restaurant.dart';
import 'auth_service.dart';

class FavoriteService {
  static const String baseUrl = 'http://localhost:8080/api/favorites';

  static Future<Map<String, dynamic>> toggleFavorite(int restaurantId) async {
    if (!AuthService.isLoggedIn) {
      throw Exception('로그인이 필요합니다.');
    }

    try {
      final response = await http.post(
        Uri.parse('$baseUrl/toggle/$restaurantId'),
        headers: AuthService.getAuthHeaders(),
      );

      if (response.statusCode == 200) {
        final data = json.decode(response.body);
        return {
          'success': true,
          'isFavorited': data['isFavorited'],
          'message': data['message'],
        };
      } else {
        final error = json.decode(response.body);
        return {
          'success': false,
          'message': error['message'] ?? '찜하기에 실패했습니다.',
        };
      }
    } catch (e) {
      return {
        'success': false,
        'message': '네트워크 오류가 발생했습니다.',
      };
    }
  }

  static Future<bool> checkFavoriteStatus(int restaurantId) async {
    if (!AuthService.isLoggedIn) {
      return false;
    }

    try {
      final response = await http.get(
        Uri.parse('$baseUrl/check/$restaurantId'),
        headers: AuthService.getAuthHeaders(),
      );

      if (response.statusCode == 200) {
        final data = json.decode(response.body);
        return data['isFavorited'] ?? false;
      }
      return false;
    } catch (e) {
      return false;
    }
  }

  static Future<List<Restaurant>> getMyFavorites({int page = 0, int size = 10}) async {
    if (!AuthService.isLoggedIn) {
      throw Exception('로그인이 필요합니다.');
    }

    try {
      final response = await http.get(
        Uri.parse('$baseUrl/my?page=$page&size=$size'),
        headers: AuthService.getAuthHeaders(),
      );

      if (response.statusCode == 200) {
        final data = json.decode(response.body);
        final List<dynamic> content = data['content'] ?? [];
        return content.map((json) => Restaurant.fromJson(json)).toList();
      } else {
        throw Exception('찜한 맛집을 불러오는데 실패했습니다.');
      }
    } catch (e) {
      // 오프라인 모드시 빈 리스트 반환
      return [];
    }
  }

  static Future<void> removeFavorite(int restaurantId) async {
    if (!AuthService.isLoggedIn) {
      throw Exception('로그인이 필요합니다.');
    }

    try {
      final response = await http.delete(
        Uri.parse('$baseUrl/$restaurantId'),
        headers: AuthService.getAuthHeaders(),
      );

      if (response.statusCode != 200) {
        final error = json.decode(response.body);
        throw Exception(error['message'] ?? '찜 삭제에 실패했습니다.');
      }
    } catch (e) {
      throw Exception(e.toString());
    }
  }

  static Future<int> getFavoriteCount() async {
    if (!AuthService.isLoggedIn) {
      return 0;
    }

    try {
      final response = await http.get(
        Uri.parse('$baseUrl/count'),
        headers: AuthService.getAuthHeaders(),
      );

      if (response.statusCode == 200) {
        final data = json.decode(response.body);
        return data['count'] ?? 0;
      }
      return 0;
    } catch (e) {
      return 0;
    }
  }
}