package com.gygy.contractservice.core.exception.result;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotFoundExceptionResult extends ExceptionResult{
    private String errorMessage;
    public NotFoundExceptionResult(String errorMessage) {
        super("BusinessException");
        this.errorMessage = errorMessage;
    }
}
