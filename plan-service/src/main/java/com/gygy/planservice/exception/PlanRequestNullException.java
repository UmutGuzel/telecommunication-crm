package com.gygy.planservice.exception;

import org.springframework.http.HttpStatus;
import com.gygy.common.exception.BaseException;

/**
 * Exception thrown when a plan request object is null
 */
public class PlanRequestNullException extends BaseException {
    public PlanRequestNullException() {
        super("Plan request data is required", HttpStatus.BAD_REQUEST, "PLAN_REQUEST_NULL");
    }
}