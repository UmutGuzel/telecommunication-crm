package domain.exception.payment;

import java.math.BigDecimal;

public class PaymentAmountException extends RuntimeException {
    public PaymentAmountException(String message) {
        super(message);
    }

    public PaymentAmountException(BigDecimal amount) {
        super("Invalid payment amount: " + amount + ". Payment amount must be greater than 0.");
    }

    public PaymentAmountException(BigDecimal paymentAmount, BigDecimal billAmount) {
        super("Payment amount (" + paymentAmount + ") cannot be greater than remaining bill amount (" + billAmount + ").");
    }
}
