package com.gygy.customerservice.application.individualCustomer.query;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetListIndividualCustomerItemDto {
    private UUID id;
    private String email;
    private String phoneNumber;
    private String name;
    private String surname;
    private String gender;
    private LocalDate birthDate;
}
