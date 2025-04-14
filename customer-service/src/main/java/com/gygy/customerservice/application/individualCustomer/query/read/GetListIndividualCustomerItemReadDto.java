package com.gygy.customerservice.application.individualCustomer.query.read;

import com.gygy.customerservice.domain.enums.IndividualCustomerGender;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetListIndividualCustomerItemReadDto {
    private UUID id;
    private String email;
    private String phoneNumber;

    private String name;
    private String surname;
    private IndividualCustomerGender gender;
    private LocalDate birthDate;
}
