package com.gygy.customerservice.application.corporateCustomer.query;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetListCorporateCustomerItemDto {
    private UUID id;
    private String email;
    private String phoneNumber;
    private String taxNumber;
    private String companyName;
    private String contactPersonName;
    private String contactPersonSurname;
}
