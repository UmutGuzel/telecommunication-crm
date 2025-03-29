package com.gygy.userservice.core.exception;

import com.gygy.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UserServiceException extends BaseException {

    private static final String ERROR_CODE_PREFIX = "USER_";

    protected UserServiceException(String message, HttpStatus status, String errorCode) {
        super(message, status, ERROR_CODE_PREFIX + errorCode);
    }

    public static UserServiceException accountLocked(String email) {
        return new UserServiceException(
                String.format("Account with email '%s' is locked", email),
                HttpStatus.FORBIDDEN,
                "ACCOUNT_LOCKED");
    }

    public static UserServiceException accountInactive(String email) {
        return new UserServiceException(
                String.format("Account with email '%s' is not activated", email),
                HttpStatus.FORBIDDEN,
                "ACCOUNT_INACTIVE");
    }

    public static UserServiceException invalidActivationToken(String token) {
        return new UserServiceException(
                "Invalid or expired activation token",
                HttpStatus.BAD_REQUEST,
                "INVALID_ACTIVATION_TOKEN");
    }

    public static UserServiceException invalidResetToken(String token) {
        return new UserServiceException(
                "Invalid or expired password reset token",
                HttpStatus.BAD_REQUEST,
                "INVALID_RESET_TOKEN");
    }
}