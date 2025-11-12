package com.fooddelivery.orderservice.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private String status;
    private LocalDateTime orderDate;
    private Long userId;
    private Long restaurantId;
    private Integer totalPrice;
    private List<OrderItemResponse> orderItems;
    private PaymentResponse payment;
}