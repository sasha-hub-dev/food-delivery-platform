package com.fooddelivery.orderservice.mapper;

import com.fooddelivery.orderservice.dto.response.PaymentResponse;
import com.fooddelivery.orderservice.entity.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentResponse toDto(Payment payment);
}