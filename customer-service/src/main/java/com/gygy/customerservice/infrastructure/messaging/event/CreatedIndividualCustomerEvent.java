package com.gygy.customerservice.infrastructure.messaging.event;

import com.gygy.customerservice.domain.enums.CustomerType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatedIndividualCustomerEvent {
    private UUID id;
    private String email;
    private String phoneNumber;

    private String name;
    private String surname;
    private CustomerType customerType;
    private boolean allowEmailMessages;
    private boolean allowSmsMessages;
    private boolean allowPromotionalEmails;
    private boolean allowPromotionalSms;
}