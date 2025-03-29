package com.gygy.common.events.paymentservice.payment;

import com.gygy.common.events.base.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSuccessEvent extends BaseEvent {

    private UUID billId;
    private UUID customerId;
    private BigDecimal paidAmount;
    private String paymentMethod;
}
