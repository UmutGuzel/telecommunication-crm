package domain.exception.payment;

import domain.entity.payment.PaymentStatus;

import java.util.UUID;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(UUID paymentId) {
        super("Payment not found with ID: " + paymentId);
    }

    public PaymentNotFoundException(UUID customerId, String message) {
        super("For Customer ID: " + customerId + " - " + message);
    }

    public PaymentNotFoundException(PaymentStatus paymentStatus) {
        super("For Payment Status: " + paymentStatus + "there is no result.");
    }
}