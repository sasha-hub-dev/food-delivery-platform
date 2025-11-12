package com.fooddelivery.restaurantservice.service;

import com.fooddelivery.restaurantservice.dto.CreateDishRequest;
import com.fooddelivery.restaurantservice.dto.DishDto;
import java.util.List;

public interface DishService {
    List<DishDto> getDishesByRestaurant(Long restaurantId);
    DishDto getDishById(Long restaurantId, Long dishId);
    DishDto createDish(Long restaurantId, CreateDishRequest request);
    DishDto updateDish(Long restaurantId, Long dishId, CreateDishRequest request);
    void deleteDish(Long restaurantId, Long dishId);
}