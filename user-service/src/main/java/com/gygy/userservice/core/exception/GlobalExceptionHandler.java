package com.gygy.userservice.core.exception;

import com.gygy.common.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.gygy.userservice.core.pipelines.Exception.ValidationException;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends com.gygy.common.exception.GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleServiceValidationException(
            ValidationException ex, HttpServletRequest request) {
        log.error("User service validation error: {}", ex.getMessage());

        Map<String, Object> details = new HashMap<>();
        details.put("validationErrors", ex.getErrors());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("USER_VALIDATION_ERROR")
                .message("User service validation failed")
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .details(details)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}