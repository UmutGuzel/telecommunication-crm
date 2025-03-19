package domain.exception.bill;

import java.math.BigDecimal;

public class BillAmountException extends RuntimeException {

    public BillAmountException(String message) {
        super(message);
    }

    public BillAmountException(BigDecimal amount) {
        super("Invalid bill amount: " + amount + ". Bill amount must be greater than 0.");
    }

    public BillAmountException(BigDecimal totalAmount, BigDecimal paidAmount) {
        super("Payment amount (" + paidAmount + ") cannot be greater than bill amount (" + totalAmount + ").");
    }
}
