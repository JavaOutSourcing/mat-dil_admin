package com.sparta.mat_dil_admin.dto;

import com.sparta.mat_dil_admin.entity.Order;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderResponseDto {
    private String accountId;
    private String name;
    private int totalPrice;
    private LocalDateTime createAt;

    public OrderResponseDto(Order order){
        this.accountId = order.getUser().getAccountId();
        this.name = order.getUser().getName();
        this.totalPrice = order.getTotal_price();
        this.createAt = order.getCreatedAt();
    }
}
