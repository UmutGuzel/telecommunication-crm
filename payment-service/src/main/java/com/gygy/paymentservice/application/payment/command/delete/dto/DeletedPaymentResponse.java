package com.gygy.paymentservice.application.payment.command.delete.dto;

import com.gygy.paymentservice.domain.entity.payment.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeletedPaymentResponse {
    private UUID paymentId;
    private LocalDateTime deletedAt;
    private PaymentStatus status;
}
