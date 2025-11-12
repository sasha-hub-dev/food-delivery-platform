package com.fooddelivery.userservice.dto.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String fullName;
    private String street;
    private String city;
    private String zipCode;
    private String country;
}