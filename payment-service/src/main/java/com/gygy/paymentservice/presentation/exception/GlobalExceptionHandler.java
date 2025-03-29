package com.gygy.paymentservice.presentation.exception;

import com.gygy.paymentservice.domain.exception.bill.BillValidationException;
import com.gygy.paymentservice.presentation.dto.ErrorResponse;
import com.gygy.paymentservice.domain.exception.bill.BillAmountException;
import com.gygy.paymentservice.domain.exception.bill.BillNotFoundException;
import com.gygy.paymentservice.domain.exception.payment.PaymentAmountException;
import com.gygy.paymentservice.domain.exception.payment.PaymentNotFoundException;
import com.gygy.paymentservice.domain.exception.payment.PaymentValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BillNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBillNotFound(BillNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                LocalDateTime.now(),
                "BILL_NOT_FOUND"
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(BillAmountException.class)
    public ResponseEntity<ErrorResponse> handleBillAmount(BillAmountException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                LocalDateTime.now(),
                "INVALID_BILL_AMOUNT"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(BillValidationException.class)
    public ResponseEntity<ErrorResponse> handleBillValidation(BillValidationException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                LocalDateTime.now(),
                "BILL_STATUS_ERROR"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePaymentNotFound(PaymentNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                LocalDateTime.now(),
                "PAYMENT_NOT_FOUND"
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(PaymentAmountException.class)
    public ResponseEntity<ErrorResponse> handlePaymentAmount(PaymentAmountException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                LocalDateTime.now(),
                "INVALID_PAYMENT_AMOUNT"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(PaymentValidationException.class)
    public ResponseEntity<ErrorResponse> handlePaymentValidation(PaymentValidationException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                LocalDateTime.now(),
                "PAYMENT_VALIDATION_ERROR"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                "An unexpected error occurred. Please try again later.",
                LocalDateTime.now(),
                "INTERNAL_SERVER_ERROR"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}

