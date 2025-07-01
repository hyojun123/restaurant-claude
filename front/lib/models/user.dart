class User {
  final int userId;
  final String email;
  final String nickname;
  final String? profileImageUrl;
  final String socialType;
  final DateTime createdAt;

  User({
    required this.userId,
    required this.email,
    required this.nickname,
    this.profileImageUrl,
    required this.socialType,
    required this.createdAt,
  });

  factory User.fromJson(Map<String, dynamic> json) {
    return User(
      userId: json['userId'],
      email: json['email'],
      nickname: json['nickname'],
      profileImageUrl: json['profileImageUrl'],
      socialType: json['socialType'] ?? 'LOCAL',
      createdAt: DateTime.parse(json['createdAt']),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'userId': userId,
      'email': email,
      'nickname': nickname,
      'profileImageUrl': profileImageUrl,
      'socialType': socialType,
      'createdAt': createdAt.toIso8601String(),
    };
  }

  String get displayName => nickname.isNotEmpty ? nickname : email;
  
  String get initials {
    if (nickname.isNotEmpty) {
      return nickname.substring(0, 1).toUpperCase();
    }
    return email.substring(0, 1).toUpperCase();
  }
}