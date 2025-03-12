package com.gygy.userservice.core.pipelines.Exception;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationException extends RuntimeException {
    private List<String> errors;

    public ValidationException(List<String> errors) {
        this.errors = errors;
    }
}
