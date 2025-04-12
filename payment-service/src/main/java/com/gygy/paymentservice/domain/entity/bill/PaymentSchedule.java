package com.gygy.paymentservice.domain.entity.bill;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "payment_schedules")
public class PaymentSchedule {
    @Id
    @UuidGenerator
    private UUID id;

    private UUID contractId;
    private UUID customerId;
    private BigDecimal amount;
    private LocalDate nextBillingDate;
    private Integer remainingMonths;

    //TODO: new
}
