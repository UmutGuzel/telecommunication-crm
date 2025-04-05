package com.gygy.common.events.paymentservice.payment;

import com.gygy.common.events.base.BaseEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;


@NoArgsConstructor
public class PaymentSuccessEvent extends BaseEvent {
    private UUID billId;
    private UUID paymentId;
    private BigDecimal paidAmount;

    public void setBillId(UUID billId) {
        this.billId = billId;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public UUID getBillId() {
        return billId;
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }
}
