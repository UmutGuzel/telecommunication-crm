package com.gygy.paymentservice.domain.exception.payment;

import com.gygy.paymentservice.domain.entity.payment.PaymentMethod;
import com.gygy.paymentservice.domain.entity.payment.PaymentStatus;

public class PaymentValidationException extends RuntimeException {
    public PaymentValidationException(String message) {
        super(message);
    }

    public PaymentValidationException(PaymentStatus currentStatus, PaymentStatus newStatus) {
        super("Invalid payment status transition: " + currentStatus + " -> " + newStatus);
    }

    public PaymentValidationException(PaymentMethod paymentMethod) {
        super("Invalid payment method: " + paymentMethod);
    }

}