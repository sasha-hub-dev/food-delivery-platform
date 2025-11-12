package com.fooddelivery.restaurantservice.mapper;

import com.fooddelivery.restaurantservice.dto.DishDto;
import com.fooddelivery.restaurantservice.dto.CreateDishRequest;
import com.fooddelivery.restaurantservice.entity.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DishMapper {

    DishMapper INSTANCE = Mappers.getMapper(DishMapper.class);

    @Mapping(target = "restaurantId", source = "restaurant.id")
    DishDto toDto(Dish dish);

    Dish toEntity(CreateDishRequest request);
}