package com.gygy.customerservice.application.customer.query;

import org.springframework.stereotype.Component;

import com.gygy.customerservice.application.customer.mapper.CustomerMapper;
import com.gygy.customerservice.application.customer.service.CustomerMessageService;
import com.gygy.customerservice.application.customer.validation.CustomerValidation;
import com.gygy.customerservice.domain.entity.CorporateCustomer;
import com.gygy.customerservice.domain.entity.IndividualCustomer;
import com.gygy.customerservice.domain.enums.CustomerType;
import com.gygy.customerservice.infrastructure.persistence.repository.CustomerRepository;

import an.awesome.pipelinr.Command;
import lombok.RequiredArgsConstructor;

public class GetCustomerByPhoneNumberQuery implements Command<GetCustomerByPhoneNumberResponse> {
    private final String phoneNumber;

    public GetCustomerByPhoneNumberQuery(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Component
    @RequiredArgsConstructor
    public static class GetCustomerByPhoneNumberQueryHandler implements Command.Handler<GetCustomerByPhoneNumberQuery, GetCustomerByPhoneNumberResponse> {
        private final CustomerRepository customerRepository;
        private final CustomerMapper customerMapper;
        private final CustomerMessageService customerMessageService;
        private final CustomerValidation customerValidation;

        @Override
        public GetCustomerByPhoneNumberResponse handle(GetCustomerByPhoneNumberQuery query) {
            GetCustomerByPhoneNumberResponse response = GetCustomerByPhoneNumberResponse.builder().build();

            customerValidation.validatePhoneNumberAndThrowValidationError(query.phoneNumber);
            // Check for individual customer
            customerRepository.findByPhoneNumberAndType(query.phoneNumber, CustomerType.INDIVIDUAL)
                    .ifPresent(customer -> {
                        IndividualCustomer individualCustomer = (IndividualCustomer) customer;
                        response.setIndividualCustomer(customerMapper.convertToIndividualCustomerInfo(individualCustomer));
                    });

            // Check for corporate customer
            customerRepository.findByPhoneNumberAndType(query.phoneNumber, CustomerType.CORPORATE)
                    .ifPresent(customer -> {
                        CorporateCustomer corporateCustomer = (CorporateCustomer) customer;
                        response.setCorporateCustomer(customerMapper.convertToCorporateCustomerInfo(corporateCustomer));
                    });

            response.setMessage(customerMessageService.getPhoneNumberStatusMessage(response));
            return response;
        }
    }
}