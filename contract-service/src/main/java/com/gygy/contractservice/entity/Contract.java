package com.gygy.contractservice.entity;

import com.gygy.contractservice.model.enums.ContractStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "contracts")
@Getter
@Setter
public class Contract {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "contract_number", unique = true, nullable = false)
    private String contractNumber;

    @Column(name = "document_type", nullable = false)
    private String documentType;

    @Column(name = "document_url")
    private String documentUrl;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;

    @Column(name = "signature_date")
    private LocalDateTime signatureDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ContractStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "contract")
    private Set<ContractDetail> contractDetails;


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