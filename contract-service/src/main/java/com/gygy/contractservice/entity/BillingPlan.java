package com.gygy.contractservice.entity;

import com.gygy.contractservice.model.enums.BillingCycleType;
import com.gygy.contractservice.model.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
    @ManyToOne
    @JoinColumn(name = "contract_detail_id", nullable = false)
    private ContractDetail contractDetail;

    @ManyToMany
    @JoinTable(
            name = "billingPlan_discount",
            joinColumns = @JoinColumn(name = "billing_plan_id"),
            inverseJoinColumns = @JoinColumn(name = "discount_id"))
    private List<Discount> discounts;

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
