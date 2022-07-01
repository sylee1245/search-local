package com.test.searchlocal.application.rest.exception;

import com.test.searchlocal.application.rest.exception.code.EnumErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 예외 처리 핸들러
 */
@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    /**
     * Bad request
     *
     * @param e
     * @return
     */
    @ExceptionHandler({
            IllegalStateException.class, IllegalArgumentException.class,
            TypeMismatchException.class, HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequestException(Exception e) {
        log.debug("Bad request exception occurred: {}", e.getMessage(), e);
        return ErrorResponse.toResponseEntity(EnumErrorType.BAD_REQUEST);
    }

    /**
     * Bad request (Method argument not valid)
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(ConstraintViolationException e) {
        log.debug("Bad request argument exception occurred: {}", e.getMessage(), e);
        final List<FieldError> fieldErrorList = e.getConstraintViolations().stream().map(
                t -> {
                    String propertyPath = t.getPropertyPath().toString();
                    return FieldError.builder().fieldName(propertyPath.substring(propertyPath.lastIndexOf('.') + 1))
                            .errorMessage(t.getMessageTemplate()).build();
                }
        ).collect(Collectors.toList());

        return ErrorResponse.toResponseEntity(EnumErrorType.BAD_REQUEST, fieldErrorList);
    }

    /**
     * Custom
     *
     * @param e
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public final ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.warn("Custom exception occurred: {}", e.getMessage(), e);
        return ErrorResponse.toResponseEntity(e.getErrCode());
    }

    /**
     * Internal Server
     *
     * @param e
     * @return
     */
    @ExceptionHandler({Exception.class, RuntimeException.class})
    public final ResponseEntity<ErrorResponse> handleAllException(Exception e) {
        log.error("Unexpected exception occurred: {}", e.getMessage(), e);
        return ErrorResponse.toResponseEntity(EnumErrorType.INTERNAL_SERVER_ERROR);
    }
}