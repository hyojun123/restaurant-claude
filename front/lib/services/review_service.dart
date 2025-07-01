import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/review.dart';
import 'auth_service.dart';

class ReviewService {
  static const String baseUrl = 'http://localhost:8080/api/reviews';

  static Future<List<Review>> getReviewsByRestaurant(int restaurantId, {int page = 0, int size = 10}) async {
    try {
      final response = await http.get(
        Uri.parse('$baseUrl/restaurant/$restaurantId?page=$page&size=$size'),
        headers: AuthService.getAuthHeaders(),
      );

      if (response.statusCode == 200) {
        final data = json.decode(response.body);
        final List<dynamic> content = data['content'] ?? [];
        return content.map((json) => Review.fromJson(json)).toList();
      } else {
        throw Exception('리뷰를 불러오는데 실패했습니다.');
      }
    } catch (e) {
      // 오프라인 모드 또는 에러시 더미 데이터 반환
      return _getDummyReviews(restaurantId);
    }
  }

  static Future<List<Review>> getMyReviews({int page = 0, int size = 10}) async {
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
        return content.map((json) => Review.fromJson(json)).toList();
      } else {
        throw Exception('내 리뷰를 불러오는데 실패했습니다.');
      }
    } catch (e) {
      return [];
    }
  }

  static Future<Review> createReview({
    required int restaurantId,
    required int rating,
    String? content,
    List<String>? photos,
    String? tags,
  }) async {
    if (!AuthService.isLoggedIn) {
      throw Exception('로그인이 필요합니다.');
    }

    try {
      final response = await http.post(
        Uri.parse(baseUrl),
        headers: AuthService.getAuthHeaders(),
        body: json.encode({
          'restaurantId': restaurantId,
          'rating': rating,
          'content': content,
          'photos': photos,
          'tags': tags,
        }),
      );

      if (response.statusCode == 200) {
        final data = json.decode(response.body);
        return Review.fromJson(data);
      } else {
        final error = json.decode(response.body);
        throw Exception(error['message'] ?? '리뷰 작성에 실패했습니다.');
      }
    } catch (e) {
      throw Exception(e.toString());
    }
  }

  static Future<Review> updateReview({
    required int reviewId,
    required int rating,
    String? content,
    List<String>? photos,
    String? tags,
  }) async {
    if (!AuthService.isLoggedIn) {
      throw Exception('로그인이 필요합니다.');
    }

    try {
      final response = await http.put(
        Uri.parse('$baseUrl/$reviewId'),
        headers: AuthService.getAuthHeaders(),
        body: json.encode({
          'rating': rating,
          'content': content,
          'photos': photos,
          'tags': tags,
        }),
      );

      if (response.statusCode == 200) {
        final data = json.decode(response.body);
        return Review.fromJson(data);
      } else {
        final error = json.decode(response.body);
        throw Exception(error['message'] ?? '리뷰 수정에 실패했습니다.');
      }
    } catch (e) {
      throw Exception(e.toString());
    }
  }

  static Future<void> deleteReview(int reviewId) async {
    if (!AuthService.isLoggedIn) {
      throw Exception('로그인이 필요합니다.');
    }

    try {
      final response = await http.delete(
        Uri.parse('$baseUrl/$reviewId'),
        headers: AuthService.getAuthHeaders(),
      );

      if (response.statusCode != 200) {
        final error = json.decode(response.body);
        throw Exception(error['message'] ?? '리뷰 삭제에 실패했습니다.');
      }
    } catch (e) {
      throw Exception(e.toString());
    }
  }

  static Future<int> getMyReviewCount() async {
    if (!AuthService.isLoggedIn) {
      return 0;
    }

    try {
      final response = await http.get(
        Uri.parse('$baseUrl/my/count'),
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

  static List<Review> _getDummyReviews(int restaurantId) {
    final List<Map<String, dynamic>> dummyData = [
      {
        'reviewId': 1,
        'userId': 1,
        'userNickname': '김맛집러버',
        'userProfileImage': null,
        'restaurantId': restaurantId,
        'restaurantName': '할머니 손맛 한정식',
        'rating': 5,
        'content': '정말 맛있어요! 할머니가 직접 만드신 것 같은 집밥 느낌이 최고입니다.',
        'photos': [],
        'tags': '#맛있어요 #집밥 #재방문의사',
        'likeCount': 12,
        'createdAt': DateTime.now().subtract(const Duration(days: 2)).toIso8601String(),
        'updatedAt': DateTime.now().subtract(const Duration(days: 2)).toIso8601String(),
      },
      {
        'reviewId': 2,
        'userId': 2,
        'userNickname': '박미식가',
        'userProfileImage': null,
        'restaurantId': restaurantId,
        'restaurantName': '할머니 손맛 한정식',
        'rating': 4,
        'content': '분위기도 좋고 음식도 맛있었습니다. 다만 조금 짜더라구요.',
        'photos': [],
        'tags': '#분위기좋음 #약간짠맛',
        'likeCount': 8,
        'createdAt': DateTime.now().subtract(const Duration(days: 5)).toIso8601String(),
        'updatedAt': DateTime.now().subtract(const Duration(days: 5)).toIso8601String(),
      },
    ];

    return dummyData.map((data) => Review.fromJson(data)).toList();
  }
}