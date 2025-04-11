package com.gygy.planservice.exception;

import org.springframework.http.HttpStatus;
import com.gygy.common.exception.BaseException;

/**
 * Exception thrown when a contract request object is null
 */
public class ContractRequestNullException extends BaseException {
    public ContractRequestNullException() {
        super("Contract request data is required", HttpStatus.BAD_REQUEST, "CONTRACT_REQUEST_NULL");
    }
}