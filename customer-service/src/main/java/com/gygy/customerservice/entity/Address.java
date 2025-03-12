package com.gygy.customerservice.entity;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @UuidGenerator
    private UUID id;

    private String street;
    private String district;
    private String city;
    private String country;

    @Column(name = "adress_line")
    private String addressLine;

    @OneToMany(mappedBy = "address")
    private List<Customer> customers;
} 