package com.sparta.mat_dil_admin.controller;

import com.sparta.mat_dil_admin.dto.ResponseDataDto;
import com.sparta.mat_dil_admin.dto.RestaurantResponseDto;
import com.sparta.mat_dil_admin.enums.ResponseStatus;
import com.sparta.mat_dil_admin.service.RestaurantService;
import com.sparta.mat_dil_admin.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
@RequestMapping("/admin/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    //음식점 전체 조회
    @GetMapping
    public ResponseEntity<ResponseDataDto<List<RestaurantResponseDto>>> getAllRestaurants(@AuthenticationPrincipal UserDetailsImpl userDetails){

        List<RestaurantResponseDto> responseDtoList = restaurantService.getAllRestaurants(userDetails.getUser());

        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.RESTAURANT_CHECK_SUCCESS, responseDtoList));
    }

}
