package com.sparta.mat_dil_admin.service;

import com.sparta.mat_dil_admin.dto.UserProfileRequestDto;
import com.sparta.mat_dil_admin.dto.UserProfileResponseDto;
import com.sparta.mat_dil_admin.dto.UserResponseDto;
import com.sparta.mat_dil_admin.dto.UserRoleUpdateResponse;
import com.sparta.mat_dil_admin.entity.User;
import com.sparta.mat_dil_admin.entity.UserStatus;
import com.sparta.mat_dil_admin.entity.UserType;
import com.sparta.mat_dil_admin.enums.ErrorType;
import com.sparta.mat_dil_admin.exception.CustomException;
import com.sparta.mat_dil_admin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    //유저 전체 조회
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUser(User user) {
        validateAdminUser(user);
        List<User> userList = userRepository.findAll();
        return userList.stream().map(UserResponseDto::new).toList();
    }

    //유저 권한 변경
    @Transactional
    public UserRoleUpdateResponse updateUserRole(Long userId, User currentUser) {
        validateAdminUser(currentUser);
        User user = findUserById(userId);
        boolean isAdmin = user.updateRole(currentUser.getUserType());
        return new UserRoleUpdateResponse(new UserResponseDto(user), isAdmin);

    }

    //유저 프로필 수정
    @Transactional
    public UserProfileResponseDto updateUser(Long userId, User currentUser, UserProfileRequestDto userProfileRequestDto) {
        validateAdminUser(currentUser);
        User user = findUserById(userId);

        user.updateUserProfile(userProfileRequestDto);

        //동일 아이디 검증
        validateUserId(user.getAccountId());
        //동일 이메일 검증
        validateUserEmail(user.getEmail());

        return new UserProfileResponseDto(user);
    }

    //유저 삭제
    @Transactional
    public void deleteUser(Long userId, User currentUser) {
        validateAdminUser(currentUser);
        User user = findUserById(userId);
        User deleteUser = userRepository.findById(userId).orElse(null);
    }

    //유저 차단
    @Transactional
    public UserProfileResponseDto blockUser(Long userId, User currentUser) {
        validateAdminUser(currentUser);
        User user = findUserById(userId);
        if (user.getUserStatus().equals(UserStatus.BLOCKED)) {
            throw new CustomException(ErrorType.USER_ALREADY_BLOCKED);
        }
        user.updateUserBlock();
        return new UserProfileResponseDto(user);
    }

    // 유저 존재 검증
    private User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorType.NOT_FOUND_USER));
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

    //동일 이메일 검증
    private void validateUserEmail(String email) {
        Optional<User> findUser = userRepository.findByEmail(email);

        if(findUser.isPresent()){
            throw new CustomException(ErrorType.DUPLICATE_EMAIL);
        }
    }

    //동일 아이디 검증
    private void validateUserId(String id) {
        Optional<User> findUser = userRepository.findByAccountId(id);

        if(findUser.isPresent()){
            throw new CustomException(ErrorType.DUPLICATE_ACCOUNT_ID);
        }
    }

}
