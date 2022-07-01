package com.test.searchlocal.application.rest.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 에러 처리시 예외 타입
 */
@Getter
@AllArgsConstructor
public enum EnumErrorType {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "001", "Bad request exception occurred"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "002", "Server error occurred. Please contact manager"),
    CLIENT_API_SERVER_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "003", "Server is unable to process client requests"),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}