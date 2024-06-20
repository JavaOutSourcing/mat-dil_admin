package com.sparta.mat_dil_admin.dto;

import com.sparta.mat_dil_admin.entity.User;
import com.sparta.mat_dil_admin.entity.UserStatus;
import com.sparta.mat_dil_admin.entity.UserType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserProfileResponseDto {
    private Long id;
    private String accountId;
    private String email;
    private String intro;
    private UserType userType;
    private UserStatus userStatus;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public UserProfileResponseDto(User user) {
        this.id = user.getId();
        this.accountId = user.getAccountId();
        this.email = user.getEmail();
        this.intro = user.getIntro();
        this.userType = user.getUserType();
        this.userStatus = user.getUserStatus();
        this.createdAt = user.getCreatedAt();
        this.modifiedAt = user.getModifiedAt();
    }

}
