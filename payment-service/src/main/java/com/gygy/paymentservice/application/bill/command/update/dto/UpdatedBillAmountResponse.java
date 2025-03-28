package com.gygy.paymentservice.application.bill.command.update.dto;

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
public class UpdatedBillAmountResponse {
    private UUID billId;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
}
