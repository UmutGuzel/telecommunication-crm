package com.gygy.contractservice.entity;

import com.gygy.contractservice.model.enums.BillingCycleType;
import com.gygy.contractservice.model.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="commitments")
@Getter
@Setter
@Builder
@AllArgsConstructor

public class Commitment {
    @Id
    @UuidGenerator
    private UUID id;

    private String name;

    @Column(name = "duration_months", nullable = false)
    private Integer durationMonths;
    @Column(name = "billing_day", nullable = false)
    private Integer billingDay;


    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "early_termination_fee", nullable = false)
    private Double earlyTerminationFee;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @Enumerated(EnumType.STRING)
    @Column(name = "cycle_type", nullable = false)
    private BillingCycleType cycleType;

    @ManyToOne
    @JoinColumn(name="contract_detail_id", nullable = false)
    private ContractDetail contractDetail;

    public Commitment() {

    }

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
