package com.gygy.customerservice.infrastructure.messaging.event.db;

import com.gygy.customerservice.domain.enums.CustomerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatedCorporateCustomerReadEvent {
    private UUID id;
    private String email;
    private String phoneNumber;
    private CustomerType type;

    private String companyName;
    private String contactPersonName;
    private String contactPersonSurname;
}
