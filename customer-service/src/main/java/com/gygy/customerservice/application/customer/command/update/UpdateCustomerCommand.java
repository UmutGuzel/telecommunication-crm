package com.gygy.customerservice.application.customer.command.update;

import an.awesome.pipelinr.Command;
import com.gygy.customerservice.application.customer.command.create.CreateCustomerCommand;
import com.gygy.customerservice.application.customer.command.create.CreatedCustomerResponse;
import com.gygy.customerservice.application.customer.mapper.CustomerMapper;
import com.gygy.customerservice.application.customer.rule.CustomerRule;
import com.gygy.customerservice.domain.entity.Customer;
import com.gygy.customerservice.persistance.repository.CustomerRepository;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCustomerCommand implements Command<UpdatedCustomerResponse> {

    private UUID id;
    private String email;
    private String phoneNumber;

    @Component
    @RequiredArgsConstructor
    public static class UpdateCustomerCommandHandler implements Command.Handler<UpdateCustomerCommand, UpdatedCustomerResponse> {
        private final CustomerRepository customerRepository;
        private final CustomerMapper customerMapper;
        private final CustomerRule customerRule;

        @Override
        public UpdatedCustomerResponse handle(UpdateCustomerCommand command) {
            Customer customer = customerRepository.findById(command.getId()).orElse(null);
            customerRule.checkUserExists(customer);
            customerMapper.updateCustomer(customer, command);
            customerRepository.save(customer);
            return customerMapper.convertCustomerToUpdatedCustomerResponse(customer);
        }
    }
}
