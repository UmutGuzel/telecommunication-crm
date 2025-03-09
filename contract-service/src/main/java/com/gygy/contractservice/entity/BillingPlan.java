package com.gygy.contractservice.entity;

import com.gygy.contractservice.model.enums.BillingCycleType;
import com.gygy.contractservice.model.enums.PaymentMethod;
import com.gygy.contractservice.model.enums.ServiceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="billing_plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingPlan {
    @Id
    @UuidGenerator
    private UUID id;
    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "cycle_type", nullable = false)
    private BillingCycleType cycleType;

    @Column(name = "billing_day", nullable = false)
    private Integer billingDay;
    @Column(name = "payment_due_days", nullable = false)
    private Integer paymentDueDays;
    @Column(name = "base_amount", nullable = false)
    private BigDecimal baseAmount;
    @Column(name = "tax_rate", nullable = false)
    private BigDecimal taxRate;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;


    @Column(name = "status", nullable = false)
    private String status;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


}
