package com.gygy.customerservice.application.customer.query;

import org.springframework.stereotype.Component;

import com.gygy.customerservice.application.customer.mapper.CustomerMapper;
import com.gygy.customerservice.application.customer.rule.CustomerRule;
import com.gygy.customerservice.application.customer.validation.CustomerValidation;
import com.gygy.customerservice.domain.entity.Customer;
import com.gygy.customerservice.infrastructure.persistence.repository.CustomerRepository;

import an.awesome.pipelinr.Command;
import lombok.RequiredArgsConstructor;

public class GetCustomerByEmailQuery implements Command<GetCustomerByEmailResponse> {
    private final String email;

    public GetCustomerByEmailQuery(String email) {
        this.email = email;
    }

    @Component
    @RequiredArgsConstructor
    public static class GetCustomerByEmailQueryHandler implements Command.Handler<GetCustomerByEmailQuery, GetCustomerByEmailResponse> {
        private final CustomerRepository customerRepository;
        private final CustomerMapper customerMapper;
        private final CustomerValidation customerValidation;
        private final CustomerRule customerRule;

        @Override
        public GetCustomerByEmailResponse handle(GetCustomerByEmailQuery query) {
            customerValidation.validateEmailAndThrowValidationError(query.email);
            
            Customer customer = customerRepository.findByEmail(query.email).orElse(null);
            customerRule.checkCustomerExists(customer);
            
            return customerMapper.convertCustomerToGetCustomerByEmailResponse(customer);
        }
    }
} 