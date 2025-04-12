package com.gygy.customerservice.domain.entity;

import com.gygy.customerservice.domain.enums.CustomerStatus;
import com.gygy.customerservice.domain.enums.CustomerType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "customers")
@Inheritance(strategy = InheritanceType.JOINED)
public class Customer {

    @Id
    @UuidGenerator
    private UUID id;

    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private CustomerStatus status = CustomerStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type")
    private CustomerType type;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @Builder.Default
    @Column(name = "allow_email_messages")
    private boolean allowEmailMessages = true;

    @Builder.Default
    @Column(name = "allow_sms_messages")
    private boolean allowSmsMessages = true;

    @Builder.Default
    @Column(name = "allow_promotional_emails")
    private boolean allowPromotionalEmails = false;

    @Builder.Default
    @Column(name = "allow_promotional_sms")
    private boolean allowPromotionalSms = false;
} 