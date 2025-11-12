package com.fooddelivery.orderservice.mapper;

import com.fooddelivery.orderservice.dto.response.OrderResponse;
import com.fooddelivery.orderservice.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, PaymentMapper.class})
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderResponse toDto(Order order);
}