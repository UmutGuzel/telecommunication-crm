package com.gygy.paymentservice.application.payment.command.update.dto;

import com.gygy.paymentservice.domain.entity.payment.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatedPaymentMethodResponse {
    private UUID paymentId;
    private UUID billId;
    private UUID customerId;
    private PaymentMethod paymentMethod;
}
