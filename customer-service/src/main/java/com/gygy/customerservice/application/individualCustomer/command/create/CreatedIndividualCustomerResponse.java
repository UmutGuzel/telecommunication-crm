package com.gygy.customerservice.application.individualCustomer.command.create;

import com.gygy.customerservice.application.customer.dto.AddressResponse;
import com.gygy.customerservice.domain.enums.IndividualCustomerGender;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatedIndividualCustomerResponse {
    private UUID id;
    private String email;
    private String phoneNumber;
    private String identityNumber;
    private String name;
    private String surname;
    private String fatherName;
    private String motherName;
    private IndividualCustomerGender gender;
    private Date birthDate;
    private AddressResponse address;
}
