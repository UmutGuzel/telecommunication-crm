package com.gygy.customerservice.domain.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

import com.gygy.customerservice.domain.enums.IndividualCustomerGender;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@SuperBuilder
@Table(name = "individual_customers")
public class IndividualCustomer extends Customer{
    @Column(name = "identity_number")
    private String identityNumber;

    private String name;
    private String surname;

    @Column(name = "father_name")
    private String fatherName;

    @Column(name = "mother_name")
    private String motherName;

    @Enumerated(EnumType.STRING)
    private IndividualCustomerGender gender;

    @Column(name = "birth_date")
    private Date birthDate;
}