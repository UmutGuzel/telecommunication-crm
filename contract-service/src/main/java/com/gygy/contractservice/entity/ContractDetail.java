package com.gygy.contractservice.entity;
import com.gygy.contractservice.model.enums.ContractDetailType;
import com.gygy.contractservice.model.enums.Status;
import com.gygy.contractservice.model.enums.ServiceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@AllArgsConstructor
public class ContractDetail {
    @Id
    @UuidGenerator
    private UUID id;
    //id değil customer number al

    //TODO:contract No lOng tipinde oluştur

    private String name;

    @Column(name="customer_name")
    private String customerName;

    @Column(name = "signature_date")
    private LocalDateTime signatureDate;

    @Column(name="cusmomer_id")
    private UUID customerId;

    @Column(name="phone_number")
    private String phoneNumber;


    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type", nullable = false)
    private ServiceType serviceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_detail_type", nullable = false)
    private ContractDetailType contractDetailType;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Email(message = "Email is not in proper format", regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private String email;


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

    public ContractDetail() {

    }


}
