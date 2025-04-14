package com.gygy.customerservice.domain.entity;

import com.gygy.customerservice.domain.enums.CustomerType;
import com.gygy.customerservice.infrastructure.persistence.listener.CorporateCustomerListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Entity
@EntityListeners(CorporateCustomerListener.class)
@Table(name = "corporate_customers")
public class CorporateCustomer extends Customer {
    @Column(name = "tax_number", nullable = false)
    private String taxNumber;

    @Column(name = "tax_number_hash", nullable = false, unique = true)
    private String taxNumberHash;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "contact_person_name")
    private String contactPersonName;

    @Column(name = "contact_person_surname")
    private String contactPersonSurname;

    public CorporateCustomer() {
        this.setType(CustomerType.CORPORATE);
    }
}