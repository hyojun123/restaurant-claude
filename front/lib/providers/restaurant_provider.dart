import 'package:flutter/material.dart';
import '../models/restaurant.dart';
import '../services/api_service.dart';

class RestaurantProvider extends ChangeNotifier {
  List<Restaurant> _restaurants = [];
  List<String> _categories = [];
  bool _isLoading = false;
  String? _error;

  List<Restaurant> get restaurants => _restaurants;
  List<String> get categories => _categories;
  bool get isLoading => _isLoading;
  String? get error => _error;

  final ApiService _apiService = ApiService();

  Future<void> loadCategories() async {
    try {
      _categories = await _apiService.getCategories();
      notifyListeners();
    } catch (e) {
      _error = e.toString();
      notifyListeners();
    }
  }

  Future<void> loadRestaurants({
    double? latitude,
    double? longitude,
    double radius = 2.0,
    String? category,
  }) async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      _restaurants = await _apiService.getRestaurants(
        latitude: latitude,
        longitude: longitude,
        radius: radius,
        category: category,
      );
    } catch (e) {
      _error = e.toString();
      _restaurants = [];
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<Restaurant?> getRandomRestaurant({String? category}) async {
    try {
      return await _apiService.getRandomRestaurant(category: category);
    } catch (e) {
      _error = e.toString();
      notifyListeners();
      return null;
    }
  }

  void clearError() {
    _error = null;
    notifyListeners();
  }
}