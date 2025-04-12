package com.gygy.customerservice.application.customer.command.delete;

import an.awesome.pipelinr.Command;

import com.gygy.customerservice.application.customer.mapper.CustomerMapper;
import com.gygy.customerservice.application.customer.rule.CustomerRule;
import com.gygy.customerservice.application.customer.validation.CustomerValidation;
import com.gygy.customerservice.domain.entity.Customer;
import com.gygy.customerservice.persistance.repository.CustomerRepository;

import lombok.*;

import org.springframework.stereotype.Component;

import java.util.UUID;

import com.gygy.customerservice.application.customer.service.CustomerService;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteCustomerCommand implements Command<DeletedCustomerResponse> {
    // String format to check the UUID pattern format then convert it to UUID
    private String id;

    @Component
    @RequiredArgsConstructor
    public static class DeleteCustomerCommandHandler implements Command.Handler<DeleteCustomerCommand, DeletedCustomerResponse> {
        private final CustomerRepository customerRepository;
        private final CustomerRule customerRule;
        private final CustomerMapper customerMapper;
        private final CustomerValidation customerValidation;
        private final CustomerService customerService;

        @Override
        public DeletedCustomerResponse handle(DeleteCustomerCommand command) {
            customerValidation.validateIdAndThrowValidationError(command.getId());

            UUID customerId = customerService.convertStringToUUID(command.getId());

            Customer customer = customerRepository.findById(customerId).orElse(null);
            customerRule.checkCustomerExists(customer);
            customerRepository.delete(customer);
            return customerMapper.convertCustomerToDeletedCustomerResponse(customer);
        }
    }
}
