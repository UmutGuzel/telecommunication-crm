package com.gygy.paymentservice.application.payment.command.update.dto;

import com.gygy.paymentservice.domain.entity.payment.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatedPaymentStatusResponse {
    private UUID paymentId;
    private UUID billId;
    private UUID customerId;
    private PaymentStatus paymentStatus;
}
