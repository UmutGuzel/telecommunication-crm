package com.gygy.customerservice.infrastructure.messaging.event;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatedCorporateCustomerEvent {
    private UUID id;
    private String email;
    private String taxNumber;
    private String companyName;
    private String contactPersonName;
    private String contactPersonSurname;
} 