package com.claudetest.restaurantapi.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDto {
    private Long totalUsers;
    private Long totalRestaurants;
    private Long totalReviews;
    private Long todayNewUsers;
    private Long todayNewReviews;
    private BigDecimal averageRating;
}