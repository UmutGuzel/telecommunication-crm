package com.gygy.customerservice.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gygy.customerservice.domain.enums.CustomerType;
import com.gygy.customerservice.domain.enums.IndividualCustomerGender;
import com.gygy.customerservice.infrastructure.persistence.listener.IndividualCustomerListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@Entity
@SuperBuilder
@Table(name = "individual_customers")
@EntityListeners(IndividualCustomerListener.class)
public class IndividualCustomer extends Customer {
    @Column(name = "identity_number", nullable = false)
    private String identityNumber;
    
    @Column(name = "identity_number_hash", nullable = false, unique = true)
    private String identityNumberHash;

    private String name;
    private String surname;

    @Column(name = "father_name")
    private String fatherName;

    @Column(name = "mother_name")
    private String motherName;

    @Enumerated(EnumType.STRING)
    private IndividualCustomerGender gender;

    @Column(name = "birth_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private LocalDate birthDate;

    public IndividualCustomer() {
        this.setType(CustomerType.INDIVIDUAL);
    }
}