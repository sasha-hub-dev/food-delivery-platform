package com.fooddelivery.restaurantservice.service;

import com.fooddelivery.restaurantservice.dto.CreateRestaurantRequest;
import com.fooddelivery.restaurantservice.dto.RestaurantDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestaurantService {
    Page<RestaurantDto> getAllRestaurants(String cuisine, String name, Pageable pageable);
    RestaurantDto getRestaurantById(Long id);
    RestaurantDto getRestaurantWithDishes(Long id);
    RestaurantDto createRestaurant(CreateRestaurantRequest request);
    RestaurantDto updateRestaurant(Long id, CreateRestaurantRequest request);
    void deleteRestaurant(Long id);
}