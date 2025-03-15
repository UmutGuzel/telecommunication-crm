package com.gygy.customerservice.application.customer.command.create;

import lombok.*;

import java.util.UUID;

import com.gygy.customerservice.application.customer.dto.AddressResponse;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatedCustomerResponse {
    private UUID id;
    private String email;
    private String phoneNumber;
    private AddressResponse address;
}
