package com.gygy.customerservice.application.individualCustomer.query;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetListIndividualCustomerItemDto {
    private UUID id;
    private String email;
}
