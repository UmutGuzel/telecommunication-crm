package com.gygy.paymentservice.application.payment.query.getbyid.dto;

import com.gygy.paymentservice.domain.entity.payment.PaymentMethod;
import com.gygy.paymentservice.domain.entity.payment.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetailResponse {
    private UUID paymentId;
    private UUID billId;
    private UUID customerId;
    private BigDecimal paidAmount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentDate;
}