package com.gygy.customerservice.application.corporateCustomer.query;

import an.awesome.pipelinr.Command;
import com.gygy.customerservice.application.customer.rule.CustomerRule;
import com.gygy.customerservice.domain.entity.CorporateCustomer;
import com.gygy.customerservice.infrastructure.persistence.repository.CorporateCustomerRepository;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetCorporateCustomerByIdQuery implements Command<GetCorporateCustomerByIdResponse> {
    private UUID id;

    @Component
    @RequiredArgsConstructor
    public static class GetCorporateCustomerByIdQueryHandler implements Command.Handler<GetCorporateCustomerByIdQuery, GetCorporateCustomerByIdResponse> {
        private final CorporateCustomerRepository individualCustomerRepository;
        private final CustomerRule customerRule;

        @Override
        public GetCorporateCustomerByIdResponse handle(GetCorporateCustomerByIdQuery query) {
            CorporateCustomer customer = individualCustomerRepository.findById(query.getId())
                    .orElse(null);

            customerRule.checkCustomerExists(customer);

            return GetCorporateCustomerByIdResponse.builder()
                    .id(customer.getId())
                    .email(customer.getEmail())
                    .phoneNumber(customer.getPhoneNumber())
                    .taxNumber(customer.getTaxNumber())
                    .companyName(customer.getCompanyName())
                    .contactPersonName(customer.getContactPersonName())
                    .contactPersonSurname(customer.getContactPersonSurname())
                    .build();
        }
    }
}
