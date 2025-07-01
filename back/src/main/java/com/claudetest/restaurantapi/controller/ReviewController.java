package com.claudetest.restaurantapi.controller;

import com.claudetest.restaurantapi.dto.review.ReviewRequest;
import com.claudetest.restaurantapi.dto.review.ReviewResponseDto;
import com.claudetest.restaurantapi.security.UserPrincipal;
import com.claudetest.restaurantapi.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Review", description = "리뷰 관련 API")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    @Operation(summary = "리뷰 작성", description = "새로운 리뷰를 작성합니다.")
    public ResponseEntity<?> createReview(
            @Valid @RequestBody ReviewRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            ReviewResponseDto review = reviewService.createReview(request, userPrincipal.getId());
            return ResponseEntity.ok(review);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = "맛집별 리뷰 조회", description = "특정 맛집의 리뷰를 조회합니다.")
    public ResponseEntity<Page<ReviewResponseDto>> getReviewsByRestaurant(
            @PathVariable Long restaurantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewResponseDto> reviews = reviewService.getReviewsByRestaurant(restaurantId, pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "사용자별 리뷰 조회", description = "특정 사용자의 리뷰를 조회합니다.")
    public ResponseEntity<Page<ReviewResponseDto>> getReviewsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewResponseDto> reviews = reviewService.getReviewsByUser(userId, pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/my")
    @Operation(summary = "내 리뷰 조회", description = "로그인한 사용자의 리뷰를 조회합니다.")
    public ResponseEntity<Page<ReviewResponseDto>> getMyReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewResponseDto> reviews = reviewService.getReviewsByUser(userPrincipal.getId(), pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/my/count")
    @Operation(summary = "내 리뷰 개수", description = "로그인한 사용자의 리뷰 개수를 조회합니다.")
    public ResponseEntity<Map<String, Long>> getMyReviewCount(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        long count = reviewService.countReviewsByUser(userPrincipal.getId());
        Map<String, Long> result = new HashMap<>();
        result.put("count", count);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{reviewId}")
    @Operation(summary = "리뷰 상세 조회", description = "특정 리뷰의 상세 정보를 조회합니다.")
    public ResponseEntity<ReviewResponseDto> getReview(@PathVariable Long reviewId) {
        Optional<ReviewResponseDto> review = reviewService.getReviewById(reviewId);
        return review.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{reviewId}")
    @Operation(summary = "리뷰 수정", description = "작성한 리뷰를 수정합니다.")
    public ResponseEntity<?> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            ReviewResponseDto review = reviewService.updateReview(reviewId, request, userPrincipal.getId());
            return ResponseEntity.ok(review);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "리뷰 삭제", description = "작성한 리뷰를 삭제합니다.")
    public ResponseEntity<?> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            reviewService.deleteReview(reviewId, userPrincipal.getId());
            Map<String, String> response = new HashMap<>();
            response.put("message", "리뷰가 삭제되었습니다.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}