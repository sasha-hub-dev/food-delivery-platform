package com.fooddelivery.orderservice.repository;

import com.fooddelivery.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    List<Order> findByRestaurantId(Long restaurantId);

    List<Order> findByStatus(String status);

    @Query("SELECT o FROM Order o WHERE o.id = :orderId AND o.userId = :userId")
    Order findByIdAndUserId(@Param("orderId") Long orderId, @Param("userId") Long userId);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems LEFT JOIN FETCH o.payment WHERE o.id = :id")
    Order findByIdWithDetails(@Param("id") Long id);
}