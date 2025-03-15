package com.gygy.customerservice.application.customer.command.delete;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeletedCustomerResponse {
    private UUID id;
}
