package com.claudetest.restaurantapi.dto;

import com.claudetest.restaurantapi.entity.Restaurant;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RestaurantResponseDto {
    
    private Long restaurantId;
    private String name;
    private String category;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String phone;
    private String businessHours;
    private String priceRange;
    private Boolean parkingAvailable;
    private BigDecimal averageRating;
    private Integer reviewCount;
    private Double distanceKm;
    private LocalDateTime createdAt;

    public RestaurantResponseDto() {}

    public RestaurantResponseDto(Restaurant restaurant) {
        this.restaurantId = restaurant.getRestaurantId();
        this.name = restaurant.getName();
        this.category = restaurant.getCategory();
        this.address = restaurant.getAddress();
        this.latitude = restaurant.getLatitude();
        this.longitude = restaurant.getLongitude();
        this.phone = restaurant.getPhone();
        this.businessHours = restaurant.getBusinessHours();
        this.priceRange = restaurant.getPriceRange() != null ? restaurant.getPriceRange().getDescription() : null;
        this.parkingAvailable = restaurant.getParkingAvailable();
        this.averageRating = restaurant.getAverageRating();
        this.reviewCount = restaurant.getReviewCount();
        this.createdAt = restaurant.getCreatedAt();
    }

    public static RestaurantResponseDto from(Restaurant restaurant) {
        return new RestaurantResponseDto(restaurant);
    }

    // Getters and Setters
    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    
    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getBusinessHours() { return businessHours; }
    public void setBusinessHours(String businessHours) { this.businessHours = businessHours; }
    
    public String getPriceRange() { return priceRange; }
    public void setPriceRange(String priceRange) { this.priceRange = priceRange; }
    
    public Boolean getParkingAvailable() { return parkingAvailable; }
    public void setParkingAvailable(Boolean parkingAvailable) { this.parkingAvailable = parkingAvailable; }
    
    public BigDecimal getAverageRating() { return averageRating; }
    public void setAverageRating(BigDecimal averageRating) { this.averageRating = averageRating; }
    
    public Integer getReviewCount() { return reviewCount; }
    public void setReviewCount(Integer reviewCount) { this.reviewCount = reviewCount; }
    
    public Double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(Double distanceKm) { this.distanceKm = distanceKm; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}