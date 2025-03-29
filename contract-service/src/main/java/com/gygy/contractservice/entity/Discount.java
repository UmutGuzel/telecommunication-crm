package com.gygy.contractservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gygy.contractservice.model.enums.BillingCycleType;
import com.gygy.contractservice.model.enums.DiscountType;
import com.gygy.contractservice.model.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="discount")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Discount {
    @Id
    @UuidGenerator
    private UUID id;
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name="billing_cycle_type")
    private BillingCycleType billingCycleType;


    @Enumerated(EnumType.STRING)
    @Column(name="discount_type")
    private DiscountType discountType;

    private double amount;
    private double percentage;

    @Column(nullable = false, columnDefinition = "varchar(255) default 'ACTIVE'")
    @Enumerated(EnumType.STRING)
    private Status status;

    private UUID customerId;
    private String description;


    @Column(name="start_date")
    private LocalDate startDate;
    @Column(name="end_date")
    private LocalDate endDate;
    @Column(name="created_at")
    private LocalDate createdAt;
    @Column(name="updated_at")
    private LocalDate updatedAt;
    @ManyToOne
    @JoinColumn(name="contract_detail_id",nullable = false)
    private ContractDetail contractDetail;
    @ManyToMany(mappedBy = "discounts")
    @JsonIgnore
    private List<BillingPlan> billingPlans;

}
