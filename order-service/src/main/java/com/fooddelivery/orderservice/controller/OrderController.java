package com.fooddelivery.orderservice.controller;

import com.fooddelivery.orderservice.dto.request.CreateOrderRequest;
import com.fooddelivery.orderservice.dto.response.OrderResponse;
import com.fooddelivery.orderservice.dto.request.UpdateOrderStatusRequest;
import com.fooddelivery.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(request));
    }

    @GetMapping("/{orderId}")
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long orderId,
            @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId, userId));
    }

    @GetMapping("/user/{userId}")
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<OrderResponse>> getUserOrders(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getUserOrders(userId));
    }

    @GetMapping("/restaurant/{restaurantId}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponse>> getRestaurantOrders(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(orderService.getRestaurantOrders(restaurantId));
    }

    @GetMapping
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<OrderResponse>> getAllOrders(Pageable pageable) {
        return ResponseEntity.ok(orderService.getAllOrders(pageable));
    }

    @PutMapping("/{orderId}/status")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody UpdateOrderStatusRequest request) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, request));
    }
}