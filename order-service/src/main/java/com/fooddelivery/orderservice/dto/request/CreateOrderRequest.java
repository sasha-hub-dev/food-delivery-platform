package com.fooddelivery.orderservice.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class CreateOrderRequest {
    private Long userId;
    private Long restaurantId;
    private String paymentMethod;
    private List<OrderItemRequest> items;
}