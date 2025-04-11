package com.gygy.customerservice.application.individualCustomer.command.create;

import com.gygy.customerservice.application.customer.dto.AddressResponse;
import com.gygy.customerservice.domain.enums.IndividualCustomerGender;
import lombok.*;

import java.time.LocalDate;
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

    private String name;
    private String surname;
    private IndividualCustomerGender gender;
    private LocalDate birthDate;

    private AddressResponse address;
}
