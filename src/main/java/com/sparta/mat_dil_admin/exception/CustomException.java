package com.sparta.mat_dil_admin.exception;

import com.sparta.mat_dil_admin.enums.ErrorType;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private String result;
    private ErrorType errorType;

    public CustomException(ErrorType errorType) {
        this.result = "ERROR";
        this.errorType = errorType;
    }
}