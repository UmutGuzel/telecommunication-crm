package com.gygy.customerservice.application.individualCustomer.command.delete;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeletedIndividualCustomerResponse {
    private UUID id;
}
