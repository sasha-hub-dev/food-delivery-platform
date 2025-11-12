package com.fooddelivery.restaurantservice.service;

import com.fooddelivery.restaurantservice.dto.CreateRestaurantRequest;
import com.fooddelivery.restaurantservice.dto.RestaurantDto;
import com.fooddelivery.restaurantservice.entity.Restaurant;
import com.fooddelivery.restaurantservice.mapper.RestaurantMapper;
import com.fooddelivery.restaurantservice.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<RestaurantDto> getAllRestaurants(String cuisine, String name, Pageable pageable) {
        Page<Restaurant> restaurants;

        if (cuisine != null && !cuisine.isEmpty()) {
            restaurants = restaurantRepository.findByCuisineContainingIgnoreCase(cuisine, pageable);
        } else if (name != null && !name.isEmpty()) {
            restaurants = restaurantRepository.findByNameContainingIgnoreCase(name, pageable);
        } else {
            restaurants = restaurantRepository.findAll(pageable);
        }

        return restaurants.map(restaurantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantDto getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + id));
        return restaurantMapper.toDto(restaurant);
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantDto getRestaurantWithDishes(Long id) {
        Restaurant restaurant = restaurantRepository.findByIdWithDishes(id);
        if (restaurant == null) {
            throw new RuntimeException("Restaurant not found with id: " + id);
        }
        return restaurantMapper.toDto(restaurant);
    }

    @Override
    public RestaurantDto createRestaurant(CreateRestaurantRequest request) {
        Restaurant restaurant = restaurantMapper.toEntity(request);
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toDto(savedRestaurant);
    }

    @Override
    public RestaurantDto updateRestaurant(Long id, CreateRestaurantRequest request) {
        Restaurant existingRestaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + id));

        existingRestaurant.setName(request.getName());
        existingRestaurant.setCuisine(request.getCuisine());
        existingRestaurant.setAddress(request.getAddress());

        Restaurant updatedRestaurant = restaurantRepository.save(existingRestaurant);
        return restaurantMapper.toDto(updatedRestaurant);
    }

    @Override
    public void deleteRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + id));
        restaurantRepository.delete(restaurant);
    }
}