package com.fooddelivery.restaurantservice.dto;

import lombok.Data;

@Data
public class CreateRestaurantRequest {
    private String name;
    private String cuisine;
    private String address;
}