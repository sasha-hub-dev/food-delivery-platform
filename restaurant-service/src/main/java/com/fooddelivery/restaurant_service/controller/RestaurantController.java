package com.fooddelivery.restaurant_service.controller;

import com.fooddelivery.restaurant_service.model.Dish;
import com.fooddelivery.restaurant_service.model.Restaurant;
import com.fooddelivery.restaurant_service.repository.DishRepository;
import com.fooddelivery.restaurant_service.repository.RestaurantRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@Tag(name = "Рестораны", description = "Управление ресторанами и меню")
public class RestaurantController {

    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;

    public RestaurantController(RestaurantRepository restaurantRepository, DishRepository dishRepository) {
        this.restaurantRepository = restaurantRepository;
        this.dishRepository = dishRepository;
    }

    @Operation(summary = "Получить список всех ресторанов")
    @GetMapping
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Operation(summary = "Создать новый ресторан")
    @PostMapping
    public Restaurant createRestaurant(@RequestBody Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Operation(summary = "Добавить блюдо в ресторан")
    @PostMapping("/{restaurantId}/dishes")
    public ResponseEntity<Dish> addDishToRestaurant(@PathVariable Long restaurantId, @RequestBody Dish dish) {
        return restaurantRepository.findById(restaurantId).map(restaurant -> {
            dish.setRestaurant(restaurant);
            return ResponseEntity.ok(dishRepository.save(dish));
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Получить меню ресторана")
    @GetMapping("/{restaurantId}/dishes")
    public List<Dish> getRestaurantMenu(@PathVariable Long restaurantId) {
        return dishRepository.findByRestaurantId(restaurantId);
    }
}