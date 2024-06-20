package com.sparta.mat_dil_admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserProfileRequestDto {
    @NotBlank(message = "아이디를 입력해 주세요.")
    @Size(min = 4, max = 10, message = "ID는 4글자 이상, 10글자 이하여야 합니다.")
    @Pattern(regexp = "^[a-z0-9]*$", message = "영문 소문자와 숫자만 입력 가능합니다.")
    private String accountId;

    @NotBlank(message = "이름을 입력해 주세요.")
    private String name;

    @NotBlank(message = "이메일을 입력해 주세요.")
    @Email(message = "이메일 형식을 입력해 주세요.")
    private String email;

    private String intro;
}
