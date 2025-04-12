package com.gygy.customerservice.application.customer.query.read;

import an.awesome.pipelinr.Command;

import com.gygy.customerservice.application.customer.validation.CustomerValidation;
import com.gygy.customerservice.domain.enums.CustomerType;
import com.gygy.customerservice.infrastructure.persistence.repository.CustomerReadRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import com.gygy.customerservice.application.customer.mapper.CustomerMapper;
import com.gygy.customerservice.application.customer.query.GetCustomerByPhoneNumberResponse;
import com.gygy.customerservice.application.customer.service.CustomerMessageService;
import com.gygy.customerservice.domain.entity.CustomerReadEntity;

public class GetCustomerByPhoneNumberReadQuery implements Command<GetCustomerByPhoneNumberResponse> {
    private final String phoneNumber;

    public GetCustomerByPhoneNumberReadQuery(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    @Component
    @RequiredArgsConstructor
    public static class GetCustomerByPhoneNumberReadQueryHandler implements Command.Handler<GetCustomerByPhoneNumberReadQuery, GetCustomerByPhoneNumberResponse> {
        private final CustomerReadRepository customerReadRepository;
        private final CustomerMapper customerMapper;
        private final CustomerValidation customerValidation;
        private final CustomerMessageService customerMessageService;

        @Override
        public GetCustomerByPhoneNumberResponse handle(GetCustomerByPhoneNumberReadQuery query) {
            customerValidation.validatePhoneNumberAndThrowValidationError(query.phoneNumber);

            CustomerReadEntity individualCustomer = customerReadRepository.findByPhoneNumberAndType(query.phoneNumber, CustomerType.INDIVIDUAL).orElse(null);
            CustomerReadEntity corporateCustomer = customerReadRepository.findByPhoneNumberAndType(query.phoneNumber, CustomerType.CORPORATE).orElse(null);
            
            // Create response
            GetCustomerByPhoneNumberResponse response = GetCustomerByPhoneNumberResponse.builder().build();
            
            // Set individual customer info if exists
            if (individualCustomer != null) {
                response.setIndividualCustomer(customerMapper.convertToIndividualCustomerInfo(individualCustomer));
            }
            
            // Set corporate customer info if exists
            if (corporateCustomer != null) {
                response.setCorporateCustomer(customerMapper.convertToCorporateCustomerInfo(corporateCustomer));
            }
            
            // Set message
            response.setMessage(customerMessageService.getPhoneNumberStatusMessage(response));
            return response;
        }
    } 
}