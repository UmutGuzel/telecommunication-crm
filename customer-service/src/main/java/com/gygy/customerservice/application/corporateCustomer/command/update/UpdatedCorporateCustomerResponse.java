package com.gygy.customerservice.application.corporateCustomer.command.update;

import com.gygy.customerservice.application.customer.dto.AddressResponse;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatedCorporateCustomerResponse {
    private UUID id;
    private String email;
    private String phoneNumber;

    private String companyName;
    private String contactPersonName;
    private String contactPersonSurname;

    private AddressResponse address;
}
