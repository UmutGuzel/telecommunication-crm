package com.gygy.customerservice.application.customer.query;

import org.springframework.stereotype.Component;

import com.gygy.customerservice.application.customer.mapper.CustomerMapper;
import com.gygy.customerservice.application.customer.rule.CustomerRule;
import com.gygy.customerservice.application.customer.validation.CustomerValidation;
import com.gygy.customerservice.domain.entity.Customer;
import com.gygy.customerservice.persistance.repository.CustomerRepository;

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
        private final CustomerValidation customerValidation;
        private final CustomerRule customerRule;

        @Override
        public GetCustomerByPhoneNumberResponse handle(GetCustomerByPhoneNumberQuery query) {
            customerValidation.validatePhoneNumber(query.phoneNumber);
            
            Customer customer = customerRepository.findByPhoneNumber(query.phoneNumber).orElse(null);
            customerRule.checkCustomerExists(customer);
            
            return customerMapper.convertCustomerToGetCustomerByPhoneNumberResponse(customer);
        }
    }
} 