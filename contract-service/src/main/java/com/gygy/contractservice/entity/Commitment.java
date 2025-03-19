package com.gygy.contractservice.entity;

import com.gygy.contractservice.model.enums.BillingCycleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="commitments")
@Getter
@Setter
public class Commitment {
    @Id
    @UuidGenerator
    private UUID id;

    private String name;

    @Column(name = "duration_months", nullable = false)
    private Integer durationMonths;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "early_termination_fee", nullable = false)
    private BigDecimal earlyTerminationFee;

    @Column(name = "status", nullable = false)
    private String status;

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
