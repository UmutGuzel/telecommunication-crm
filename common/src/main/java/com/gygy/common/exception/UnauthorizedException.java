package com.gygy.common.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BaseException {

    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;
    private static final String ERROR_CODE = "UNAUTHORIZED";

    public UnauthorizedException(String message) {
        super(message, STATUS, ERROR_CODE);
    }

    public UnauthorizedException() {
        this("Authentication required");
    }
}