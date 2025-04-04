package com.gygy.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {

    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;
    private static final String ERROR_CODE = "RESOURCE_NOT_FOUND";

    public NotFoundException(String message) {
        super(message, STATUS, ERROR_CODE);
    }

    public static NotFoundException forResource(String resourceType, Object identifier) {
        return new NotFoundException(String.format("%s with identifier '%s' not found", resourceType, identifier));
    }
}