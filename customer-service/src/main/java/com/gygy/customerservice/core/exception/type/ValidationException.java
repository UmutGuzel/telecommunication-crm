package com.gygy.customerservice.core.exception.type;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ValidationException extends RuntimeException{
    private final List<String> errors;

    public ValidationException(List<String> errors) {
        this.errors = errors;
    }
}
