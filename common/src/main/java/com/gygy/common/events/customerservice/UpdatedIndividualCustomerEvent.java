package com.gygy.common.events.customerservice;

import com.gygy.common.events.enums.IndividualCustomerGender;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatedIndividualCustomerEvent {
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