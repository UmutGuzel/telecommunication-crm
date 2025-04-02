package com.gygy.customerservice.infrastructure.messaging.event;

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
    private String name;
    private String surname;
}