import 'package:flutter/material.dart';
import 'package:flutter/foundation.dart';
import 'package:geolocator/geolocator.dart';

// 모바일에서 사용하는 지도 위젯
import 'mobile_map_widget.dart' if (dart.library.html) 'web_map_widget_stub.dart';

class NaverMapWidget extends StatelessWidget {
  final Position? currentPosition;
  final List<dynamic>? restaurants;

  const NaverMapWidget({
    super.key,
    this.currentPosition,
    this.restaurants,
  });

  @override
  Widget build(BuildContext context) {
    // 웹에서는 WebMapWidget 사용
    /*if (kIsWeb) {
      return WebMapWidget(
        currentPosition: currentPosition,
        restaurants: restaurants,
      );
    }*/

    // 모바일/에뮬레이터에서는 실제 네이버 지도 사용
    return MobileMapWidget(
      currentPosition: currentPosition,
      restaurants: restaurants,
    );
  }

}