import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:flutter/foundation.dart';
import 'screens/home_screen.dart';
import 'screens/category_screen.dart';
import 'screens/restaurant_detail_screen.dart';
import 'screens/login_screen.dart';
import 'screens/signup_screen.dart';
import 'screens/favorites_screen.dart';
import 'screens/profile_screen.dart';
import 'providers/location_provider.dart';
import 'providers/restaurant_provider.dart';
import 'providers/auth_provider.dart';
import 'services/auth_service.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  // AuthService.loadToken() 호출 제거 - AuthProvider에서 처리
  runApp(const RestaurantApp());
}

class RestaurantApp extends StatelessWidget {
  const RestaurantApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (_) => AuthProvider()),
        ChangeNotifierProvider(create: (_) => LocationProvider()),
        ChangeNotifierProvider(create: (_) => RestaurantProvider()),
      ],
      child: Consumer<AuthProvider>(
        builder: (context, authProvider, child) {
          return MaterialApp(
            title: '아무거나 요정 - 뭐 먹을래?',
            theme: ThemeData(
              primarySwatch: Colors.orange,
              colorScheme: ColorScheme.fromSeed(
                seedColor: Colors.orange,
                brightness: Brightness.light,
              ),
              useMaterial3: true,
              appBarTheme: const AppBarTheme(
                backgroundColor: Colors.orange,
                foregroundColor: Colors.white,
                elevation: 2,
              ),
            ),
            home: FutureBuilder(
              future: authProvider.loadUser(),
              builder: (context, snapshot) {
                return const HomeScreen();
              },
            ),
            routes: {
              '/login': (context) => const LoginScreen(),
              '/signup': (context) => const SignupScreen(),
              '/category': (context) => const CategoryScreen(),
              '/restaurant-detail': (context) => const RestaurantDetailScreen(),
              '/favorites': (context) => const FavoritesScreen(),
              '/profile': (context) => const ProfileScreen(),
            },
            onGenerateRoute: (settings) {
              // 동적 라우팅을 위한 추가 설정
              if (settings.name?.startsWith('/reviews/') == true) {
                // 리뷰 화면 등 추가 라우팅 처리
              }
              return null;
            },
          );
        },
      ),
    );
  }
}