package com.gygy.customerservice.application.customer.query;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCustomerByPhoneNumberResponse {
    private String message;
    private IndividualCustomerInfo individualCustomer;
    private CorporateCustomerInfo corporateCustomer;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IndividualCustomerInfo {
        private UUID id;
        private String email;
        private String name;
        private String surname;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CorporateCustomerInfo {
        private UUID id;
        private String email;
        private String companyName;
        private String contactPersonName;
        private String contactPersonSurname;
    }
}