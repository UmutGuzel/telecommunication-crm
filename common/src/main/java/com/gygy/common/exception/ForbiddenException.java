package com.gygy.common.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends BaseException {

    private static final HttpStatus STATUS = HttpStatus.FORBIDDEN;
    private static final String ERROR_CODE = "FORBIDDEN";

    public ForbiddenException(String message) {
        super(message, STATUS, ERROR_CODE);
    }

    public ForbiddenException() {
        this("Access denied");
    }

    public static ForbiddenException missingRole(String requiredRole) {
        return new ForbiddenException(String.format("Required role not found: %s", requiredRole));
    }

    public static ForbiddenException missingPermission(String requiredPermission) {
        return new ForbiddenException(String.format("Required permission not found: %s", requiredPermission));
    }
}