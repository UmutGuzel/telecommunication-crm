package com.gygy.planservice.exception;

import com.gygy.planservice.dto.response.ErrorResponse;
import com.gygy.common.exception.GlobalExceptionHandler;
import com.gygy.common.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;

@RestControllerAdvice
public class PlanServiceExceptionHandler extends GlobalExceptionHandler {

        @ExceptionHandler(CategoryNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleCategoryNotFoundException(
                        CategoryNotFoundException ex, WebRequest request) {
                ErrorResponse errorResponse = new ErrorResponse(
                                LocalDateTime.now(),
                                ex.getMessage(),
                                request.getDescription(false));
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler({ CategoryRequestNullException.class, CategoryNameNullException.class,
                        CategoryIdNullException.class })
        public ResponseEntity<ErrorResponse> handleCategoryValidationExceptions(
                        BaseException ex, WebRequest request) {
                ErrorResponse errorResponse = new ErrorResponse(
                                LocalDateTime.now(),
                                ex.getMessage(),
                                request.getDescription(false));
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler({ ContractRequestNullException.class, ContractIdNullException.class,
                        PlanIdNullException.class, PlanRequestNullException.class })
        public ResponseEntity<ErrorResponse> handleContractValidationExceptions(
                        BaseException ex, WebRequest request) {
                ErrorResponse errorResponse = new ErrorResponse(
                                LocalDateTime.now(),
                                ex.getMessage(),
                                request.getDescription(false));
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(PlanNotFoundException.class)
        public ResponseEntity<ErrorResponse> handlePlanNotFoundException(
                        PlanNotFoundException ex, WebRequest request) {
                ErrorResponse errorResponse = new ErrorResponse(
                                LocalDateTime.now(),
                                ex.getMessage(),
                                request.getDescription(false));
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(ContractNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleContractNotFoundException(
                        ContractNotFoundException ex, WebRequest request) {
                ErrorResponse errorResponse = new ErrorResponse(
                                LocalDateTime.now(),
                                ex.getMessage(),
                                request.getDescription(false));
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
}