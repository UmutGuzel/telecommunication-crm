package com.gygy.common.events.paymentservice.payment;

import com.gygy.common.events.base.BaseEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;


@Setter
@Getter
@NoArgsConstructor
public class PaymentSuccessEvent extends BaseEvent {
    private UUID billId;
    private BigDecimal paidAmount;

}
