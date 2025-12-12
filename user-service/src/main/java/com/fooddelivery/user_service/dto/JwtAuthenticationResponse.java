package com.fooddelivery.user_service.dto;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer"; // Тип токена

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}