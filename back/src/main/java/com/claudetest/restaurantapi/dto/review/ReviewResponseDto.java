package com.claudetest.restaurantapi.dto.review;

import com.claudetest.restaurantapi.entity.Review;

import java.time.LocalDateTime;

public class ReviewResponseDto {
    
    private Long reviewId;
    private Long userId;
    private String userNickname;
    private String userProfileImage;
    private Long restaurantId;
    private String restaurantName;
    private Integer rating;
    private String content;
    private String[] photos;
    private String tags;
    private Integer likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ReviewResponseDto() {}

    public ReviewResponseDto(Review review) {
        this.reviewId = review.getReviewId();
        this.userId = review.getUser().getUserId();
        this.userNickname = review.getUser().getNickname();
        this.userProfileImage = review.getUser().getProfileImageUrl();
        this.restaurantId = review.getRestaurant().getRestaurantId();
        this.restaurantName = review.getRestaurant().getName();
        this.rating = review.getRating();
        this.content = review.getContent();
        this.photos = review.getPhotos();
        this.tags = review.getTags();
        this.likeCount = review.getLikeCount();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
    }

    // Getters and Setters
    public Long getReviewId() { return reviewId; }
    public void setReviewId(Long reviewId) { this.reviewId = reviewId; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getUserNickname() { return userNickname; }
    public void setUserNickname(String userNickname) { this.userNickname = userNickname; }
    
    public String getUserProfileImage() { return userProfileImage; }
    public void setUserProfileImage(String userProfileImage) { this.userProfileImage = userProfileImage; }
    
    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
    
    public String getRestaurantName() { return restaurantName; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }
    
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String[] getPhotos() { return photos; }
    public void setPhotos(String[] photos) { this.photos = photos; }
    
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    
    public Integer getLikeCount() { return likeCount; }
    public void setLikeCount(Integer likeCount) { this.likeCount = likeCount; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}