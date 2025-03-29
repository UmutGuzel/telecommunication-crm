package com.gygy.paymentservice.application.bill.query.getbyid.dto;

import com.gygy.paymentservice.domain.entity.bill.BillStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillDetailResponse {
    private UUID billId;
    private UUID customerId;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private LocalDate dueDate;
    private BillStatus status;
    //private LocalDateTime createdAt;
    //private LocalDateTime updatedAt;
    //TODO: hangileri olmalı??
}