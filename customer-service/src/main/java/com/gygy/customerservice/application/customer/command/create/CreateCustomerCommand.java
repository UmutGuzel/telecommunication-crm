package com.gygy.customerservice.application.customer.command.create;

import an.awesome.pipelinr.Command;
import com.gygy.customerservice.application.customer.mapper.CustomerMapper;
import com.gygy.customerservice.application.customer.rule.CustomerRule;
import com.gygy.customerservice.domain.entity.Customer;
import com.gygy.customerservice.persistance.repository.CustomerRepository;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerCommand implements Command<CreatedCustomerResponse> {

    private String email;
    private String phoneNumber;

    @Component
    @RequiredArgsConstructor
    public static class CreateCustomerCommandHandler implements Command.Handler<CreateCustomerCommand, CreatedCustomerResponse> {
        private final CustomerRepository customerRepository;
        private final CustomerMapper customerMapper;
        private final CustomerRule customerRule;

        @Override
        public CreatedCustomerResponse handle(CreateCustomerCommand command) {
            Customer customer = customerRepository.findByEmail(command.getEmail()).orElse(null);
            customerRule.checkUserNotExists(customer);
            Customer newCustomer = customerMapper.convertCreateCommandToCustomer(command);
            customerRepository.save(newCustomer);
            return customerMapper.convertCustomerToCreatedCustomerResponse(newCustomer);
        }
    }
}
