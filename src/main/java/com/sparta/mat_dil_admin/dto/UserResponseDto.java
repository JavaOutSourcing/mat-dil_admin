package com.sparta.mat_dil_admin.dto;

import com.sparta.mat_dil_admin.entity.User;
import com.sparta.mat_dil_admin.entity.UserType;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private Long id;
    private String accountId;
    private String email;
    private UserType userType;

    public UserResponseDto(User user){
        this.id = user.getId();
        this.accountId = user.getAccountId();
        this.email = user.getEmail();
        this.userType = user.getUserType();
    }
}
