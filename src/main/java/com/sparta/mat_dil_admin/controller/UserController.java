package com.sparta.mat_dil_admin.controller;

import com.sparta.mat_dil_admin.dto.*;
import com.sparta.mat_dil_admin.enums.ResponseStatus;
import com.sparta.mat_dil_admin.security.UserDetailsImpl;
import com.sparta.mat_dil_admin.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    //유저 전체 조회
    @GetMapping
    public ResponseEntity<ResponseDataDto<List<UserResponseDto>>> getAllUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<UserResponseDto> userResponseDtoList = userService.getAllUser(userDetails.getUser());

        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.ALL_USERS_FETCH_SUCCESS, userResponseDtoList));
    }

    //유저 권한 변경
    @PatchMapping("/{userId}/role")
    public ResponseEntity<ResponseDataDto<UserResponseDto>> updateUserRole(@PathVariable Long userId,
                                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserRoleUpdateResponse userRole = userService.updateUserRole(userId, userDetails.getUser());
        ResponseStatus responseStatus = userRole.isAdmin() ? ResponseStatus.UPDATE_USER_ADMIN_SUCCESS : ResponseStatus.UPDATE_USER_CONSUMER_SUCCESS;

        return ResponseEntity.ok(new ResponseDataDto<>(responseStatus, userRole.getUserResponseDto()));
    }


    //특정 회원 정보 수정
    @PutMapping("/{userId}")
    public ResponseEntity<ResponseDataDto<UserProfileResponseDto>> updateUser(@PathVariable Long userId, @Valid @RequestBody UserProfileRequestDto userProfileRequestDto,
                                                                             @AuthenticationPrincipal UserDetailsImpl userDetails){
        UserProfileResponseDto userProfileResponseDto = userService.updateUser(userId, userDetails.getUser(), userProfileRequestDto);

        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.PROFILE_UPDATE_SUCCESS, userProfileResponseDto));
    }

    //특정 회원 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseMessageDto> deleteUser(@PathVariable Long userId,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails){
        userService.deleteUser(userId, userDetails.getUser());

        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.USER_DELETE_SUCCESS));
    }

    //특정 회원 차단
    @PatchMapping("/{userId}/block")
    public ResponseEntity<ResponseDataDto<UserProfileResponseDto>> blockUser(@PathVariable Long userId,
                                                                             @AuthenticationPrincipal UserDetailsImpl userDetails){
        UserProfileResponseDto responseDto = userService.blockUser(userId, userDetails.getUser());

        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.USER_BLOCK_SUCCESS, responseDto));
    }

    //공지글 등록
    @PostMapping("/announcements")
    public ResponseEntity<ResponseDataDto<AnnouncementResponseDto>> createAnnounce(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                                   @RequestBody AnnouncementRequestDto requestDto){
        AnnouncementResponseDto responseDto = userService.createAnnounce(userDetails.getUser(), requestDto);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.ANNOUNCEMENT_POST_CREATE_SUCCESS, responseDto));
    }

}
