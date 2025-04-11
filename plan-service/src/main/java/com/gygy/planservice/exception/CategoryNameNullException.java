package com.gygy.planservice.exception;

import org.springframework.http.HttpStatus;
import com.gygy.common.exception.BaseException;

/**
 * Exception thrown when category name is null
 */
public class CategoryNameNullException extends BaseException {
    public CategoryNameNullException() {
        super("Category name is required", HttpStatus.BAD_REQUEST, "CATEGORY_NAME_NULL");
    }
}