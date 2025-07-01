package com.claudetest.restaurantapi.service;

import com.claudetest.restaurantapi.dto.RestaurantResponseDto;
import com.claudetest.restaurantapi.dto.RestaurantFilterDto;
import com.claudetest.restaurantapi.entity.Restaurant;
import com.claudetest.restaurantapi.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Cacheable(value = "restaurants", key = "'all'")
    public List<RestaurantResponseDto> getAllRestaurants() {
        return restaurantRepository.findByIsActiveTrue()
                .stream()
                .map(RestaurantResponseDto::new)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "restaurants", key = "#category")
    public List<RestaurantResponseDto> getRestaurantsByCategory(String category) {
        return restaurantRepository.findByCategoryAndIsActiveTrue(category)
                .stream()
                .map(RestaurantResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<RestaurantResponseDto> getNearbyRestaurants(
            BigDecimal latitude, BigDecimal longitude, double radiusKm, String category) {
        
        List<Restaurant> restaurants;
        if (category != null && !category.isEmpty()) {
            restaurants = restaurantRepository.findNearbyRestaurantsByCategory(
                    latitude, longitude, radiusKm, category);
        } else {
            restaurants = restaurantRepository.findNearbyRestaurants(
                    latitude, longitude, radiusKm);
        }

        return restaurants.stream()
                .map(restaurant -> {
                    RestaurantResponseDto dto = new RestaurantResponseDto(restaurant);
                    double distance = calculateDistance(
                            latitude.doubleValue(), longitude.doubleValue(),
                            restaurant.getLatitude().doubleValue(), restaurant.getLongitude().doubleValue()
                    );
                    dto.setDistanceKm(Math.round(distance * 100.0) / 100.0);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Optional<RestaurantResponseDto> getRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .filter(Restaurant::getIsActive)
                .map(RestaurantResponseDto::new);
    }

    @Cacheable(value = "categories")
    public List<String> getAllCategories() {
        return restaurantRepository.findAllCategories();
    }

    public Optional<RestaurantResponseDto> getRandomRestaurant(String category) {
        Restaurant restaurant;
        if (category != null && !category.isEmpty()) {
            restaurant = restaurantRepository.findRandomRestaurantByCategory(category);
        } else {
            restaurant = restaurantRepository.findRandomRestaurant();
        }
        
        return restaurant != null ? Optional.of(new RestaurantResponseDto(restaurant)) : Optional.empty();
    }
    
    public Page<RestaurantResponseDto> searchWithFilters(RestaurantFilterDto filter) {
        Sort sort = createSort(filter.getSortBy(), filter.getSortDirection());
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), sort);
        
        Page<Restaurant> restaurantPage;
        String categoryParam = (filter.getCategories() != null && !filter.getCategories().isEmpty()) 
                ? filter.getCategories().get(0) : null;
        
        if (filter.getLatitude() != null && filter.getLongitude() != null) {
            restaurantPage = restaurantRepository.findNearbyWithFilters(
                    filter.getLatitude(),
                    filter.getLongitude(),
                    filter.getRadius(),
                    filter.getCategories(),
                    categoryParam,
                    filter.getPriceRange(),
                    filter.getMinRating(),
                    filter.getMinReviewCount(),
                    filter.getParkingAvailable(),
                    pageable
            );
        } else {
            restaurantPage = restaurantRepository.findWithFilters(
                    filter.getCategories(),
                    categoryParam,
                    filter.getPriceRange(),
                    filter.getMinRating(),
                    filter.getMinReviewCount(),
                    filter.getParkingAvailable(),
                    pageable
            );
        }
        
        return restaurantPage.map(restaurant -> {
            RestaurantResponseDto dto = new RestaurantResponseDto(restaurant);
            if (filter.getLatitude() != null && filter.getLongitude() != null) {
                double distance = calculateDistance(
                        filter.getLatitude().doubleValue(), 
                        filter.getLongitude().doubleValue(),
                        restaurant.getLatitude().doubleValue(), 
                        restaurant.getLongitude().doubleValue()
                );
                dto.setDistanceKm(Math.round(distance * 100.0) / 100.0);
            }
            return dto;
        });
    }
    
    private Sort createSort(String sortBy, String direction) {
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) 
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        switch (sortBy.toLowerCase()) {
            case "rating":
                return Sort.by(sortDirection, "averageRating");
            case "reviewcount":
                return Sort.by(sortDirection, "reviewCount");
            case "name":
                return Sort.by(sortDirection, "name");
            case "distance":
            default:
                return Sort.by(sortDirection, "restaurantId"); // Default fallback
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 지구 반지름 (km)
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }
}