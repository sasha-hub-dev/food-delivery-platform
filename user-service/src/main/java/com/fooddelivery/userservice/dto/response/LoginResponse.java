package com.fooddelivery.userservice.dto.response;

import lombok.Data;

@Data
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private UserResponse user;
}