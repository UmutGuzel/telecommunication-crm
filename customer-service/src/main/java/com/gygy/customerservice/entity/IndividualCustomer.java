package com.gygy.customerservice.entity;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "individual_customers")
public class IndividualCustomer {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "identity_number")
    private String identityNumber;

    private String name;
    private String surname;

    @Column(name = "father_name")
    private String fatherName;

    @Column(name = "mother_name")
    private String motherName;

    private String gender;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "customer_id")
    private UUID customerId;

    @OneToOne
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    private Customer customer;
} 