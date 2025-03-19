package com.gygy.customerservice.core.exception.type;

public class BusinessException extends RuntimeException
{
    public BusinessException(String message) {
        super(message);
    }
}