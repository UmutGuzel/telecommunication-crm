package com.gygy.contractservice.core.exception.result;
import java.util.List;

public class ValidationExceptionResult extends ExceptionResult{
    List<String> errors;
    public ValidationExceptionResult(List<String> errors) {
        super("Validation Error");
        this.errors = errors;
    }
}
