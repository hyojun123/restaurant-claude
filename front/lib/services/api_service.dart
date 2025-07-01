import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/restaurant.dart';

class ApiService {
  // 개발용: localhost 대신 실제 IP 주소 사용 (PC의 IP 주소로 변경 필요)
  // 안드로이드 에뮬레이터의 경우 10.0.2.2 사용
  static const String baseUrl = 'http://10.0.2.2:8080/api';

  Future<List<Restaurant>> getRestaurants({
    double? latitude,
    double? longitude,
    double radius = 2.0,
    String? category,
  }) async {
    try {
      String url = '$baseUrl/restaurants?';
      
      if (latitude != null && longitude != null) {
        url += 'latitude=$latitude&longitude=$longitude&radius=$radius';
      }
      
      if (category != null && category.isNotEmpty) {
        url += '&category=$category';
      }

      final response = await http.get(Uri.parse(url));
      
      if (response.statusCode == 200) {
        final List<dynamic> data = json.decode(response.body);
        return data.map((json) => Restaurant.fromJson(json)).toList();
      } else {
        throw Exception('맛집 정보를 불러오는데 실패했습니다.');
      }
    } catch (e) {
      // 오프라인 모드 또는 서버 연결 실패시 더미 데이터 반환
      return _getDummyRestaurants(category: category);
    }
  }

  Future<List<String>> getCategories() async {
    try {
      final response = await http.get(Uri.parse('$baseUrl/restaurants/categories'));
      
      if (response.statusCode == 200) {
        final List<dynamic> data = json.decode(response.body);
        return data.cast<String>();
      } else {
        throw Exception('카테고리 정보를 불러오는데 실패했습니다.');
      }
    } catch (e) {
      // 오프라인 모드시 더미 카테고리 반환
      return ['한식', '일식', '중식', '양식', '카페', '술집'];
    }
  }

  Future<Restaurant?> getRandomRestaurant({String? category}) async {
    try {
      String url = '$baseUrl/restaurants/random';
      if (category != null && category.isNotEmpty) {
        url += '?category=$category';
      }

      final response = await http.get(Uri.parse(url));
      
      if (response.statusCode == 200) {
        final data = json.decode(response.body);
        return Restaurant.fromJson(data);
      } else {
        throw Exception('랜덤 맛집을 불러오는데 실패했습니다.');
      }
    } catch (e) {
      // 오프라인 모드시 더미 데이터에서 랜덤 선택
      final restaurants = _getDummyRestaurants(category: category);
      if (restaurants.isNotEmpty) {
        restaurants.shuffle();
        return restaurants.first;
      }
      return null;
    }
  }

  List<Restaurant> _getDummyRestaurants({String? category}) {
    final List<Map<String, dynamic>> dummyData = [
      {
        'restaurantId': 1,
        'name': '할머니 손맛 한정식',
        'category': '한식',
        'address': '서울 강남구 역삼동 123-45',
        'latitude': 37.5012345,
        'longitude': 127.0345678,
        'phone': '02-1234-5678',
        'businessHours': '11:00-21:00',
        'priceRange': '보통',
        'parkingAvailable': true,
        'averageRating': 4.5,
        'reviewCount': 45,
        'distanceKm': 0.5,
      },
      {
        'restaurantId': 2,
        'name': '스시 마스터',
        'category': '일식',
        'address': '서울 강남구 신사동 234-56',
        'latitude': 37.5212345,
        'longitude': 127.0245678,
        'phone': '02-4567-8901',
        'businessHours': '18:00-23:00',
        'priceRange': '비싼',
        'parkingAvailable': false,
        'averageRating': 4.6,
        'reviewCount': 89,
        'distanceKm': 0.8,
      },
      {
        'restaurantId': 3,
        'name': '황금 짜장면',
        'category': '중식',
        'address': '서울 중구 명동 345-67',
        'latitude': 37.5632345,
        'longitude': 126.9835678,
        'phone': '02-7890-1234',
        'businessHours': '11:00-21:00',
        'priceRange': '저렴',
        'parkingAvailable': false,
        'averageRating': 4.0,
        'reviewCount': 76,
        'distanceKm': 1.2,
      },
      {
        'restaurantId': 4,
        'name': '이탈리아 파스타',
        'category': '양식',
        'address': '서울 강남구 청담동 123-45',
        'latitude': 37.5172345,
        'longitude': 127.0475678,
        'phone': '02-0123-4567',
        'businessHours': '11:30-22:00',
        'priceRange': '비싼',
        'parkingAvailable': true,
        'averageRating': 4.6,
        'reviewCount': 78,
        'distanceKm': 0.6,
      },
      {
        'restaurantId': 5,
        'name': '달콤한 카페',
        'category': '카페',
        'address': '서울 강남구 가로수길 234-56',
        'latitude': 37.5192345,
        'longitude': 127.0225678,
        'phone': '02-3456-7891',
        'businessHours': '08:00-22:00',
        'priceRange': '보통',
        'parkingAvailable': false,
        'averageRating': 4.3,
        'reviewCount': 89,
        'distanceKm': 0.7,
      },
    ];

    List<Restaurant> restaurants = dummyData
        .map((data) => Restaurant.fromJson(data))
        .toList();

    if (category != null && category.isNotEmpty) {
      restaurants = restaurants
          .where((restaurant) => restaurant.category == category)
          .toList();
    }

    return restaurants;
  }
}