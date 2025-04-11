package com.gygy.planservice.exception;

import org.springframework.http.HttpStatus;
import com.gygy.common.exception.BaseException;

/**
 * Exception thrown when contract ID is null
 */
public class ContractIdNullException extends BaseException {
    public ContractIdNullException() {
        super("Contract ID is required", HttpStatus.BAD_REQUEST, "CONTRACT_ID_NULL");
    }
}