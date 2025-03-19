package domain.exception.bill;

import domain.entity.bill.BillStatus;

public class BillStatusException extends RuntimeException {

    public BillStatusException(BillStatus currentStatus, BillStatus newStatus) {
        super("Invalid status transition: " + currentStatus + " -> " + newStatus);
    }

    public BillStatusException(String message) {
        super(message);
    }

}