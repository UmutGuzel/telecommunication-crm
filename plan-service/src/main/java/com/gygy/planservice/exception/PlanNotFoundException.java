package com.gygy.planservice.exception;

import org.springframework.http.HttpStatus;

import com.gygy.common.exception.BaseException;

public class PlanNotFoundException extends BaseException {
    public PlanNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "PLAN_NOT_FOUND");
    }

    public PlanNotFoundException(String message, Throwable cause) {
        super(message, cause, HttpStatus.NOT_FOUND, "PLAN_NOT_FOUND");
    }
}