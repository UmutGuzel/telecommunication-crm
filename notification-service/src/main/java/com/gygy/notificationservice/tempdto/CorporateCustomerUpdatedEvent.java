package com.gygy.notificationservice.tempdto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CorporateCustomerUpdatedEvent {
    private UUID id;
    private String email;
    private String phoneNumber;
    private String taxNumber;
    private String companyName;
    private String contactPersonName;
    private String contactPersonSurname;

    private List<String> changedFields;
}
