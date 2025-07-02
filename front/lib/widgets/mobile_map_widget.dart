import 'package:flutter/material.dart';
import 'package:flutter_naver_map/flutter_naver_map.dart';
import 'package:geolocator/geolocator.dart';

class MobileMapWidget extends StatefulWidget {
  final Position? currentPosition;
  final List<dynamic>? restaurants;

  const MobileMapWidget({
    super.key,
    this.currentPosition,
    this.restaurants,
  });

  @override
  State<MobileMapWidget> createState() => _MobileMapWidgetState();
}

class _MobileMapWidgetState extends State<MobileMapWidget> {
  NaverMapController? _controller;
  bool _isMapReady = false;

  @override
  Widget build(BuildContext context) {
    // 현재 위치가 없으면 로딩 표시
    if (widget.currentPosition == null) {
      return Container(
        color: Colors.grey[200],
        child: const Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              CircularProgressIndicator(
                valueColor: AlwaysStoppedAnimation<Color>(Colors.orange),
              ),
              SizedBox(height: 16),
              Text(
                '위치 정보를 가져오는 중...',
                style: TextStyle(
                  fontSize: 16,
                  color: Colors.grey,
                ),
              ),
            ],
          ),
        ),
      );
    }

    return Stack(
      children: [
        NaverMap(
          options: NaverMapViewOptions(
            initialCameraPosition: NCameraPosition(
              target: NLatLng(
                widget.currentPosition!.latitude,
                widget.currentPosition!.longitude,
              ),
              zoom: 14,
            ),
            mapType: NMapType.basic,
            activeLayerGroups: [NLayerGroup.building, NLayerGroup.transit],
            locationButtonEnable: true,
            consumeSymbolTapEvents: false,
            // 에뮬레이터에서 지도 렌더링을 위한 추가 옵션
            logoClickEnable: false,
            scaleBarEnable: true,
            indoorEnable: true,
            rotationGesturesEnable: true,
            scrollGesturesEnable: true,
            tiltGesturesEnable: true,
            zoomGesturesEnable: true,
          ),
          onMapReady: (controller) async {
            _controller = controller;
            setState(() {
              _isMapReady = true;
            });
            
            debugPrint('네이버 지도 초기화 완료');
            
            // 약간의 딜레이 후 마커 추가 (지도 완전 로딩 대기)
            await Future.delayed(const Duration(milliseconds: 500));
            
            // 현재 위치에 마커 추가
            await _addCurrentLocationMarker();
            
            // 맛집 마커들 추가
            if (widget.restaurants != null && widget.restaurants!.isNotEmpty) {
              await _addRestaurantMarkers();
            }
          },
          onMapTapped: (point, latLng) {
            debugPrint('지도 탭: ${latLng.latitude}, ${latLng.longitude}');
          },
          onCameraChange: (position, reason) {
            // 카메라 변경 시 로직 (필요시)
          },
          onCameraIdle: () {
            // 카메라 이동 완료 시 로직 (필요시)
          },
        ),
        
        // 지도 로딩 중 오버레이
        if (!_isMapReady)
          Container(
            color: Colors.white.withOpacity(0.8),
            child: const Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  CircularProgressIndicator(
                    valueColor: AlwaysStoppedAnimation<Color>(Colors.orange),
                  ),
                  SizedBox(height: 16),
                  Text(
                    '네이버 지도를 로딩 중...',
                    style: TextStyle(
                      fontSize: 16,
                      color: Colors.grey,
                      fontWeight: FontWeight.w500,
                    ),
                  ),
                ],
              ),
            ),
          ),
      ],
    );
  }

  Future<void> _addCurrentLocationMarker() async {
    if (_controller == null || widget.currentPosition == null) return;

    try {
      final marker = NMarker(
        id: 'current_location',
        position: NLatLng(
            37.5012345,
            127.0345678
          // widget.currentPosition!.latitude,
          // widget.currentPosition!.longitude,
        ),
        caption: NOverlayCaption(
          text: '현재 위치',
          textSize: 14,
          color: Colors.blue,
          haloColor: Colors.white,
        ),
      );

      await _controller!.addOverlay(marker);
      debugPrint('현재 위치 마커 추가됨');
    } catch (e) {
      debugPrint('현재 위치 마커 추가 오류: $e');
    }
  }

  Future<void> _addRestaurantMarkers() async {
    if (_controller == null || widget.restaurants == null) return;

    try {
      for (int i = 0; i < widget.restaurants!.length; i++) {
        final restaurant = widget.restaurants![i];
        
        double? lat, lng;
        String? name;
        
        if (restaurant is Map) {
          lat = restaurant['latitude']?.toDouble();
          lng = restaurant['longitude']?.toDouble();
          name = restaurant['name']?.toString();
        } else {
          try {
            lat = restaurant.latitude?.toDouble();
            lng = restaurant.longitude?.toDouble();
            name = restaurant.name;
          } catch (e) {
            debugPrint('맛집 데이터 파싱 오류: $e');
            continue;
          }
        }

        if (lat != null && lng != null && name != null) {
          final marker = NMarker(
            id: 'restaurant_$i',
            position: NLatLng(lat, lng),
            caption: NOverlayCaption(
              text: name,
              textSize: 12,
              color: Colors.red,
              haloColor: Colors.white,
            ),
          );

          // 마커 탭 이벤트
          marker.setOnTapListener((overlay) {
            _showRestaurantInfo(restaurant);
          });

          await _controller!.addOverlay(marker);
        }
      }
      debugPrint('맛집 마커 ${widget.restaurants!.length}개 추가됨');
    } catch (e) {
      debugPrint('맛집 마커 추가 오류: $e');
    }
  }

  void _showRestaurantInfo(dynamic restaurant) {
    String name = '';
    String category = '';
    double? rating;
    
    if (restaurant is Map) {
      name = restaurant['name']?.toString() ?? '';
      category = restaurant['category']?.toString() ?? '';
      rating = restaurant['averageRating']?.toDouble();
    } else {
      try {
        name = restaurant.name ?? '';
        category = restaurant.category ?? '';
        rating = restaurant.averageRating;
      } catch (e) {
        debugPrint('맛집 정보 파싱 오류: $e');
      }
    }

    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Row(
          children: [
            const Icon(Icons.restaurant, color: Colors.orange),
            const SizedBox(width: 8),
            Expanded(child: Text(name)),
          ],
        ),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            if (category.isNotEmpty) ...[
              Row(
                children: [
                  const Icon(Icons.category, size: 16, color: Colors.grey),
                  const SizedBox(width: 8),
                  Text('카테고리: $category'),
                ],
              ),
              const SizedBox(height: 8),
            ],
            if (rating != null) ...[
              Row(
                children: [
                  const Icon(Icons.star, size: 16, color: Colors.amber),
                  const SizedBox(width: 8),
                  Text('평점: ${rating.toStringAsFixed(1)}⭐'),
                ],
              ),
            ],
          ],
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('닫기'),
          ),
          ElevatedButton(
            onPressed: () {
              Navigator.pop(context);
              Navigator.pushNamed(
                context,
                '/restaurant-detail',
                arguments: restaurant,
              );
            },
            style: ElevatedButton.styleFrom(
              backgroundColor: Colors.orange,
              foregroundColor: Colors.white,
            ),
            child: const Text('상세보기'),
          ),
        ],
      ),
    );
  }
}