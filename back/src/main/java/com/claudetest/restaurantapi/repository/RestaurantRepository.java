package com.claudetest.restaurantapi.repository;

import com.claudetest.restaurantapi.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    
    List<Restaurant> findByIsActiveTrue();
    
    List<Restaurant> findByCategoryAndIsActiveTrue(String category);
    
    @Query("SELECT r FROM Restaurant r WHERE r.isActive = true AND " +
           "r.category = :category AND " +
           "(6371 * acos(cos(radians(:latitude)) * cos(radians(r.latitude)) * " +
           "cos(radians(r.longitude) - radians(:longitude)) + " +
           "sin(radians(:latitude)) * sin(radians(r.latitude)))) <= :radiusKm " +
           "ORDER BY (6371 * acos(cos(radians(:latitude)) * cos(radians(r.latitude)) * " +
           "cos(radians(r.longitude) - radians(:longitude)) + " +
           "sin(radians(:latitude)) * sin(radians(r.latitude))))")
    List<Restaurant> findNearbyRestaurantsByCategory(
        @Param("latitude") BigDecimal latitude, 
        @Param("longitude") BigDecimal longitude, 
        @Param("radiusKm") double radiusKm,
        @Param("category") String category
    );
    
    @Query("SELECT r FROM Restaurant r WHERE r.isActive = true AND " +
           "(6371 * acos(cos(radians(:latitude)) * cos(radians(r.latitude)) * " +
           "cos(radians(r.longitude) - radians(:longitude)) + " +
           "sin(radians(:latitude)) * sin(radians(r.latitude)))) <= :radiusKm " +
           "ORDER BY (6371 * acos(cos(radians(:latitude)) * cos(radians(r.latitude)) * " +
           "cos(radians(r.longitude) - radians(:longitude)) + " +
           "sin(radians(:latitude)) * sin(radians(r.latitude))))")
    List<Restaurant> findNearbyRestaurants(
        @Param("latitude") BigDecimal latitude, 
        @Param("longitude") BigDecimal longitude, 
        @Param("radiusKm") double radiusKm
    );
    
    @Query("SELECT DISTINCT r.category FROM Restaurant r WHERE r.isActive = true ORDER BY r.category")
    List<String> findAllCategories();
    
    @Query("SELECT r FROM Restaurant r WHERE r.isActive = true ORDER BY RANDOM() LIMIT 1")
    Restaurant findRandomRestaurant();
    
    @Query("SELECT r FROM Restaurant r WHERE r.isActive = true AND r.category = :category ORDER BY RANDOM() LIMIT 1")
    Restaurant findRandomRestaurantByCategory(@Param("category") String category);
    
    Page<Restaurant> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Restaurant> findByCategory(String category, Pageable pageable);
    
    @Query("SELECT DISTINCT r.category FROM Restaurant r ORDER BY r.category")
    List<String> findDistinctCategories();
    
    @Query("SELECT r.name, r.category, r.averageRating, r.reviewCount FROM Restaurant r WHERE r.isActive = true ORDER BY r.averageRating DESC, r.reviewCount DESC")
    List<Object[]> findTop10ByOrderByAverageRatingDesc();
    
    @Query("SELECT r FROM Restaurant r WHERE r.isActive = true " +
           "AND (:category IS NULL OR r.category IN :categories) " +
           "AND (:priceRange IS NULL OR r.priceRange = :priceRange) " +
           "AND (:minRating IS NULL OR r.averageRating >= :minRating) " +
           "AND (:minReviewCount IS NULL OR r.reviewCount >= :minReviewCount) " +
           "AND (:parkingAvailable IS NULL OR r.parkingAvailable = :parkingAvailable)")
    Page<Restaurant> findWithFilters(
            @Param("categories") List<String> categories,
            @Param("category") String category,
            @Param("priceRange") String priceRange,
            @Param("minRating") Double minRating,
            @Param("minReviewCount") Integer minReviewCount,
            @Param("parkingAvailable") Boolean parkingAvailable,
            Pageable pageable);
            
    @Query("SELECT r FROM Restaurant r WHERE r.isActive = true " +
           "AND (6371 * acos(cos(radians(:latitude)) * cos(radians(r.latitude)) * " +
           "cos(radians(r.longitude) - radians(:longitude)) + " +
           "sin(radians(:latitude)) * sin(radians(r.latitude)))) <= :radiusKm " +
           "AND (:category IS NULL OR r.category IN :categories) " +
           "AND (:priceRange IS NULL OR r.priceRange = :priceRange) " +
           "AND (:minRating IS NULL OR r.averageRating >= :minRating) " +
           "AND (:minReviewCount IS NULL OR r.reviewCount >= :minReviewCount) " +
           "AND (:parkingAvailable IS NULL OR r.parkingAvailable = :parkingAvailable)")
    Page<Restaurant> findNearbyWithFilters(
            @Param("latitude") BigDecimal latitude,
            @Param("longitude") BigDecimal longitude,
            @Param("radiusKm") Double radiusKm,
            @Param("categories") List<String> categories,
            @Param("category") String category,
            @Param("priceRange") String priceRange,
            @Param("minRating") Double minRating,
            @Param("minReviewCount") Integer minReviewCount,
            @Param("parkingAvailable") Boolean parkingAvailable,
            Pageable pageable);
}