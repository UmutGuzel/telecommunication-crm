package com.gygy.customerservice.core.exception.type;

import java.util.ArrayList;
import java.util.List;

public class BusinessException extends RuntimeException
{
    private final List<String> errors;

    public BusinessException(String message) {
        super(message);
        this.errors = new ArrayList<>();
        this.errors.add(message);
    }

    public BusinessException(List<String> errors) {
        super(String.join(", ", errors));
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}