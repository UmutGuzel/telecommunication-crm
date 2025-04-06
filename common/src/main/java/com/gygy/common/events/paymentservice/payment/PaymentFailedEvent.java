package com.gygy.common.events.paymentservice.payment;

import com.gygy.common.events.base.BaseEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class PaymentFailedEvent extends BaseEvent {
    // Manuel setter metodları

    private UUID billId;
    private UUID paymentId;
    private String errorMessage;

}
