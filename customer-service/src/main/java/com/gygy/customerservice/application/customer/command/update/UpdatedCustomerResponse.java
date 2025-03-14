package com.gygy.customerservice.application.customer.command.update;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatedCustomerResponse {
    private UUID id;
    private String email;
    private String phoneNumber;
}

