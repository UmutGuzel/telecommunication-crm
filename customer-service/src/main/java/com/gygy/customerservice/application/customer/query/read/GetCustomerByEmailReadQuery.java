package com.gygy.customerservice.application.customer.query.read;

import an.awesome.pipelinr.Command;

import com.gygy.customerservice.application.customer.mapper.CustomerMapper;
import com.gygy.customerservice.application.customer.query.GetCustomerByEmailResponse;
import com.gygy.customerservice.application.customer.validation.CustomerValidation;
import com.gygy.customerservice.domain.entity.CustomerReadEntity;
import com.gygy.customerservice.infrastructure.persistence.repository.CustomerReadRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import com.gygy.customerservice.application.customer.rule.CustomerRule;

public class GetCustomerByEmailReadQuery implements Command<GetCustomerByEmailResponse> {
    private final String email;

    public GetCustomerByEmailReadQuery(String email) {
        this.email = email;
    }

    @Component
    @RequiredArgsConstructor
    public static class GetCustomerByEmailReadQueryHandler implements Command.Handler<GetCustomerByEmailReadQuery, GetCustomerByEmailResponse> {
        private final CustomerReadRepository customerReadRepository;
        private final CustomerValidation customerValidation;
        private final CustomerMapper customerMapper;
        private final CustomerRule customerRule;

        @Override
        public GetCustomerByEmailResponse handle(GetCustomerByEmailReadQuery query) {
            customerValidation.validateEmailAndThrowValidationError(query.email);
            
            CustomerReadEntity customer = customerReadRepository.findByEmail(query.email).orElse(null);
            customerRule.checkCustomerReadEntityExists(customer);
            
            return customerMapper.convertCustomerReadEntityToGetCustomerByEmailResponse(customer);
        }
    }
} 