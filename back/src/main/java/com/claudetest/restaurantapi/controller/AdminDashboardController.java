package com.claudetest.restaurantapi.controller;

import com.claudetest.restaurantapi.dto.admin.DashboardStatsDto;
import com.claudetest.restaurantapi.repository.RestaurantRepository;
import com.claudetest.restaurantapi.repository.ReviewRepository;
import com.claudetest.restaurantapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDto> getStats() {
        LocalDateTime startOfToday = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endOfToday = LocalDateTime.now().with(LocalTime.MAX);

        Long totalUsers = userRepository.count();
        Long totalRestaurants = restaurantRepository.count();
        Long totalReviews = reviewRepository.count();
        Long todayNewUsers = userRepository.countByCreatedAtBetween(startOfToday, endOfToday);
        Long todayNewReviews = reviewRepository.countByCreatedAtBetween(startOfToday, endOfToday);
        BigDecimal averageRating = reviewRepository.findAverageRating();

        DashboardStatsDto stats = DashboardStatsDto.builder()
                .totalUsers(totalUsers)
                .totalRestaurants(totalRestaurants)
                .totalReviews(totalReviews)
                .todayNewUsers(todayNewUsers)
                .todayNewReviews(todayNewReviews)
                .averageRating(averageRating != null ? averageRating : BigDecimal.ZERO)
                .build();

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/popular-restaurants")
    public ResponseEntity<?> getPopularRestaurants() {
        List<Object[]> popularRestaurants = restaurantRepository.findTop10ByOrderByAverageRatingDesc();
        
        return ResponseEntity.ok(popularRestaurants.stream().map(result -> {
            Map<String, Object> restaurant = new HashMap<>();
            restaurant.put("name", result[0]);
            restaurant.put("category", result[1]);
            restaurant.put("averageRating", result[2]);
            restaurant.put("reviewCount", result[3]);
            return restaurant;
        }).toList());
    }

    @GetMapping("/user-trends")
    public ResponseEntity<?> getUserTrends() {
        List<Object[]> monthlyStats = userRepository.findMonthlyUserStats();
        
        return ResponseEntity.ok(monthlyStats.stream().map(result -> {
            Map<String, Object> stat = new HashMap<>();
            stat.put("month", result[0]);
            stat.put("userCount", result[1]);
            return stat;
        }).toList());
    }
}