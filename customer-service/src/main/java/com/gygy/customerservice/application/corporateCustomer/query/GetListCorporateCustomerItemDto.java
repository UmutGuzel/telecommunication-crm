package com.gygy.customerservice.application.corporateCustomer.query;

import java.util.UUID;

public class GetListCorporateCustomerItemDto {
    private final UUID id;
    private final String email;
    private final String phoneNumber;
    private final String companyName;
    private final String contactPersonName;
    private final String contactPersonSurname;

    // JPA projection için constructor
    public GetListCorporateCustomerItemDto(UUID id, String email, String phoneNumber, 
                                         String companyName, String contactPersonName, 
                                         String contactPersonSurname) {
        this.id = id;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.companyName = companyName;
        this.contactPersonName = contactPersonName;
        this.contactPersonSurname = contactPersonSurname;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public String getContactPersonSurname() {
        return contactPersonSurname;
    }
}
