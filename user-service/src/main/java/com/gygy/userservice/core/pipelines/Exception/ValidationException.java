package com.gygy.userservice.core.pipelines.Exception;

import com.gygy.common.exception.BaseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class ValidationException extends BaseException {
    private final List<String> errors;

    public ValidationException(List<String> errors) {
        super("Validation failed", HttpStatus.BAD_REQUEST, "VALIDATION_ERROR");
        this.errors = errors;
    }
}
