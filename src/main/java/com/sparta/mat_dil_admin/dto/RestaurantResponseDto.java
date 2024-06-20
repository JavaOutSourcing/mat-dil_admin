package com.sparta.mat_dil_admin.dto;

import com.sparta.mat_dil_admin.entity.Restaurant;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RestaurantResponseDto {
    private Long id;
    private Long userId;
    private String restaurantName;
    private String description;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

    public RestaurantResponseDto(Restaurant restaurant){
        this.id = restaurant.getId();
        this.userId = restaurant.getUser().getId();
        this.restaurantName = restaurant.getRestaurantName();
        this.description = restaurant.getDescription();
        this.createAt = restaurant.getCreatedAt();
        this.modifiedAt = restaurant.getModifiedAt();
    }
}
