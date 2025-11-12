package com.fooddelivery.orderservice.service;

import com.fooddelivery.orderservice.dto.request.CreateOrderRequest;
import com.fooddelivery.orderservice.dto.request.OrderItemRequest;
import com.fooddelivery.orderservice.dto.response.OrderResponse;
import com.fooddelivery.orderservice.dto.request.UpdateOrderStatusRequest;
import com.fooddelivery.orderservice.entity.Order;
import com.fooddelivery.orderservice.entity.OrderItem;
import com.fooddelivery.orderservice.entity.Payment;
import com.fooddelivery.orderservice.mapper.OrderMapper;
import com.fooddelivery.orderservice.repository.OrderRepository;
import com.fooddelivery.orderservice.repository.OrderItemRepository;
import com.fooddelivery.orderservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponse placeOrder(CreateOrderRequest request) {
        log.info("Placing order for user: {}, restaurant: {}", request.getUserId(), request.getRestaurantId());

        Integer totalPrice = calculateTotalPrice(request.getItems());

        Order order = Order.builder()
                .status("PLACED")
                .orderDate(LocalDateTime.now())
                .userId(request.getUserId())
                .restaurantId(request.getRestaurantId())
                .totalPrice(totalPrice)
                .build();


        Order savedOrder = orderRepository.save(order);


        List<OrderItem> orderItems = createOrderItems(request.getItems(), savedOrder);
        orderItemRepository.saveAll(orderItems);

        Payment payment = createPayment(savedOrder, request.getPaymentMethod(), totalPrice);
        paymentRepository.save(payment);

        savedOrder.setOrderItems(orderItems);
        savedOrder.setPayment(payment);

        log.info("Order placed successfully with ID: {}", savedOrder.getId());
        return orderMapper.toDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId, Long userId) {
        log.info("Getting order by ID: {} for user: {}", orderId, userId);

        Order order = orderRepository.findByIdWithDetails(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }

        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getUserOrders(Long userId) {
        log.info("Getting all orders for user: {}", userId);
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        log.info("Getting all orders (admin)");
        Page<Order> orders = orderRepository.findAll(pageable);
        return orders.map(orderMapper::toDto);
    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request) {
        log.info("Updating order status for ID: {} to {}", orderId, request.getStatus());

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        order.setStatus(request.getStatus());
        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toDto(updatedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getRestaurantOrders(Long restaurantId) {
        log.info("Getting all orders for restaurant: {}", restaurantId);
        List<Order> orders = orderRepository.findByRestaurantId(restaurantId);
        return orders.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    private Integer calculateTotalPrice(List<OrderItemRequest> items) {
        return items.stream()
                .mapToInt(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    private List<OrderItem> createOrderItems(List<OrderItemRequest> itemRequests, Order order) {
        return itemRequests.stream()
                .map(itemRequest -> OrderItem.builder()
                        .dishId(itemRequest.getDishId())
                        .dishName(itemRequest.getDishName())
                        .quantity(itemRequest.getQuantity())
                        .price(itemRequest.getPrice())
                        .order(order)
                        .build())
                .toList();
    }

    private Payment createPayment(Order order, String paymentMethod, Integer amount) {
        return Payment.builder()
                .method(paymentMethod)
                .amount(amount)
                .status("PAID")
                .order(order)
                .build();
    }
}