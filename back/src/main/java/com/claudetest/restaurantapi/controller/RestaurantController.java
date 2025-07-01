package com.claudetest.restaurantapi.controller;

import com.claudetest.restaurantapi.dto.RestaurantResponseDto;
import com.claudetest.restaurantapi.dto.RestaurantFilterDto;
import com.claudetest.restaurantapi.entity.Restaurant;
import com.claudetest.restaurantapi.service.RestaurantService;
import com.claudetest.restaurantapi.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/restaurants")
@Tag(name = "Restaurant", description = "맛집 관련 API")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;
    
    @Autowired
    private RecommendationService recommendationService;

    @GetMapping
    @Operation(summary = "맛집 목록 조회", description = "위치와 카테고리로 맛집을 조회합니다.")
    public ResponseEntity<List<RestaurantResponseDto>> getRestaurants(
            @RequestParam(required = false) BigDecimal latitude,
            @RequestParam(required = false) BigDecimal longitude,
            @RequestParam(defaultValue = "2.0") double radius,
            @RequestParam(required = false) String category) {

        List<RestaurantResponseDto> restaurants;

        if (latitude != null && longitude != null) {
            restaurants = restaurantService.getNearbyRestaurants(latitude, longitude, radius, category);
        } else if (category != null && !category.isEmpty()) {
            restaurants = restaurantService.getRestaurantsByCategory(category);
        } else {
            restaurants = restaurantService.getAllRestaurants();
        }

        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/{id}")
    @Operation(summary = "맛집 상세 조회", description = "특정 맛집의 상세 정보를 조회합니다.")
    public ResponseEntity<RestaurantResponseDto> getRestaurant(@PathVariable Long id) {
        Optional<RestaurantResponseDto> restaurant = restaurantService.getRestaurantById(id);
        return restaurant.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/categories")
    @Operation(summary = "카테고리 목록", description = "모든 음식 카테고리를 조회합니다.")
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = restaurantService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/random")
    @Operation(summary = "랜덤 맛집 추천", description = "랜덤하게 맛집을 추천합니다.")
    public ResponseEntity<RestaurantResponseDto> getRandomRestaurant(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal latitude,
            @RequestParam(required = false) BigDecimal longitude,
            @RequestParam(defaultValue = "2.0") Double radius,
            Authentication authentication) {
        
        Long userId = null;
        if (authentication != null && authentication.isAuthenticated()) {
            // Extract user ID from authentication if needed
            userId = extractUserIdFromAuthentication(authentication);
        }
        
        Restaurant restaurant = recommendationService.getRandomRecommendation(userId, category, latitude, longitude, radius);
        if (restaurant != null) {
            return ResponseEntity.ok(RestaurantResponseDto.from(restaurant));
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/recommendations")
    @Operation(summary = "개인화된 맛집 추천", description = "사용자 취향을 고려한 맛집을 추천합니다.")
    public ResponseEntity<List<RestaurantResponseDto>> getPersonalizedRecommendations(
            @RequestParam(required = false) BigDecimal latitude,
            @RequestParam(required = false) BigDecimal longitude,
            @RequestParam(defaultValue = "2.0") Double radius,
            @RequestParam(defaultValue = "5") int limit,
            Authentication authentication) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.badRequest().build();
        }
        
        Long userId = extractUserIdFromAuthentication(authentication);
        List<Restaurant> recommendations = recommendationService.getPersonalizedRecommendations(userId, latitude, longitude, radius, limit);
        
        List<RestaurantResponseDto> response = recommendations.stream()
                .map(RestaurantResponseDto::from)
                .toList();
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/search")
    @Operation(summary = "고급 필터 검색", description = "다양한 조건으로 맛집을 검색합니다.")
    public ResponseEntity<Map<String, Object>> searchRestaurants(@RequestBody RestaurantFilterDto filter) {
        Page<RestaurantResponseDto> results = restaurantService.searchWithFilters(filter);
        
        Map<String, Object> response = new HashMap<>();
        response.put("restaurants", results.getContent());
        response.put("totalElements", results.getTotalElements());
        response.put("totalPages", results.getTotalPages());
        response.put("currentPage", results.getNumber());
        response.put("size", results.getSize());
        response.put("hasNext", results.hasNext());
        response.put("hasPrevious", results.hasPrevious());
        
        return ResponseEntity.ok(response);
    }
    
    private Long extractUserIdFromAuthentication(Authentication authentication) {
        // Implementation depends on your JWT token structure
        // For now, return null - this should be implemented based on your UserPrincipal
        return null;
    }
}