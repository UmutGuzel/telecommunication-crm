package com.gygy.customerservice.application.corporateCustomer.command.create;

import com.gygy.customerservice.application.customer.dto.AddressResponse;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatedCorporateCustomerResponse {
    private UUID id;
    private String email;
    private String phoneNumber;

    private String taxNumber;
    private String companyName;
    private String contactPersonName;
    private String contactPersonSurname;

    private AddressResponse address;
}
