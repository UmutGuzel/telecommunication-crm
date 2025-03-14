package com.gygy.customerservice.application.customer.command.create;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatedCustomerResponse {
    private UUID id;
    private String email;
    private String phoneNumber;
}
