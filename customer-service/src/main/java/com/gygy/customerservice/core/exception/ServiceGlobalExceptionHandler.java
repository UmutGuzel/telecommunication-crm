package com.gygy.customerservice.core.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gygy.common.exception.GlobalExceptionHandler;
import com.gygy.customerservice.core.exception.response.BusinessExceptionResponse;
import com.gygy.customerservice.core.exception.type.BusinessException;
import com.gygy.customerservice.core.exception.type.ValidationException;


@RestControllerAdvice
public class ServiceGlobalExceptionHandler extends GlobalExceptionHandler {

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<List<String>> validationException(ValidationException e) {
        return new ResponseEntity<>(e.getErrors(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<BusinessExceptionResponse> businessException(BusinessException e) {
        BusinessExceptionResponse response = new BusinessExceptionResponse();
        response.setErrors(e.getErrors());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}