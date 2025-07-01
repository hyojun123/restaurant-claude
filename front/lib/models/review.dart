class Review {
  final int reviewId;
  final int userId;
  final String userNickname;
  final String? userProfileImage;
  final int restaurantId;
  final String restaurantName;
  final int rating;
  final String? content;
  final List<String> photos;
  final String? tags;
  final int likeCount;
  final DateTime createdAt;
  final DateTime updatedAt;

  Review({
    required this.reviewId,
    required this.userId,
    required this.userNickname,
    this.userProfileImage,
    required this.restaurantId,
    required this.restaurantName,
    required this.rating,
    this.content,
    required this.photos,
    this.tags,
    required this.likeCount,
    required this.createdAt,
    required this.updatedAt,
  });

  factory Review.fromJson(Map<String, dynamic> json) {
    return Review(
      reviewId: json['reviewId'],
      userId: json['userId'],
      userNickname: json['userNickname'],
      userProfileImage: json['userProfileImage'],
      restaurantId: json['restaurantId'],
      restaurantName: json['restaurantName'],
      rating: json['rating'],
      content: json['content'],
      photos: json['photos'] != null ? List<String>.from(json['photos']) : [],
      tags: json['tags'],
      likeCount: json['likeCount'],
      createdAt: DateTime.parse(json['createdAt']),
      updatedAt: DateTime.parse(json['updatedAt']),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'reviewId': reviewId,
      'userId': userId,
      'userNickname': userNickname,
      'userProfileImage': userProfileImage,
      'restaurantId': restaurantId,
      'restaurantName': restaurantName,
      'rating': rating,
      'content': content,
      'photos': photos,
      'tags': tags,
      'likeCount': likeCount,
      'createdAt': createdAt.toIso8601String(),
      'updatedAt': updatedAt.toIso8601String(),
    };
  }

  String get formattedDate {
    final now = DateTime.now();
    final difference = now.difference(createdAt);
    
    if (difference.inDays > 0) {
      return '${difference.inDays}일 전';
    } else if (difference.inHours > 0) {
      return '${difference.inHours}시간 전';
    } else if (difference.inMinutes > 0) {
      return '${difference.inMinutes}분 전';
    } else {
      return '방금 전';
    }
  }

  String get ratingStars => '⭐' * rating;
}