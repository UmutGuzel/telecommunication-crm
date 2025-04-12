package com.gygy.notificationservice.tempdto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CorporateCustomerCreatedEvent {
    private UUID id;
    private String email;
    private String taxNumber;
    private String companyName;
    private String contactPersonName;
    private String contactPersonSurname;
}
