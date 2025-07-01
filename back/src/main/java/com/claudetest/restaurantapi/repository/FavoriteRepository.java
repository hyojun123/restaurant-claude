package com.claudetest.restaurantapi.repository;

import com.claudetest.restaurantapi.entity.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    
    Page<Favorite> findByUserUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    Optional<Favorite> findByUserUserIdAndRestaurantRestaurantId(Long userId, Long restaurantId);
    
    Boolean existsByUserUserIdAndRestaurantRestaurantId(Long userId, Long restaurantId);
    
    void deleteByUserUserIdAndRestaurantRestaurantId(Long userId, Long restaurantId);
    
    Long countByUserUserId(Long userId);
    
    @Query("SELECT f FROM Favorite f JOIN FETCH f.restaurant WHERE f.user.userId = :userId ORDER BY f.createdAt DESC")
    List<Favorite> findByUserIdWithRestaurant(@Param("userId") Long userId);
}