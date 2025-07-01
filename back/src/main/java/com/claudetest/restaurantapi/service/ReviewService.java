package com.claudetest.restaurantapi.service;

import com.claudetest.restaurantapi.dto.review.ReviewRequest;
import com.claudetest.restaurantapi.dto.review.ReviewResponseDto;
import com.claudetest.restaurantapi.entity.Restaurant;
import com.claudetest.restaurantapi.entity.Review;
import com.claudetest.restaurantapi.entity.User;
import com.claudetest.restaurantapi.repository.RestaurantRepository;
import com.claudetest.restaurantapi.repository.ReviewRepository;
import com.claudetest.restaurantapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Transactional
    public ReviewResponseDto createReview(ReviewRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        // 이미 리뷰를 작성했는지 확인
        if (reviewRepository.existsByUserUserIdAndRestaurantRestaurantId(userId, request.getRestaurantId())) {
            throw new RuntimeException("이미 이 맛집에 리뷰를 작성하셨습니다.");
        }

        Review review = new Review();
        review.setUser(user);
        review.setRestaurant(restaurant);
        review.setRating(request.getRating());
        review.setContent(request.getContent());
        review.setPhotos(request.getPhotos());
        review.setTags(request.getTags());
        review.setIsActive(true);

        Review savedReview = reviewRepository.save(review);

        // 맛집 평점 업데이트
        updateRestaurantRating(request.getRestaurantId());

        return new ReviewResponseDto(savedReview);
    }

    public Page<ReviewResponseDto> getReviewsByRestaurant(Long restaurantId, Pageable pageable) {
        return reviewRepository.findByRestaurantRestaurantIdAndIsActiveTrueOrderByCreatedAtDesc(restaurantId, pageable)
                .map(ReviewResponseDto::new);
    }

    public Page<ReviewResponseDto> getReviewsByUser(Long userId, Pageable pageable) {
        return reviewRepository.findByUserUserIdAndIsActiveTrueOrderByCreatedAtDesc(userId, pageable)
                .map(ReviewResponseDto::new);
    }

    public long countReviewsByUser(Long userId) {
        return reviewRepository.countByUserUserIdAndIsActiveTrue(userId);
    }

    public Page<ReviewResponseDto> getAllReviews(Pageable pageable) {
        return reviewRepository.findByIsActiveTrueOrderByCreatedAtDesc(pageable)
                .map(ReviewResponseDto::new);
    }

    public Optional<ReviewResponseDto> getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .filter(Review::getIsActive)
                .map(ReviewResponseDto::new);
    }

    @Transactional
    public ReviewResponseDto updateReview(Long reviewId, ReviewRequest request, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("리뷰 수정 권한이 없습니다.");
        }

        review.setRating(request.getRating());
        review.setContent(request.getContent());
        review.setPhotos(request.getPhotos());
        review.setTags(request.getTags());

        Review updatedReview = reviewRepository.save(review);

        // 맛집 평점 업데이트
        updateRestaurantRating(review.getRestaurant().getRestaurantId());

        return new ReviewResponseDto(updatedReview);
    }

    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("리뷰 삭제 권한이 없습니다.");
        }

        review.setIsActive(false);
        reviewRepository.save(review);

        // 맛집 평점 업데이트
        updateRestaurantRating(review.getRestaurant().getRestaurantId());
    }

    @Transactional
    private void updateRestaurantRating(Long restaurantId) {
        BigDecimal averageRating = reviewRepository.findAverageRatingByRestaurantId(restaurantId);
        Long reviewCount = reviewRepository.countByRestaurantId(restaurantId);

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        restaurant.setAverageRating(averageRating != null ? averageRating : BigDecimal.ZERO);
        restaurant.setReviewCount(reviewCount.intValue());

        restaurantRepository.save(restaurant);
    }
}