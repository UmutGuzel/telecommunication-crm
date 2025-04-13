package com.gygy.planservice.exception;

import org.springframework.http.HttpStatus;
import com.gygy.common.exception.BaseException;

/**
 * Exception thrown when plan ID is null
 */
public class PlanIdNullException extends BaseException {
    public PlanIdNullException() {
        super("Plan ID is required", HttpStatus.BAD_REQUEST, "PLAN_ID_NULL");
    }
}