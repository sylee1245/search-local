package com.test.searchlocal.application.rest.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.test.searchlocal.application.rest.exception.code.EnumErrorType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 에러 응답
 */
@Getter
@Builder
public class ErrorResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<FieldError> error;

    public static ResponseEntity<ErrorResponse> toResponseEntity(EnumErrorType errorType) {
        return toResponseEntity(errorType, null);
    }

    public static ResponseEntity<ErrorResponse> toResponseEntity(EnumErrorType errorType, List<FieldError> fieldErr) {
        return ResponseEntity
                .status(errorType.getStatus())
                .body(ErrorResponse.builder()
                        .status(errorType.getStatus().value())
                        .code(errorType.getCode())
                        .message(errorType.getMessage())
                        .error(fieldErr)
                        .build()
                );
    }

}