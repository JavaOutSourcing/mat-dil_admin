package com.sparta.mat_dil_admin.controller;

import com.sparta.mat_dil_admin.dto.*;
import com.sparta.mat_dil_admin.enums.ResponseStatus;
import com.sparta.mat_dil_admin.service.RestaurantService;
import com.sparta.mat_dil_admin.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.support.CustomSQLErrorCodesTranslation;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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

    // 전체 주문 조회
    @GetMapping("/orders")
    public ResponseEntity<ResponseDataDto<List<OrderResponseDto>>> getAllOrders(@AuthenticationPrincipal UserDetailsImpl userDetails){
        List<OrderResponseDto> responseDtoList = restaurantService.getAllOrders(userDetails.getUser());
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.ORDER_CHECK_SUCCESS, responseDtoList));
    }

    // 전체 댓글 조회
    @GetMapping("/comments")
    public ResponseEntity<ResponseDataDto<List<CommentsResponseDto>>> getAllComments(@AuthenticationPrincipal UserDetailsImpl userDetails){
        List<CommentsResponseDto> responseDtoList = restaurantService.getAllComments(userDetails.getUser());
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.COMMENTS_CHECK_SUCCESS, responseDtoList));
    }

    //특정 음식 삭제
    @DeleteMapping("/{restaurantId}/foods/{foodId}")
    public ResponseEntity<ResponseMessageDto> deleteFood(@PathVariable Long restaurantId, @PathVariable Long foodId,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails){
        restaurantService.deleteFood(restaurantId, foodId, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.FOOD_DELETE_SUCCESS));
    }

    //특정 음식점 삭제
    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<ResponseMessageDto> deleteRestaurant(@PathVariable Long restaurantId,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails){
        restaurantService.deleteRestaurant(restaurantId, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.RESTAURANT_DELETE_SUCCESS));
    }

    //특정 음식 삭제
    @DeleteMapping("/{restaurantId}/comments/{commentId}")
    public ResponseEntity<ResponseMessageDto> deleteComment(@PathVariable Long restaurantId, @PathVariable Long commentId,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails){
        restaurantService.deleteComment(restaurantId, commentId, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.FOOD_DELETE_SUCCESS));
    }

    //특정 음식점 상단 고정
    @PatchMapping("/{restaurantId}")
    public ResponseEntity<ResponseDataDto<RestaurantResponseDto>> announceTop(@PathVariable Long restaurantId,
                                                                              @AuthenticationPrincipal UserDetailsImpl userDetails){
        RestaurantResponseDto responseDto = restaurantService.announceTop(restaurantId, userDetails.getUser());
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.FOOD_TOP_SUCCESS, responseDto));
    }
}
