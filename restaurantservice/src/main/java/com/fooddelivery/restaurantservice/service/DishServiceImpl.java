package com.fooddelivery.restaurantservice.service;

import com.fooddelivery.restaurantservice.dto.CreateDishRequest;
import com.fooddelivery.restaurantservice.dto.DishDto;
import com.fooddelivery.restaurantservice.entity.Dish;
import com.fooddelivery.restaurantservice.entity.Restaurant;
import com.fooddelivery.restaurantservice.mapper.DishMapper;
import com.fooddelivery.restaurantservice.repository.DishRepository;
import com.fooddelivery.restaurantservice.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final DishMapper dishMapper;

    @Override
    @Transactional(readOnly = true)
    public List<DishDto> getDishesByRestaurant(Long restaurantId) {
        return dishRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(dishMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DishDto getDishById(Long restaurantId, Long dishId) {
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new RuntimeException("Dish not found"));

        if (!dish.getRestaurant().getId().equals(restaurantId)) {
            throw new RuntimeException("Dish does not belong to the specified restaurant");
        }

        return dishMapper.toDto(dish);
    }

    @Override
    public DishDto createDish(Long restaurantId, CreateDishRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        Dish dish = dishMapper.toEntity(request);
        dish.setRestaurant(restaurant);

        Dish savedDish = dishRepository.save(dish);
        return dishMapper.toDto(savedDish);
    }

    @Override
    public DishDto updateDish(Long restaurantId, Long dishId, CreateDishRequest request) {
        Dish existingDish = dishRepository.findById(dishId)
                .orElseThrow(() -> new RuntimeException("Dish not found"));

        if (!existingDish.getRestaurant().getId().equals(restaurantId)) {
            throw new RuntimeException("Dish does not belong to the specified restaurant");
        }

        existingDish.setName(request.getName());
        existingDish.setDescription(request.getDescription());
        existingDish.setPrice(request.getPrice());
        existingDish.setImageUrl(request.getImageUrl());

        Dish updatedDish = dishRepository.save(existingDish);
        return dishMapper.toDto(updatedDish);
    }

    @Override
    public void deleteDish(Long restaurantId, Long dishId) {
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new RuntimeException("Dish not found"));

        if (!dish.getRestaurant().getId().equals(restaurantId)) {
            throw new RuntimeException("Dish does not belong to the specified restaurant");
        }

        dishRepository.delete(dish);
    }
}