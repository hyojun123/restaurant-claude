class Restaurant {
  final int restaurantId;
  final String name;
  final String category;
  final String address;
  final double latitude;
  final double longitude;
  final String? phone;
  final String? businessHours;
  final String? priceRange;
  final bool? parkingAvailable;
  final double averageRating;
  final int reviewCount;
  final double? distanceKm;

  Restaurant({
    required this.restaurantId,
    required this.name,
    required this.category,
    required this.address,
    required this.latitude,
    required this.longitude,
    this.phone,
    this.businessHours,
    this.priceRange,
    this.parkingAvailable,
    required this.averageRating,
    required this.reviewCount,
    this.distanceKm,
  });

  factory Restaurant.fromJson(Map<String, dynamic> json) {
    return Restaurant(
      restaurantId: json['restaurantId'],
      name: json['name'],
      category: json['category'],
      address: json['address'],
      latitude: double.parse(json['latitude'].toString()),
      longitude: double.parse(json['longitude'].toString()),
      phone: json['phone'],
      businessHours: json['businessHours'],
      priceRange: json['priceRange'],
      parkingAvailable: json['parkingAvailable'],
      averageRating: double.parse(json['averageRating'].toString()),
      reviewCount: json['reviewCount'],
      distanceKm: json['distanceKm']?.toDouble(),
    );
  }

  String get formattedDistance {
    if (distanceKm == null) return '';
    if (distanceKm! < 1.0) {
      return '${(distanceKm! * 1000).round()}m';
    }
    return '${distanceKm!.toStringAsFixed(1)}km';
  }

  String get ratingDisplay => '⭐ ${averageRating.toStringAsFixed(1)}';
  
  String get priceDisplay {
    switch (priceRange) {
      case '저렴':
        return '💰';
      case '보통':
        return '💰💰';
      case '비싼':
        return '💰💰💰';
      default:
        return '💰💰';
    }
  }
}