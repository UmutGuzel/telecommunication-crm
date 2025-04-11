package com.gygy.planservice.exception;

import org.springframework.http.HttpStatus;

import com.gygy.common.exception.BaseException;

public class ContractNotFoundException extends BaseException {
    public ContractNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "CONTRACT_NOT_FOUND");
    }

    public ContractNotFoundException(String message, Throwable cause) {
        super(message, cause, HttpStatus.NOT_FOUND, "CONTRACT_NOT_FOUND");
    }
}