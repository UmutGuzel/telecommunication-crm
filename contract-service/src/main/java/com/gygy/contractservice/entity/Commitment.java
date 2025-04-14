package com.gygy.contractservice.entity;

import com.gygy.contractservice.model.enums.BillingCycleType;
import com.gygy.contractservice.model.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "commitments")
@Getter
@Setter
@Builder
@AllArgsConstructor

public class Commitment {
    @Id
    @UuidGenerator
    private UUID id;

    private String name;

    @Column(name = "duration_months")
    private Integer durationMonths;
    @Column(name = "billing_day")
    private Integer billingDay;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "early_termination_fee")
    private Double earlyTerminationFee;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "cycle_type")
    private BillingCycleType cycleType;

    @ManyToOne
    @JoinColumn(name = "contract_detail_id")
    private ContractDetail contractDetail;

    public Commitment() {

    }
}
