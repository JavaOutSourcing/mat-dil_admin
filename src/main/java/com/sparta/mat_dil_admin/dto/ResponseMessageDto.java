package com.sparta.mat_dil_admin.dto;

import com.sparta.mat_dil_admin.enums.ResponseStatus;
import lombok.Getter;

@Getter
public class ResponseMessageDto {
    private int status;
    private String message;

    public ResponseMessageDto(ResponseStatus status) {
        this.status = status.getHttpStatus().value();
        this.message = status.getMessage();
    }
}
