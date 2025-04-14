package com.gygy.customerservice.application.corporateCustomer.query.read;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetListCorporateCustomerItemReadDto {
    private UUID id;
    private String email;
    private String phoneNumber;

    private String companyName;
    private String contactPersonName;
    private String contactPersonSurname;
}
