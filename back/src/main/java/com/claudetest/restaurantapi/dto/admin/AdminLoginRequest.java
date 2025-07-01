package com.claudetest.restaurantapi.dto.admin;

import lombok.Data;

@Data
public class AdminLoginRequest {
    private String username;
    private String password;
}