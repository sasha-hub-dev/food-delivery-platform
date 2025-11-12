package com.fooddelivery.restaurantservice.dto;

import lombok.Data;

@Data
public class DishDto {
    private Long id;
    private String name;
    private String description;
    private Integer price;
    private String imageUrl;
    private Long restaurantId;
}