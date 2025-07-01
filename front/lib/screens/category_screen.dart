import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/location_provider.dart';
import '../providers/restaurant_provider.dart';
import '../widgets/restaurant_list.dart';

class CategoryScreen extends StatefulWidget {
  const CategoryScreen({super.key});

  @override
  State<CategoryScreen> createState() => _CategoryScreenState();
}

class _CategoryScreenState extends State<CategoryScreen> {
  String? selectedCategory;
  double selectedRadius = 2.0;

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    
    // 전달받은 카테고리가 있으면 설정
    final category = ModalRoute.of(context)?.settings.arguments as String?;
    if (category != null && selectedCategory != category) {
      selectedCategory = category;
      _loadRestaurants();
    }
  }

  Future<void> _loadRestaurants() async {
    final locationProvider = Provider.of<LocationProvider>(context, listen: false);
    final restaurantProvider = Provider.of<RestaurantProvider>(context, listen: false);

    if (locationProvider.currentPosition != null) {
      await restaurantProvider.loadRestaurants(
        latitude: locationProvider.currentPosition!.latitude,
        longitude: locationProvider.currentPosition!.longitude,
        radius: selectedRadius,
        category: selectedCategory,
      );
    }
  }

  Future<void> _onRandomCategoryRecommendation() async {
    final restaurantProvider = Provider.of<RestaurantProvider>(context, listen: false);
    
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (context) => const Center(
        child: CircularProgressIndicator(),
      ),
    );

    final randomRestaurant = await restaurantProvider.getRandomRestaurant(
      category: selectedCategory,
    );
    
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
        title: Text(selectedCategory != null ? '$selectedCategory 맛집' : '카테고리별 맛집'),
        actions: [
          if (selectedCategory != null)
            IconButton(
              icon: const Icon(Icons.casino),
              onPressed: _onRandomCategoryRecommendation,
              tooltip: '이 카테고리에서 랜덤 추천',
            ),
        ],
      ),
      body: Column(
        children: [
          // 필터 섹션
          Container(
            padding: const EdgeInsets.all(16.0),
            child: Row(
              children: [
                Expanded(
                  child: Consumer<RestaurantProvider>(
                    builder: (context, provider, child) {
                      return DropdownButtonFormField<String>(
                        value: selectedCategory,
                        decoration: const InputDecoration(
                          labelText: '카테고리',
                          border: OutlineInputBorder(),
                        ),
                        items: provider.categories.map((category) {
                          return DropdownMenuItem(
                            value: category,
                            child: Text(category),
                          );
                        }).toList(),
                        onChanged: (value) {
                          setState(() {
                            selectedCategory = value;
                          });
                          _loadRestaurants();
                        },
                      );
                    },
                  ),
                ),
                const SizedBox(width: 16),
                Expanded(
                  child: DropdownButtonFormField<double>(
                    value: selectedRadius,
                    decoration: const InputDecoration(
                      labelText: '반경',
                      border: OutlineInputBorder(),
                    ),
                    items: const [
                      DropdownMenuItem(value: 0.5, child: Text('500m')),
                      DropdownMenuItem(value: 1.0, child: Text('1km')),
                      DropdownMenuItem(value: 2.0, child: Text('2km')),
                      DropdownMenuItem(value: 5.0, child: Text('5km')),
                    ],
                    onChanged: (value) {
                      setState(() {
                        selectedRadius = value ?? 2.0;
                      });
                      _loadRestaurants();
                    },
                  ),
                ),
              ],
            ),
          ),
          
          // 맛집 리스트
          const Expanded(
            child: RestaurantList(),
          ),
        ],
      ),
    );
  }
}