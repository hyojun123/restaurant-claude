import 'package:flutter/material.dart';
import 'package:url_launcher/url_launcher.dart';
import '../models/restaurant.dart';
import '../widgets/favorite_button.dart';
import 'reviews_screen.dart';
import 'review_write_screen.dart';

class RestaurantDetailScreen extends StatelessWidget {
  const RestaurantDetailScreen({super.key});

  Future<void> _launchPhone(String phone) async {
    final Uri phoneUri = Uri(scheme: 'tel', path: phone);
    if (await canLaunchUrl(phoneUri)) {
      await launchUrl(phoneUri);
    }
  }

  Future<void> _launchMap(double latitude, double longitude, String name) async {
    // 네이버 지도 앱 실행 또는 웹 페이지 열기
    final Uri naverMapUri = Uri.parse(
      'nmap://search?query=$name&lat=$latitude&lng=$longitude',
    );
    
    final Uri webMapUri = Uri.parse(
      'https://map.naver.com/v5/search/$name',
    );

    if (await canLaunchUrl(naverMapUri)) {
      await launchUrl(naverMapUri);
    } else if (await canLaunchUrl(webMapUri)) {
      await launchUrl(webMapUri);
    }
  }

  void _navigateToReviews(BuildContext context, Restaurant restaurant) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => ReviewsScreen(restaurant: restaurant),
      ),
    );
  }

  void _navigateToWriteReview(BuildContext context, Restaurant restaurant) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => ReviewWriteScreen(restaurant: restaurant),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final restaurant = ModalRoute.of(context)?.settings.arguments as Restaurant?;
    
    if (restaurant == null) {
      return Scaffold(
        appBar: AppBar(title: const Text('맛집 상세')),
        body: const Center(
          child: Text('맛집 정보를 찾을 수 없습니다.'),
        ),
      );
    }

    return Scaffold(
      appBar: AppBar(
        title: Text(restaurant.name),
        actions: [
          FavoriteButton(
            restaurant: restaurant,
            color: Colors.white,
          ),
          IconButton(
            icon: const Icon(Icons.share),
            onPressed: () {
              // 공유 기능 (추후 구현)
              ScaffoldMessenger.of(context).showSnackBar(
                const SnackBar(content: Text('공유 기능을 준비 중입니다.')),
              );
            },
          ),
        ],
      ),
      body: SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // 맛집 헤더 정보
            Container(
              width: double.infinity,
              padding: const EdgeInsets.all(20),
              decoration: const BoxDecoration(
                gradient: LinearGradient(
                  begin: Alignment.topCenter,
                  end: Alignment.bottomCenter,
                  colors: [Colors.orange, Colors.deepOrange],
                ),
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    restaurant.name,
                    style: const TextStyle(
                      fontSize: 24,
                      fontWeight: FontWeight.bold,
                      color: Colors.white,
                    ),
                  ),
                  const SizedBox(height: 8),
                  Row(
                    children: [
                      Container(
                        padding: const EdgeInsets.symmetric(
                          horizontal: 8,
                          vertical: 4,
                        ),
                        decoration: BoxDecoration(
                          color: Colors.white.withOpacity(0.2),
                          borderRadius: BorderRadius.circular(12),
                        ),
                        child: Text(
                          restaurant.category,
                          style: const TextStyle(
                            color: Colors.white,
                            fontWeight: FontWeight.w500,
                          ),
                        ),
                      ),
                      const SizedBox(width: 8),
                      Text(
                        restaurant.ratingDisplay,
                        style: const TextStyle(
                          color: Colors.white,
                          fontSize: 16,
                          fontWeight: FontWeight.w500,
                        ),
                      ),
                      const SizedBox(width: 4),
                      GestureDetector(
                        onTap: () => _navigateToReviews(context, restaurant),
                        child: Text(
                          '(${restaurant.reviewCount}개)',
                          style: const TextStyle(
                            color: Colors.white70,
                            fontSize: 14,
                            decoration: TextDecoration.underline,
                          ),
                        ),
                      ),
                      const Spacer(),
                      if (restaurant.distanceKm != null)
                        Text(
                          restaurant.formattedDistance,
                          style: const TextStyle(
                            color: Colors.white,
                            fontSize: 16,
                            fontWeight: FontWeight.w500,
                          ),
                        ),
                    ],
                  ),
                ],
              ),
            ),
            
            // 상세 정보
            Padding(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  // 주소
                  _InfoCard(
                    icon: Icons.location_on,
                    title: '주소',
                    content: restaurant.address,
                  ),
                  
                  if (restaurant.phone != null) ...[
                    const SizedBox(height: 16),
                    _InfoCard(
                      icon: Icons.phone,
                      title: '전화번호',
                      content: restaurant.phone!,
                      onTap: () => _launchPhone(restaurant.phone!),
                    ),
                  ],
                  
                  if (restaurant.businessHours != null) ...[
                    const SizedBox(height: 16),
                    _InfoCard(
                      icon: Icons.access_time,
                      title: '영업시간',
                      content: restaurant.businessHours!,
                    ),
                  ],
                  
                  const SizedBox(height: 16),
                  _InfoCard(
                    icon: Icons.attach_money,
                    title: '가격대',
                    content: '${restaurant.priceDisplay} ${restaurant.priceRange}',
                  ),
                  
                  if (restaurant.parkingAvailable != null) ...[
                    const SizedBox(height: 16),
                    _InfoCard(
                      icon: Icons.local_parking,
                      title: '주차',
                      content: restaurant.parkingAvailable! ? '주차 가능' : '주차 불가',
                    ),
                  ],
                ],
              ),
            ),
          ],
        ),
      ),
      bottomNavigationBar: Container(
        padding: const EdgeInsets.all(16),
        child: Row(
          children: [
            Expanded(
              child: ElevatedButton.icon(
                onPressed: () => _launchMap(
                  restaurant.latitude,
                  restaurant.longitude,
                  restaurant.name,
                ),
                icon: const Icon(Icons.map),
                label: const Text('길찾기'),
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.blue,
                  foregroundColor: Colors.white,
                  padding: const EdgeInsets.symmetric(vertical: 12),
                ),
              ),
            ),
            if (restaurant.phone != null) ...[
              const SizedBox(width: 12),
              Expanded(
                child: ElevatedButton.icon(
                  onPressed: () => _launchPhone(restaurant.phone!),
                  icon: const Icon(Icons.phone),
                  label: const Text('전화'),
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.green,
                    foregroundColor: Colors.white,
                    padding: const EdgeInsets.symmetric(vertical: 12),
                  ),
                ),
              ),
            ],
          ],
        ),
      ),
    );
  }
}

class _InfoCard extends StatelessWidget {
  final IconData icon;
  final String title;
  final String content;
  final VoidCallback? onTap;

  const _InfoCard({
    required this.icon,
    required this.title,
    required this.content,
    this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return Card(
      child: InkWell(
        onTap: onTap,
        child: Padding(
          padding: const EdgeInsets.all(16),
          child: Row(
            children: [
              Icon(icon, color: Colors.orange),
              const SizedBox(width: 16),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      title,
                      style: const TextStyle(
                        fontSize: 12,
                        color: Colors.grey,
                        fontWeight: FontWeight.w500,
                      ),
                    ),
                    const SizedBox(height: 4),
                    Text(
                      content,
                      style: const TextStyle(
                        fontSize: 16,
                        fontWeight: FontWeight.w500,
                      ),
                    ),
                  ],
                ),
              ),
              if (onTap != null)
                const Icon(Icons.chevron_right, color: Colors.grey),
            ],
          ),
        ),
      ),
    );
  }
}