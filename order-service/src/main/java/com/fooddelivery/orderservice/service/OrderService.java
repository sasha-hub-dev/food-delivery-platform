package com.fooddelivery.orderservice.service;

import com.fooddelivery.orderservice.dto.request.CreateOrderRequest;
import com.fooddelivery.orderservice.dto.response.OrderResponse;
import com.fooddelivery.orderservice.dto.request.UpdateOrderStatusRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    OrderResponse placeOrder(CreateOrderRequest request);
    OrderResponse getOrderById(Long orderId, Long userId);
    List<OrderResponse> getUserOrders(Long userId);
    Page<OrderResponse> getAllOrders(Pageable pageable);
    OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request);
    List<OrderResponse> getRestaurantOrders(Long restaurantId);
}