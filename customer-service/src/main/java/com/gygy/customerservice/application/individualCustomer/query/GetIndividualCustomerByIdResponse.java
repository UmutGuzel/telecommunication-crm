package com.gygy.customerservice.application.individualCustomer.query;

import com.gygy.customerservice.domain.enums.IndividualCustomerGender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetIndividualCustomerByIdResponse {
    private UUID id;
    private String email;
    private String phoneNumber;

    private String identityNumber;
    private String name;
    private String surname;
    private String fatherName;
    private String motherName;
    private IndividualCustomerGender gender;
    private LocalDate birthDate;
}