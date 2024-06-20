package com.sparta.mat_dil_admin.dto;

import lombok.Getter;

@Getter
public class UserRoleUpdateResponse {
    private UserResponseDto userResponseDto;
    private boolean isAdmin;

    public UserRoleUpdateResponse(UserResponseDto user, boolean isAdmin) {
        this.userResponseDto = user;
        this.isAdmin = isAdmin;
    }
}
