package com.gygy.customerservice.application.individualCustomer.query;

import com.gygy.customerservice.application.customer.rule.CustomerRule;
import com.gygy.customerservice.domain.entity.IndividualCustomer;
import com.gygy.customerservice.infrastructure.persistence.repository.IndividualCustomerRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import an.awesome.pipelinr.Command;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetIndividualCustomerByIdQuery implements Command<GetIndividualCustomerByIdResponse> {
    private UUID id;

    @Component
    @RequiredArgsConstructor
    public static class GetIndividualCustomerByIdQueryHandler implements Command.Handler<GetIndividualCustomerByIdQuery, GetIndividualCustomerByIdResponse> {
        private final IndividualCustomerRepository individualCustomerRepository;
        private final CustomerRule customerRule;

        @Override
        public GetIndividualCustomerByIdResponse handle(GetIndividualCustomerByIdQuery query) {
            IndividualCustomer customer = individualCustomerRepository.findById(query.getId())
                    .orElse(null);
            
            customerRule.checkCustomerExists(customer);
            
            return GetIndividualCustomerByIdResponse.builder()
                    .id(customer.getId())
                    .email(customer.getEmail())
                    .phoneNumber(customer.getPhoneNumber())
                    .identityNumber(customer.getIdentityNumber())
                    .name(customer.getName())
                    .surname(customer.getSurname())
                    .fatherName(customer.getFatherName())
                    .motherName(customer.getMotherName())
                    .gender(customer.getGender())
                    .birthDate(customer.getBirthDate())
                    .build();
        }
    }
}