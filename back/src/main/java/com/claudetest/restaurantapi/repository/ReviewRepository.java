package com.claudetest.restaurantapi.repository;

import com.claudetest.restaurantapi.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    Page<Review> findByRestaurantRestaurantIdAndIsActiveTrueOrderByCreatedAtDesc(Long restaurantId, Pageable pageable);
    
    Page<Review> findByUserUserIdAndIsActiveTrueOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    long countByUserUserIdAndIsActiveTrue(Long userId);
    
    Page<Review> findByIsActiveTrueOrderByCreatedAtDesc(Pageable pageable);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.restaurant.restaurantId = :restaurantId AND r.isActive = true")
    BigDecimal findAverageRatingByRestaurantId(@Param("restaurantId") Long restaurantId);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.restaurant.restaurantId = :restaurantId AND r.isActive = true")
    Long countByRestaurantId(@Param("restaurantId") Long restaurantId);
    
    Boolean existsByUserUserIdAndRestaurantRestaurantId(Long userId, Long restaurantId);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.isActive = true AND r.createdAt >= :startOfDay AND r.createdAt < :endOfDay")
    long countTodayReviews(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
    
    Long countByUserUserId(Long userId);
    Long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.isActive = true")
    BigDecimal findAverageRating();
    
    Page<Review> findByRatingBetween(Integer minRating, Integer maxRating, Pageable pageable);
    Page<Review> findByIsActive(Boolean isActive, Pageable pageable);
    
    List<Review> findByUserUserIdOrderByCreatedAtDesc(Long userId);
    
    @Query("SELECT r.rating, COUNT(r) FROM Review r WHERE r.isActive = true GROUP BY r.rating ORDER BY r.rating")
    List<Object[]> findRatingDistribution();
    
    @Query("SELECT r FROM Review r JOIN FETCH r.user JOIN FETCH r.restaurant")
    Page<Review> findAllWithUserAndRestaurant(Pageable pageable);
    
    @Query("SELECT r FROM Review r JOIN FETCH r.user JOIN FETCH r.restaurant WHERE r.rating BETWEEN :minRating AND :maxRating")
    Page<Review> findByRatingBetweenWithUserAndRestaurant(@Param("minRating") Integer minRating, @Param("maxRating") Integer maxRating, Pageable pageable);
    
    @Query("SELECT r FROM Review r JOIN FETCH r.user JOIN FETCH r.restaurant WHERE r.isActive = :isActive")
    Page<Review> findByIsActiveWithUserAndRestaurant(@Param("isActive") Boolean isActive, Pageable pageable);
}