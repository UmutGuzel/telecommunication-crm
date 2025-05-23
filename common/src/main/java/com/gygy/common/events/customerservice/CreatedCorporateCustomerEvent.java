package com.gygy.common.events.customerservice;

import com.gygy.common.events.dto.AddressResponse;
import lombok.*;

import java.util.UUID;
import com.gygy.common.events.enums.CustomerType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatedCorporateCustomerEvent {
    private UUID id;
    private String email;
    private String phoneNumber;

    private String companyName;
    private String contactPersonName;
    private String contactPersonSurname;
    private CustomerType customerType;
    private boolean allowEmailMessages;
    private boolean allowSmsMessages;
    private boolean allowPromotionalEmails;
    private boolean allowPromotionalSms;

    private AddressResponse address;
} 