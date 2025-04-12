package com.gygy.planservice.exception;

import org.springframework.http.HttpStatus;
import com.gygy.common.exception.BaseException;

/**
 * Exception thrown when category ID is null
 */
public class CategoryIdNullException extends BaseException {
    public CategoryIdNullException() {
        super("Category ID is required", HttpStatus.BAD_REQUEST, "CATEGORY_ID_NULL");
    }
}