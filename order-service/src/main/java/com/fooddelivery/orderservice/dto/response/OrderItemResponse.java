package com.fooddelivery.orderservice.dto.response;

import lombok.Data;

@Data
public class OrderItemResponse {
    private Long id;
    private Long dishId;
    private String dishName;
    private Integer quantity;
    private Integer price;
}