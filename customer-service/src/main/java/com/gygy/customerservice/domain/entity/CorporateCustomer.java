package com.gygy.customerservice.domain.entity;

import com.gygy.customerservice.domain.enums.CustomerType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "corporate_customers")
public class CorporateCustomer extends Customer {
    @Column(name = "tax_number")
    private String taxNumber;

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