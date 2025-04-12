package com.gygy.customerservice.application.individualCustomer.query;

import com.gygy.customerservice.domain.enums.IndividualCustomerGender;
import java.time.LocalDate;
import java.util.UUID;

public class GetListIndividualCustomerItemDto {
    private final UUID id;
    private final String email;
    private final String phoneNumber;
    private final String name;
    private final String surname;
    private final IndividualCustomerGender gender;
    private final LocalDate birthDate;

    // JPA projection için constructor - parametre sırası JPQL sorgusundaki sırayla aynı olmalı
    public GetListIndividualCustomerItemDto(UUID id, String email, String phoneNumber, 
                                          String name, String surname, IndividualCustomerGender gender, 
                                          LocalDate birthDate) {
        this.id = id;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.birthDate = birthDate;
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

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getGender() {
        return gender != null ? gender.name() : null;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }
}
