package com.gygy.customerservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @UuidGenerator
    private UUID id;

    private String street;
    private String district;
    private String apartmentNumber;
    private String city;
    private String country;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "address")
    private Customer customer;
} 