package com.fooddelivery.restaurantservice.dto;

import lombok.Data;

@Data
public class CreateDishRequest {
    private String name;
    private String description;
    private Integer price;
    private String imageUrl;
}