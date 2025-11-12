package com.fooddelivery.restaurantservice.controller;

import com.fooddelivery.restaurantservice.dto.CreateRestaurantRequest;
import com.fooddelivery.restaurantservice.dto.RestaurantDto;
import com.fooddelivery.restaurantservice.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<Page<RestaurantDto>> getAllRestaurants(
            @RequestParam(required = false) String cuisine,
            @RequestParam(required = false) String name,
            Pageable pageable) {
        return ResponseEntity.ok(restaurantService.getAllRestaurants(cuisine, name, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDto> getRestaurantById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getRestaurantById(id));
    }

    @GetMapping("/{id}/dishes")
    public ResponseEntity<RestaurantDto> getRestaurantWithDishes(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getRestaurantWithDishes(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestaurantDto> createRestaurant(@RequestBody CreateRestaurantRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.createRestaurant(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestaurantDto> updateRestaurant(
            @PathVariable Long id,
            @RequestBody CreateRestaurantRequest request) {
        return ResponseEntity.ok(restaurantService.updateRestaurant(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }
}