package com.fooddelivery.orderservice.mapper;

import com.fooddelivery.orderservice.dto.request.OrderItemRequest;
import com.fooddelivery.orderservice.dto.response.OrderItemResponse;
import com.fooddelivery.orderservice.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    OrderItem toEntity(OrderItemRequest request);

    OrderItemResponse toDto(OrderItem orderItem);
}