package com.gygy.common.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends BaseException {

    private static final HttpStatus STATUS = HttpStatus.CONFLICT;
    private static final String ERROR_CODE = "CONFLICT";

    public ConflictException(String message) {
        super(message, STATUS, ERROR_CODE);
    }

    public static ConflictException duplicateValue(String resourceType, String field, Object value) {
        return new ConflictException(
                String.format("%s with %s '%s' already exists", resourceType, field, value));
    }
}