package com.gygy.customerservice.application.customer.command.delete;

import an.awesome.pipelinr.Command;
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
public class DeleteCustomerCommand implements Command<DeletedCustomerResponse> {
    private UUID id;

    @Component
    @RequiredArgsConstructor
    public static class DeleteCustomerCommandHandler implements Command.Handler<DeleteCustomerCommand, DeletedCustomerResponse> {
        private final CustomerRepository customerRepository;
        private final CustomerRule customerRule;
        private final CustomerMapper customerMapper;

        @Override
        public DeletedCustomerResponse handle(DeleteCustomerCommand command) {
            Customer customer = customerRepository.findById(command.getId()).orElse(null);
            customerRule.checkCustomerExists(customer);
            customerRepository.delete(customer);
            return customerMapper.convertCustomerToDeletedCustomerResponse(customer);
        }
    }
}
