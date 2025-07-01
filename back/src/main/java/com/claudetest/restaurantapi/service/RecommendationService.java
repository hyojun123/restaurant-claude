package com.claudetest.restaurantapi.service;

import com.claudetest.restaurantapi.entity.Restaurant;
import com.claudetest.restaurantapi.entity.User;
import com.claudetest.restaurantapi.repository.FavoriteRepository;
import com.claudetest.restaurantapi.repository.RestaurantRepository;
import com.claudetest.restaurantapi.repository.ReviewRepository;
import com.claudetest.restaurantapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;
    private final ReviewRepository reviewRepository;

    public Restaurant getRandomRecommendation(Long userId, String category, BigDecimal latitude, BigDecimal longitude, Double radiusKm) {
        List<Restaurant> candidates = new ArrayList<>();
        
        if (category != null && !category.isEmpty()) {
            if (latitude != null && longitude != null && radiusKm != null) {
                candidates = restaurantRepository.findNearbyRestaurantsByCategory(latitude, longitude, radiusKm, category);
            } else {
                candidates = restaurantRepository.findByCategoryAndIsActiveTrue(category);
            }
        } else {
            if (latitude != null && longitude != null && radiusKm != null) {
                candidates = restaurantRepository.findNearbyRestaurants(latitude, longitude, radiusKm);
            } else {
                candidates = restaurantRepository.findByIsActiveTrue();
            }
        }

        if (candidates.isEmpty()) {
            return null;
        }

        if (userId != null) {
            candidates = filterUserPreferences(userId, candidates);
        }

        return getWeightedRandomRestaurant(candidates);
    }

    public List<Restaurant> getPersonalizedRecommendations(Long userId, BigDecimal latitude, BigDecimal longitude, Double radiusKm, int limit) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return getTopRatedRestaurants(latitude, longitude, radiusKm, limit);
        }

        User user = userOpt.get();
        List<String> favoriteCategories = getUserFavoriteCategories(userId);
        List<Restaurant> recommendations = new ArrayList<>();

        for (String category : favoriteCategories) {
            List<Restaurant> categoryRestaurants;
            if (latitude != null && longitude != null && radiusKm != null) {
                categoryRestaurants = restaurantRepository.findNearbyRestaurantsByCategory(latitude, longitude, radiusKm, category);
            } else {
                categoryRestaurants = restaurantRepository.findByCategoryAndIsActiveTrue(category);
            }
            
            categoryRestaurants = categoryRestaurants.stream()
                    .filter(r -> !hasUserVisited(userId, r.getRestaurantId()))
                    .collect(Collectors.toList());
            
            recommendations.addAll(categoryRestaurants.stream()
                    .sorted((r1, r2) -> r2.getAverageRating().compareTo(r1.getAverageRating()))
                    .limit(2)
                    .collect(Collectors.toList()));
        }

        if (recommendations.size() < limit) {
            List<Restaurant> additionalRecs = getTopRatedRestaurants(latitude, longitude, radiusKm, limit - recommendations.size());
            recommendations.addAll(additionalRecs.stream()
                    .filter(r -> !recommendations.contains(r) && !hasUserVisited(userId, r.getRestaurantId()))
                    .collect(Collectors.toList()));
        }

        return recommendations.stream().limit(limit).collect(Collectors.toList());
    }

    private List<Restaurant> filterUserPreferences(Long userId, List<Restaurant> candidates) {
        Set<Long> visitedRestaurantIds = favoriteRepository.findByUserIdWithRestaurant(userId)
                .stream()
                .map(f -> f.getRestaurant().getRestaurantId())
                .collect(Collectors.toSet());

        List<String> favoriteCategories = getUserFavoriteCategories(userId);
        
        List<Restaurant> preferredRestaurants = candidates.stream()
                .filter(r -> favoriteCategories.contains(r.getCategory()) && !visitedRestaurantIds.contains(r.getRestaurantId()))
                .collect(Collectors.toList());

        if (!preferredRestaurants.isEmpty()) {
            return preferredRestaurants;
        }

        return candidates.stream()
                .filter(r -> !visitedRestaurantIds.contains(r.getRestaurantId()))
                .collect(Collectors.toList());
    }

    private List<String> getUserFavoriteCategories(Long userId) {
        return favoriteRepository.findByUserIdWithRestaurant(userId)
                .stream()
                .map(f -> f.getRestaurant().getCategory())
                .collect(Collectors.groupingBy(category -> category, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(3)
                .collect(Collectors.toList());
    }

    private boolean hasUserVisited(Long userId, Long restaurantId) {
        return favoriteRepository.existsByUserUserIdAndRestaurantRestaurantId(userId, restaurantId) ||
               reviewRepository.existsByUserUserIdAndRestaurantRestaurantId(userId, restaurantId);
    }

    private Restaurant getWeightedRandomRestaurant(List<Restaurant> restaurants) {
        if (restaurants.isEmpty()) {
            return null;
        }

        double totalWeight = restaurants.stream()
                .mapToDouble(r -> Math.pow(r.getAverageRating().doubleValue(), 2) * (1 + r.getReviewCount() * 0.1))
                .sum();

        double randomValue = Math.random() * totalWeight;
        double currentWeight = 0;

        for (Restaurant restaurant : restaurants) {
            currentWeight += Math.pow(restaurant.getAverageRating().doubleValue(), 2) * (1 + restaurant.getReviewCount() * 0.1);
            if (currentWeight >= randomValue) {
                return restaurant;
            }
        }

        return restaurants.get(0);
    }

    private List<Restaurant> getTopRatedRestaurants(BigDecimal latitude, BigDecimal longitude, Double radiusKm, int limit) {
        List<Restaurant> restaurants;
        if (latitude != null && longitude != null && radiusKm != null) {
            restaurants = restaurantRepository.findNearbyRestaurants(latitude, longitude, radiusKm);
        } else {
            restaurants = restaurantRepository.findByIsActiveTrue();
        }

        return restaurants.stream()
                .sorted((r1, r2) -> {
                    int ratingCompare = r2.getAverageRating().compareTo(r1.getAverageRating());
                    if (ratingCompare == 0) {
                        return Integer.compare(r2.getReviewCount(), r1.getReviewCount());
                    }
                    return ratingCompare;
                })
                .limit(limit)
                .collect(Collectors.toList());
    }
}