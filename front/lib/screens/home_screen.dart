import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:flutter/foundation.dart';
import '../providers/location_provider.dart';
import '../providers/restaurant_provider.dart';
import '../providers/auth_provider.dart';
import '../widgets/category_grid.dart';
import '../widgets/restaurant_list.dart';
import '../widgets/naver_map_widget.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      _initializeData();
    });
  }

  Future<void> _initializeData() async {
    final locationProvider = Provider.of<LocationProvider>(context, listen: false);
    final restaurantProvider = Provider.of<RestaurantProvider>(context, listen: false);

    // 위치 정보 가져오기
    await locationProvider.getCurrentLocation();
    
    // 카테고리 로드
    await restaurantProvider.loadCategories();
    
    // 근처 맛집 로드
    if (locationProvider.currentPosition != null) {
      await restaurantProvider.loadRestaurants(
        latitude: locationProvider.currentPosition!.latitude,
        longitude: locationProvider.currentPosition!.longitude,
      );
    }
  }

  Future<void> _onRandomRecommendation() async {
    final restaurantProvider = Provider.of<RestaurantProvider>(context, listen: false);
    
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (context) => const Center(
        child: CircularProgressIndicator(),
      ),
    );

    final randomRestaurant = await restaurantProvider.getRandomRestaurant();
    
    Navigator.pop(context); // 로딩 다이얼로그 닫기
    
    if (randomRestaurant != null) {
      Navigator.pushNamed(
        context,
        '/restaurant-detail',
        arguments: randomRestaurant,
      );
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('랜덤 추천을 가져올 수 없습니다.')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('🧚‍♀️ 아무거나 요정'),
        centerTitle: true,
        actions: [
          Consumer<AuthProvider>(
            builder: (context, authProvider, child) {
              if (authProvider.isLoggedIn) {
                return Row(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    IconButton(
                      icon: const Icon(Icons.favorite),
                      onPressed: () => Navigator.pushNamed(context, '/favorites'),
                    ),
                    IconButton(
                      icon: const Icon(Icons.person),
                      onPressed: () => Navigator.pushNamed(context, '/profile'),
                    ),
                  ],
                );
              } else {
                return TextButton(
                  onPressed: () => Navigator.pushNamed(context, '/login'),
                  child: const Text(
                    '로그인',
                    style: TextStyle(color: Colors.white),
                  ),
                );
              }
            },
          ),
        ],
      ),
      body: RefreshIndicator(
        onRefresh: _initializeData,
        child: SingleChildScrollView(
          physics: const AlwaysScrollableScrollPhysics(),
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // 현재 위치 표시
              Consumer<LocationProvider>(
                builder: (context, locationProvider, child) {
                  return Card(
                    child: ListTile(
                      leading: const Icon(Icons.location_on, color: Colors.orange),
                      title: Text(
                        locationProvider.currentAddress.isEmpty
                            ? '위치를 가져오는 중...'
                            : locationProvider.currentAddress,
                      ),
                      subtitle: locationProvider.error != null
                          ? Text(
                              locationProvider.error!,
                              style: const TextStyle(color: Colors.red),
                            )
                          : null,
                      trailing: locationProvider.isLoading
                          ? const SizedBox(
                              width: 20,
                              height: 20,
                              child: CircularProgressIndicator(strokeWidth: 2),
                            )
                          : IconButton(
                              icon: const Icon(Icons.refresh),
                              onPressed: () => locationProvider.getCurrentLocation(),
                            ),
                    ),
                  );
                },
              ),
              
              const SizedBox(height: 20),
              
              // 아무거나 추천받기 버튼
              SizedBox(
                width: double.infinity,
                height: 60,
                child: ElevatedButton.icon(
                  onPressed: _onRandomRecommendation,
                  icon: const Icon(Icons.casino, size: 28),
                  label: const Text(
                    '🎲 아무거나 추천받기',
                    style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                  ),
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.orange,
                    foregroundColor: Colors.white,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(12),
                    ),
                  ),
                ),
              ),
              
              const SizedBox(height: 20),
              
              // Naver 지도 섹션
              Consumer2<LocationProvider, RestaurantProvider>(
                builder: (context, locationProvider, restaurantProvider, child) {
                  return Container(
                    height: 300,
                    margin: const EdgeInsets.symmetric(horizontal: 0),
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(12),
                      boxShadow: [
                        BoxShadow(
                          color: Colors.grey.withOpacity(0.3),
                          spreadRadius: 2,
                          blurRadius: 5,
                          offset: const Offset(0, 3),
                        ),
                      ],
                    ),
                    child: ClipRRect(
                      borderRadius: BorderRadius.circular(12),
                      child: NaverMapWidget(
                        currentPosition: locationProvider.currentPosition,
                        restaurants: restaurantProvider.restaurants,
                      ),
                    ),
                  );
                },
              ),
              
              const SizedBox(height: 30),
              
              // 음식 카테고리 섹션
              const Text(
                '음식 카테고리',
                style: TextStyle(
                  fontSize: 20,
                  fontWeight: FontWeight.bold,
                ),
              ),
              const SizedBox(height: 16),
              const CategoryGrid(),
              
              const SizedBox(height: 30),
              
              // 근처 맛집 섹션
              const Text(
                '근처 맛집',
                style: TextStyle(
                  fontSize: 20,
                  fontWeight: FontWeight.bold,
                ),
              ),
              const SizedBox(height: 16),
              const RestaurantList(),
            ],
          ),
        ),
      ),
    );
  }
}