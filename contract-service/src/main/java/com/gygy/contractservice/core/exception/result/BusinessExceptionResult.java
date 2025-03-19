package com.gygy.contractservice.core.exception.result;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessExceptionResult extends ExceptionResult {
    private String errorMessage;
    public BusinessExceptionResult(String errorMessage) {
        super("BusinessException");
        this.errorMessage = errorMessage;
    }
}
