package com.gygy.common.exception;

import org.springframework.http.HttpStatus;
import java.util.Map;
import lombok.Getter;

@Getter
public class ValidationException extends BaseException {
    private final Map<String, String> validationErrors;

    public ValidationException(Map<String, String> validationErrors) {
        super("Validation failed", HttpStatus.BAD_REQUEST, "VALIDATION_ERROR");
        this.validationErrors = validationErrors;
    }

    public static ValidationException withError(String field, String message) {
        Map<String, String> errors = Map.of(field, message);
        return new ValidationException(errors);
    }
}