package com.gygy.paymentservice.domain.entity.bill;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
public class PaymentSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private UUID contractId;
    private UUID customerId;
    private BigDecimal amount;
    private LocalDate nextBillingDate;
    private Integer remainingMonths;

    //TODO: new
}
