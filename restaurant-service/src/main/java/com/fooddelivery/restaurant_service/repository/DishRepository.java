package com.fooddelivery.restaurant_service.repository;

import com.fooddelivery.restaurant_service.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByRestaurantId(Long restaurantId);
}