package com.gygy.customerservice.application.customer.command.update;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.gygy.customerservice.application.customer.mapper.CustomerMapper;
import com.gygy.customerservice.application.customer.rule.CustomerRule;
import com.gygy.customerservice.application.customer.validation.CustomerValidation;
import com.gygy.customerservice.domain.entity.Customer;
import com.gygy.customerservice.domain.enums.CustomerStatus;
import com.gygy.customerservice.persistance.repository.CustomerRepository;

import an.awesome.pipelinr.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCustomerStatusCommand implements Command<UpdatedCustomerStatusResponse> {
    private UUID id;
    private CustomerStatus status;

    @Component
    @RequiredArgsConstructor
    public static class UpdateCustomerStatusCommandHandler implements Command.Handler<UpdateCustomerStatusCommand, UpdatedCustomerStatusResponse> {
        private final CustomerRepository customerRepository;
        private final CustomerMapper customerMapper;
        private final CustomerValidation customerValidation;
        private final CustomerRule customerRule;

        @Override
        public UpdatedCustomerStatusResponse handle(UpdateCustomerStatusCommand command) {
            customerValidation.validateId(command.getId());
            customerValidation.validateStatus(command.getStatus());
            
            Customer customer = customerRepository.findById(command.getId()).orElse(null);
            customerRule.checkCustomerExists(customer);
            
            customer.setStatus(command.getStatus());
            customerRepository.save(customer);
            
            return customerMapper.convertCustomerToUpdatedCustomerStatusResponse(customer);
        }
    }
} 