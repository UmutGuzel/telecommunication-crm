package com.gygy.customerservice.infrastructure.messaging.event.db;

import com.gygy.customerservice.application.customer.dto.AddressResponse;
import com.gygy.customerservice.domain.enums.IndividualCustomerGender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatedIndividualCustomerReadEvent {
    private UUID id;
    private String email;
    private String phoneNumber;

    private String name;
    private String surname;
    private IndividualCustomerGender gender;
    private LocalDate birthDate;
}
