package com.fooddelivery.userservice.dto.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String fullName;
    private String street;
    private String city;
    private String zipCode;
    private String country;
}