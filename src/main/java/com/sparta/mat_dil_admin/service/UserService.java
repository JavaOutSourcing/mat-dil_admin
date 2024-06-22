package com.sparta.mat_dil_admin.service;

import com.sparta.mat_dil_admin.dto.*;
import com.sparta.mat_dil_admin.entity.AnnouncementPost;
import com.sparta.mat_dil_admin.entity.User;
import com.sparta.mat_dil_admin.entity.UserStatus;
import com.sparta.mat_dil_admin.entity.UserType;
import com.sparta.mat_dil_admin.enums.ErrorType;
import com.sparta.mat_dil_admin.exception.CustomException;
import com.sparta.mat_dil_admin.repository.AnnouncementPostRepository;
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
    private final AnnouncementPostRepository announcementPostRepository;

    //유저 전체 조회
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUser(User currentUser) {
        validateAdminUser(currentUser);
        List<User> userList = userRepository.findAll();
        return userList.stream().map(UserResponseDto::new).toList();
    }

    //유저 권한 변경
    @Transactional
    public UserRoleUpdateResponse updateUserRole(Long userId, User currentUser) {
        User user = validateUser(userId);
        validateAdminUser(currentUser);
        boolean isAdmin = user.updateRole(user.getUserType());
        return new UserRoleUpdateResponse(new UserResponseDto(user), isAdmin);

    }

    //유저 프로필 수정
    @Transactional
    public UserProfileResponseDto updateUser(Long userId, User currentUser, UserProfileRequestDto userProfileRequestDto) {
        User user = validateUser(userId);
        validateAdminUser(currentUser);

        //동일 아이디 검증
        validateUserId(userProfileRequestDto.getAccountId());
        //동일 이메일 검증
        validateUserEmail(userProfileRequestDto.getEmail());

        user.updateUserProfile(userProfileRequestDto);

        return new UserProfileResponseDto(user);
    }

    //유저 삭제
    @Transactional
    public void deleteUser(Long userId, User currentUser) {
        User user = validateUser(userId);
        validateAdminUser(currentUser);
        userRepository.delete(user);
    }

    //유저 차단
    @Transactional
    public UserProfileResponseDto blockUser(Long userId, User currentUser) {
        User user = validateUser(userId);
        validateAdminUser(currentUser);
        if (user.getUserStatus().equals(UserStatus.BLOCKED)) {
            throw new CustomException(ErrorType.USER_ALREADY_BLOCKED);
        }
        user.updateUserBlock();
        return new UserProfileResponseDto(user);
    }

    //공지글 게시
    @Transactional
    public AnnouncementResponseDto createAnnounce(User user, AnnouncementRequestDto requestDto) {
        validateAdminUser(user);
        validateUser(user.getId());
        AnnouncementPost announcementPost = new AnnouncementPost(requestDto);
        announcementPostRepository.save(announcementPost);

        return new AnnouncementResponseDto(announcementPost);
    }

    // 유저 존재 검증 및 탈퇴 유저 검증
    private User validateUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorType.NOT_FOUND_USER));
        if(user.getUserStatus().equals(UserStatus.DEACTIVATE)){
            throw new CustomException(ErrorType.DEACTIVATE_USER);
        }

        return user;
    }

    // 관리자 검증 및 탈퇴한 회원 검증
    private void validateAdminUser(User user) {
        if (!user.getUserType().equals(UserType.ADMIN)) {
            throw new CustomException(ErrorType.NO_AUTHENTICATION);
        }
        if (user.getUserStatus().equals(UserStatus.DEACTIVATE)) {
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
