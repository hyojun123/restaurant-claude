package com.claudetest.restaurantapi.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RestaurantFilterDto {
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Double radius = 2.0;
    private List<String> categories;
    private String priceRange;
    private Double minRating;
    private Integer minReviewCount;
    private Boolean parkingAvailable;
    private Boolean currentlyOpen;
    private String sortBy = "distance"; // distance, rating, reviewCount, name
    private String sortDirection = "asc"; // asc, desc
    private Integer page = 0;
    private Integer size = 20;
}