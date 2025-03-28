package com.gygy.paymentservice.domain.exception.bill;

import java.util.UUID;

public class BillNotFoundException extends RuntimeException {
    public BillNotFoundException(UUID billId) {
        super("Bill not found with ID: " + billId);
    }

    public BillNotFoundException(UUID customerId, String message) {
        super("For Customer ID: " + customerId + " - " + message);
    }
}