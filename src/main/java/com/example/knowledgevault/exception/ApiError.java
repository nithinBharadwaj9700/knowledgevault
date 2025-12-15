package com.example.knowledgevault.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter
public class ApiError {

    private String errorCode;
    private String message;
    private LocalDateTime timestamp;

    public ApiError(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
