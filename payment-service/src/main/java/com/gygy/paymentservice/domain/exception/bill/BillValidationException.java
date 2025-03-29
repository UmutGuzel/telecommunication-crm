package com.gygy.paymentservice.domain.exception.bill;

import com.gygy.paymentservice.domain.entity.bill.BillStatus;

public class BillValidationException extends RuntimeException {

    public BillValidationException(BillStatus currentStatus, BillStatus newStatus) {
        super("Invalid status transition: " + currentStatus + " -> " + newStatus);
    }

    public BillValidationException(String message) {
        super(message);
    }

}