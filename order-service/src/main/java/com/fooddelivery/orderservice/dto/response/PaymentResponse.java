package com.fooddelivery.orderservice.dto.response;

import lombok.Data;

@Data
public class PaymentResponse {
    private Long id;
    private String method;
    private Integer amount;
    private String status;
}