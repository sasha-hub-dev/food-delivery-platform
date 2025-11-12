package com.fooddelivery.restaurantservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dishes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Integer price;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Restaurant restaurant;
}