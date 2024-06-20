package com.sparta.mat_dil_admin.service;

import com.sparta.mat_dil_admin.dto.RestaurantResponseDto;
import com.sparta.mat_dil_admin.entity.Restaurant;
import com.sparta.mat_dil_admin.entity.User;
import com.sparta.mat_dil_admin.entity.UserStatus;
import com.sparta.mat_dil_admin.entity.UserType;
import com.sparta.mat_dil_admin.enums.ErrorType;
import com.sparta.mat_dil_admin.exception.CustomException;
import com.sparta.mat_dil_admin.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    //음식점 전체 조회
    public List<RestaurantResponseDto> getAllRestaurants(User user) {
        validateAdminUser(user);
        List<Restaurant> restaurantList = restaurantRepository.findAll();

        return restaurantList.stream().map(RestaurantResponseDto::new).toList();
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
}
