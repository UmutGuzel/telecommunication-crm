package domain.exception.payment;

import domain.entity.payment.PaymentMethod;
import domain.entity.payment.PaymentStatus;

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