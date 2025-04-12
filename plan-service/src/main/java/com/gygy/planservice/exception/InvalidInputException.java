package com.gygy.planservice.exception;

import org.springframework.http.HttpStatus;

import com.gygy.common.exception.BaseException;

public class InvalidInputException extends BaseException {
    public InvalidInputException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "INVALID_INPUT");
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause, HttpStatus.BAD_REQUEST, "INVALID_INPUT");
    }
}