package com.gygy.planservice.exception;

import com.gygy.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends BaseException {
    public CategoryNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "CATEGORY_NOT_FOUND");
    }

    public CategoryNotFoundException(String message, Throwable cause) {
        super(message, cause, HttpStatus.NOT_FOUND, "CATEGORY_NOT_FOUND");
    }
}