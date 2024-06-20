package com.sparta.mat_dil_admin.repository;

import com.sparta.mat_dil_admin.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
