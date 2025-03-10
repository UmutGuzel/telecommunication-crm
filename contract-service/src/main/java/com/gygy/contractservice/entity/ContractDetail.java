package com.gygy.contractservice.entity;
import com.gygy.contractservice.model.enums.ContractDetailType;
import com.gygy.contractservice.model.enums.ContractStatus;
import com.gygy.contractservice.model.enums.ServiceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="contract_details")
@Getter
@Setter
public class ContractDetail {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ContractStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type", nullable = false)
    private ServiceType serviceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_detail_type", nullable = false)
    private ContractDetailType contractDetailType;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name="contract_id",nullable = false)
    private Contract contract;

    @OneToMany(mappedBy = "contractDetail")
    private Set<Commitment> commitments;

    @OneToMany(mappedBy = "contractDetail")
    private Set<BillingPlan> billingPlans;
    @OneToMany(mappedBy = "contractDetail")
    private Set<Discount> discounts;

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
