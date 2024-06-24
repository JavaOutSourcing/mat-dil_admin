package com.sparta.mat_dil_admin.repository;

import com.sparta.mat_dil_admin.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {

}
