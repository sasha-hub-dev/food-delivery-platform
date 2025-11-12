package com.fooddelivery.restaurantservice.repository;

import com.fooddelivery.restaurantservice.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Page<Restaurant> findByCuisineContainingIgnoreCase(String cuisine, Pageable pageable);

    Page<Restaurant> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<Restaurant> findByCuisineContainingIgnoreCase(String cuisine);

    List<Restaurant> findByNameContainingIgnoreCase(String name);

    @Query("SELECT DISTINCT r FROM Restaurant r LEFT JOIN FETCH r.dishes WHERE r.id = :id")
    Restaurant findByIdWithDishes(@Param("id") Long id);
}