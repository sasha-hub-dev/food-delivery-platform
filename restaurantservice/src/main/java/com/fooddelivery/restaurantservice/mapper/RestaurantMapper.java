package com.fooddelivery.restaurantservice.mapper;

import com.fooddelivery.restaurantservice.dto.RestaurantDto;
import com.fooddelivery.restaurantservice.dto.CreateRestaurantRequest;
import com.fooddelivery.restaurantservice.entity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = DishMapper.class)
public interface RestaurantMapper {

    RestaurantMapper INSTANCE = Mappers.getMapper(RestaurantMapper.class);

    Restaurant toEntity(CreateRestaurantRequest request);

    RestaurantDto toDto(Restaurant restaurant);
}