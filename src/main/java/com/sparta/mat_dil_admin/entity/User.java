package com.sparta.mat_dil_admin.entity;

import com.sparta.mat_dil_admin.dto.UserProfileRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String intro;

    @Column
    private String refreshToken;

    @Column
    @Enumerated(value = EnumType.STRING)
    private UserType userType;

    @Column()
    @Enumerated(value = EnumType.STRING)
    private UserStatus userStatus;

    @ElementCollection
    private List<String> passwordHistory = new ArrayList<>();

    public boolean updateRole(UserType userType){
        if(userType.equals(UserType.ADMIN)){
            this.userType = UserType.CONSUMER;
            return false;
        }
        else{
            this.userType = UserType.ADMIN;
            return true;
        }
    }

    public void updateUserProfile(UserProfileRequestDto requestDto){
        this.accountId = requestDto.getAccountId();
        this.name = requestDto.getName();
        this.email = requestDto.getEmail();
        this.intro = requestDto.getIntro();
    }

    public void updateUserBlock(){
        this.userStatus = UserStatus.BLOCKED;
    }
}
