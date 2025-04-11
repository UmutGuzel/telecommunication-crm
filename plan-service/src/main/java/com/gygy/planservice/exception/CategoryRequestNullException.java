package com.gygy.planservice.exception;

import org.springframework.http.HttpStatus;
import com.gygy.common.exception.BaseException;

/**
 * Exception thrown when a category request object is null
 */
public class CategoryRequestNullException extends BaseException {
    public CategoryRequestNullException() {
        super("Category request data is required", HttpStatus.BAD_REQUEST, "CATEGORY_REQUEST_NULL");
    }
}