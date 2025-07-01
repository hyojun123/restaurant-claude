package com.claudetest.restaurantapi.controller;

import com.claudetest.restaurantapi.dto.RestaurantResponseDto;
import com.claudetest.restaurantapi.entity.Restaurant;
import com.claudetest.restaurantapi.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/restaurants")
@RequiredArgsConstructor
public class AdminRestaurantController {

    private final RestaurantRepository restaurantRepository;

    @GetMapping
    public ResponseEntity<?> getRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Restaurant> restaurants;

        if (name != null && !name.isEmpty()) {
            restaurants = restaurantRepository.findByNameContainingIgnoreCase(name, pageable);
        } else if (category != null && !category.isEmpty()) {
            restaurants = restaurantRepository.findByCategory(category, pageable);
        } else {
            restaurants = restaurantRepository.findAll(pageable);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("restaurants", restaurants.getContent().stream()
                .map(RestaurantResponseDto::from).toList());
        response.put("totalElements", restaurants.getTotalElements());
        response.put("totalPages", restaurants.getTotalPages());
        response.put("currentPage", restaurants.getNumber());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRestaurant(@PathVariable Long id) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        if (restaurant.isPresent()) {
            return ResponseEntity.ok(RestaurantResponseDto.from(restaurant.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> createRestaurant(@RequestBody Restaurant restaurant) {
        restaurant.setAverageRating(BigDecimal.ZERO);
        restaurant.setReviewCount(0);
        restaurant.setIsActive(true);
        Restaurant saved = restaurantRepository.save(restaurant);
        return ResponseEntity.ok(RestaurantResponseDto.from(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRestaurant(@PathVariable Long id, @RequestBody Restaurant restaurantUpdate) {
        Optional<Restaurant> existingRestaurant = restaurantRepository.findById(id);
        if (existingRestaurant.isPresent()) {
            Restaurant restaurant = existingRestaurant.get();
            restaurant.setName(restaurantUpdate.getName());
            restaurant.setCategory(restaurantUpdate.getCategory());
            restaurant.setAddress(restaurantUpdate.getAddress());
            restaurant.setLatitude(restaurantUpdate.getLatitude());
            restaurant.setLongitude(restaurantUpdate.getLongitude());
            restaurant.setPhone(restaurantUpdate.getPhone());
            restaurant.setBusinessHours(restaurantUpdate.getBusinessHours());
            restaurant.setPriceRange(restaurantUpdate.getPriceRange());
            restaurant.setParkingAvailable(restaurantUpdate.getParkingAvailable());
            
            Restaurant saved = restaurantRepository.save(restaurant);
            return ResponseEntity.ok(RestaurantResponseDto.from(saved));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRestaurant(@PathVariable Long id) {
        if (restaurantRepository.existsById(id)) {
            restaurantRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateRestaurantStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> status) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        if (restaurant.isPresent()) {
            Restaurant r = restaurant.get();
            r.setIsActive(status.get("isActive"));
            restaurantRepository.save(r);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() {
        return ResponseEntity.ok(restaurantRepository.findDistinctCategories());
    }
}