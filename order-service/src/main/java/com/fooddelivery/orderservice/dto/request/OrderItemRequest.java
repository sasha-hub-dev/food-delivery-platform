package com.fooddelivery.orderservice.dto.request;

import lombok.Data;

@Data
public class OrderItemRequest {
    private Long dishId;
    private String dishName;
    private Integer quantity;
    private Integer price;
}