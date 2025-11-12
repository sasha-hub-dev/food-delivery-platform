package com.fooddelivery.restaurantservice.controller;

import com.fooddelivery.restaurantservice.dto.CreateDishRequest;
import com.fooddelivery.restaurantservice.dto.DishDto;
import com.fooddelivery.restaurantservice.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    @GetMapping
    public ResponseEntity<List<DishDto>> getDishesByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(dishService.getDishesByRestaurant(restaurantId));
    }

    @GetMapping("/{dishId}")
    public ResponseEntity<DishDto> getDishById(
            @PathVariable Long restaurantId,
            @PathVariable Long dishId) {
        return ResponseEntity.ok(dishService.getDishById(restaurantId, dishId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DishDto> createDish(
            @PathVariable Long restaurantId,
            @RequestBody CreateDishRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(dishService.createDish(restaurantId, request));
    }

    @PutMapping("/{dishId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DishDto> updateDish(
            @PathVariable Long restaurantId,
            @PathVariable Long dishId,
            @RequestBody CreateDishRequest request) {
        return ResponseEntity.ok(dishService.updateDish(restaurantId, dishId, request));
    }

    @DeleteMapping("/{dishId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDish(
            @PathVariable Long restaurantId,
            @PathVariable Long dishId) {
        dishService.deleteDish(restaurantId, dishId);
        return ResponseEntity.noContent().build();
    }
}