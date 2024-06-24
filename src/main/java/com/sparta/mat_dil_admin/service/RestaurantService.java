package com.sparta.mat_dil_admin.service;

import com.sparta.mat_dil_admin.dto.CommentsResponseDto;
import com.sparta.mat_dil_admin.dto.OrderResponseDto;
import com.sparta.mat_dil_admin.dto.RestaurantResponseDto;
import com.sparta.mat_dil_admin.entity.*;
import com.sparta.mat_dil_admin.enums.ErrorType;
import com.sparta.mat_dil_admin.exception.CustomException;
import com.sparta.mat_dil_admin.repository.CommentRepository;
import com.sparta.mat_dil_admin.repository.FoodRepository;
import com.sparta.mat_dil_admin.repository.OrderRepository;
import com.sparta.mat_dil_admin.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;
    private final CommentRepository commentRepository;
    private final FoodRepository foodRepository;

    //음식점 전체 조회
    public List<RestaurantResponseDto> getAllRestaurants(User user) {
        validateAdminUser(user);
        List<Restaurant> restaurantList = restaurantRepository.findAll();

        return restaurantList.stream().map(RestaurantResponseDto::new).toList();
    }

    //주문 전체 조회
    public List<OrderResponseDto> getAllOrders(User user) {

        validateAdminUser(user);
        List<Order> orders = orderRepository.findAll();

        return orders.stream().map(OrderResponseDto::new).toList();
    }

    //댓글 전체 조회
    public List<CommentsResponseDto> getAllComments(User user) {
        validateAdminUser(user);
        List<Comment> comments = commentRepository.findAll();
        return comments.stream().map(CommentsResponseDto::new).toList();
    }

    //특정 음식 삭제
    @Transactional
    public void deleteFood(Long restaurantId, Long foodId, User user) {
        validateAdminUser(user);
        validateRestaurant(restaurantId);
        Food food = findFood(foodId);

        foodRepository.delete(food);
    }

    //특정 음식점 삭제
    @Transactional
    public void deleteRestaurant(Long restaurantId, User user) {
        validateAdminUser(user);
        Restaurant restaurant = validateRestaurant(restaurantId);
        restaurantRepository.delete(restaurant);
    }

    //특정 댓글 삭제
    @Transactional
    public void deleteComment(Long restaurantId, Long commentId, User user) {
        validateAdminUser(user);
        validateRestaurant(restaurantId);
        Comment comment = validateComment(commentId);
        commentRepository.delete(comment);
    }

    //특정 음식점 상단 고정
    @Transactional
    public RestaurantResponseDto announceTop(Long restaurantId, User user) {
        validateAdminUser(user);
        Restaurant restaurant = validateRestaurant(restaurantId);
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        restaurantList.forEach(Restaurant::pinnedFalse);
        restaurant.pinnedTrue();
        return new RestaurantResponseDto(restaurant);
    }

    // 관리자 검증 및 탈퇴한 회원 검증
    private void validateAdminUser(User user) {
        if (user.getUserType() != UserType.ADMIN) {
            throw new CustomException(ErrorType.NO_AUTHENTICATION);
        }
        if (user.getUserStatus() == UserStatus.DEACTIVATE) {
            throw new CustomException(ErrorType.DEACTIVATE_USER);
        }
    }

    //음식 존재 검증
    private Food findFood(Long foodId) {
        return foodRepository.findById(foodId).orElseThrow(() ->
                new CustomException(ErrorType.NOT_FOUND_FOOD));
    }

    //음식점 존재 검증
    private Restaurant validateRestaurant(Long restaurantId) {
        return restaurantRepository.findById(restaurantId).orElseThrow(() ->
                new CustomException(ErrorType.NOT_FOUND_RESTAURANT));
    }

    //댓글 존재 검증
    private Comment validateComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new CustomException(ErrorType.NOT_FOUND_COMMENT));
    }


}
