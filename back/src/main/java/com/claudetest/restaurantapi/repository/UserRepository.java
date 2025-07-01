package com.claudetest.restaurantapi.repository;

import com.claudetest.restaurantapi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    Boolean existsByEmail(String email);
    
    Optional<User> findBySocialTypeAndSocialId(User.SocialType socialType, String socialId);
    
    Page<User> findByIsActiveTrue(Pageable pageable);
    
    Page<User> findByIsActiveTrueAndNicknameContaining(String nickname, Pageable pageable);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = true")
    long countActiveUsers();
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = true AND u.createdAt >= :startOfDay AND u.createdAt < :endOfDay")
    long countTodaySignups(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
    
    Page<User> findByNicknameContainingIgnoreCase(String nickname, Pageable pageable);
    Page<User> findByIsActive(Boolean isActive, Pageable pageable);
    
    Long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT FUNCTION('DATE_FORMAT', u.createdAt, '%Y-%m') as month, COUNT(u) as userCount FROM User u GROUP BY FUNCTION('DATE_FORMAT', u.createdAt, '%Y-%m') ORDER BY month DESC")
    java.util.List<Object[]> findMonthlyUserStats();
}