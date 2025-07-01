import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/restaurant_provider.dart';

class CategoryGrid extends StatelessWidget {
  const CategoryGrid({super.key});

  final Map<String, String> categoryEmojis = const {
    '한식': '🍚',
    '일식': '🍣',
    '중식': '🍜',
    '양식': '🍝',
    '카페': '☕',
    '술집': '🍺',
  };

  @override
  Widget build(BuildContext context) {
    return Consumer<RestaurantProvider>(
      builder: (context, provider, child) {
        if (provider.categories.isEmpty) {
          return const Center(
            child: CircularProgressIndicator(),
          );
        }

        return GridView.builder(
          shrinkWrap: true,
          physics: const NeverScrollableScrollPhysics(),
          gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
            crossAxisCount: 3,
            crossAxisSpacing: 12,
            mainAxisSpacing: 12,
            childAspectRatio: 1,
          ),
          itemCount: provider.categories.length,
          itemBuilder: (context, index) {
            final category = provider.categories[index];
            final emoji = categoryEmojis[category] ?? '🍽️';

            return Card(
              elevation: 2,
              child: InkWell(
                onTap: () {
                  Navigator.pushNamed(
                    context,
                    '/category',
                    arguments: category,
                  );
                },
                borderRadius: BorderRadius.circular(8),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text(
                      emoji,
                      style: const TextStyle(fontSize: 32),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      category,
                      style: const TextStyle(
                        fontSize: 14,
                        fontWeight: FontWeight.w500,
                      ),
                      textAlign: TextAlign.center,
                    ),
                  ],
                ),
              ),
            );
          },
        );
      },
    );
  }
}