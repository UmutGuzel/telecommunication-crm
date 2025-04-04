package com.gygy.customerservice.application.customer.command.update;

import lombok.*;

import java.util.UUID;

import com.gygy.customerservice.application.customer.dto.AddressResponse;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatedCustomerResponse {
    private UUID id;
    private String email;
    private String phoneNumber;
    private AddressResponse address;
}

