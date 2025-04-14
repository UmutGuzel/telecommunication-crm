package com.gygy.contractservice.entity;

import com.gygy.contractservice.model.enums.BillingCycleType;
import com.gygy.contractservice.model.enums.PaymentMethod;
import com.gygy.contractservice.model.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "billing_plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillingPlan {
    @Id
    @UuidGenerator
    private UUID id;
    private String name;
    private String description;
    private UUID planId;

    @Enumerated(EnumType.STRING)
    @Column(name = "cycle_type")
    private BillingCycleType cycleType;

    @Column(name = "base_amount")
    private Integer baseAmount;
    @Column(name = "tax_rate")
    private Double taxRate;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "status")
    private Status status;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name = "contract_id")
    private Contract contract;

    @OneToMany(mappedBy = "billingPlan")
    private Set<Discount> discounts;

}
