package com.test.searchlocal.application.rest.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Builder
@Getter
@ToString
public class FieldError implements Serializable {
    private final String fieldName;
    private final String errorMessage;
}