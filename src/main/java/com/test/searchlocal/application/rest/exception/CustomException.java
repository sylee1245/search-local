package com.test.searchlocal.application.rest.exception;

import com.test.searchlocal.application.rest.exception.code.EnumErrorType;
import lombok.Data;

/**
 * Client API 요청이 실패한 경우
 */
@Data
public class CustomException extends RuntimeException {
    private final EnumErrorType errCode;

    public CustomException(EnumErrorType errCode) {
        this.errCode = errCode;
    }

    public EnumErrorType getErrCode() {
        return this.errCode;
    }

}
