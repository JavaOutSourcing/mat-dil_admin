package com.sparta.mat_dil_admin.repository;

import com.sparta.mat_dil_admin.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
