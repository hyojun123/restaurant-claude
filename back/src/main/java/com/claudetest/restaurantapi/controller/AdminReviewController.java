package com.claudetest.restaurantapi.controller;

import com.claudetest.restaurantapi.entity.Review;
import com.claudetest.restaurantapi.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {

    private final ReviewRepository reviewRepository;

    @GetMapping
    public ResponseEntity<?> getReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer minRating,
            @RequestParam(required = false) Integer maxRating,
            @RequestParam(required = false) Boolean isActive) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Review> reviews;

        if (minRating != null && maxRating != null) {
            reviews = reviewRepository.findByRatingBetweenWithUserAndRestaurant(minRating, maxRating, pageable);
        } else if (isActive != null) {
            reviews = reviewRepository.findByIsActiveWithUserAndRestaurant(isActive, pageable);
        } else {
            reviews = reviewRepository.findAllWithUserAndRestaurant(pageable);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("reviews", reviews.getContent().stream().map(review -> {
            Map<String, Object> reviewData = new HashMap<>();
            reviewData.put("reviewId", review.getReviewId());
            reviewData.put("rating", review.getRating());
            reviewData.put("content", review.getContent());
            reviewData.put("tags", review.getTags());
            reviewData.put("likeCount", review.getLikeCount());
            reviewData.put("isActive", review.getIsActive());
            reviewData.put("createdAt", review.getCreatedAt());
            reviewData.put("updatedAt", review.getUpdatedAt());
            
            // User 정보 안전하게 추가
            if (review.getUser() != null) {
                Map<String, Object> userData = new HashMap<>();
                userData.put("userId", review.getUser().getUserId());
                userData.put("nickname", review.getUser().getNickname());
                userData.put("email", review.getUser().getEmail());
                reviewData.put("user", userData);
            }
            
            // Restaurant 정보 안전하게 추가
            if (review.getRestaurant() != null) {
                Map<String, Object> restaurantData = new HashMap<>();
                restaurantData.put("restaurantId", review.getRestaurant().getRestaurantId());
                restaurantData.put("name", review.getRestaurant().getName());
                restaurantData.put("category", review.getRestaurant().getCategory());
                reviewData.put("restaurant", restaurantData);
            }
            
            return reviewData;
        }).toList());
        response.put("totalElements", reviews.getTotalElements());
        response.put("totalPages", reviews.getTotalPages());
        response.put("currentPage", reviews.getNumber());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReview(@PathVariable Long id) {
        Optional<Review> review = reviewRepository.findById(id);
        return review.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        if (reviewRepository.existsById(id)) {
            reviewRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateReviewStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> status) {
        Optional<Review> review = reviewRepository.findById(id);
        if (review.isPresent()) {
            Review r = review.get();
            r.setIsActive(status.get("isActive"));
            reviewRepository.save(r);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/statistics")
    public ResponseEntity<?> getReviewStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalReviews", reviewRepository.count());
        stats.put("averageRating", reviewRepository.findAverageRating());
        stats.put("ratingDistribution", reviewRepository.findRatingDistribution());
        return ResponseEntity.ok(stats);
    }
}