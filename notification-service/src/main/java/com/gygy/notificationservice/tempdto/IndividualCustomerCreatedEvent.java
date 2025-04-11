package com.gygy.notificationservice.tempdto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IndividualCustomerCreatedEvent {
    private UUID id;
    private String email;
    private String name;
    private String surname;
}
