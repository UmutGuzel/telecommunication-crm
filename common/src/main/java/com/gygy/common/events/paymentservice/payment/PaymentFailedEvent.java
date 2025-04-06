package com.gygy.common.events.paymentservice.payment;

import com.gygy.common.events.base.BaseEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@NoArgsConstructor
public class PaymentFailedEvent extends BaseEvent {
    // Manuel setter metodları

    private UUID billId;
    private UUID paymentId;
    private String errorMessage;

    public UUID getPaymentId() {
        return paymentId;
    }

    public void setBillId(UUID billId) {
        this.billId = billId;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public UUID getBillId() {
        return billId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
