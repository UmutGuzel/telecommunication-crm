package com.gygy.customerservice.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import com.gygy.customerservice.domain.enums.CustomerStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @UuidGenerator
    private UUID id;

    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private CustomerStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

//    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
//    private IndividualCustomer individualCustomer;
//
//    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
//    private CorporateCustomer corporateCustomer;

    @ManyToOne
    @JoinColumn(name = "address_id", insertable = false, updatable = false)
    private Address address;
} 