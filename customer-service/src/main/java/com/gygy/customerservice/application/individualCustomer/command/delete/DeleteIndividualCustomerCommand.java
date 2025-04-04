package com.gygy.customerservice.application.individualCustomer.command.delete;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.gygy.customerservice.application.customer.rule.CustomerRule;
import com.gygy.customerservice.application.customer.validation.CustomerValidation;
import com.gygy.customerservice.application.individualCustomer.mapper.IndividualCustomerMapper;
import com.gygy.customerservice.domain.entity.IndividualCustomer;
import com.gygy.customerservice.persistance.repository.IndividualCustomerRepository;

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
public class DeleteIndividualCustomerCommand implements Command<DeletedIndividualCustomerResponse> {

    private UUID id;

    @Component
    @RequiredArgsConstructor
    public static class DeleteIndividualCustomerCommandHandler implements Command.Handler<DeleteIndividualCustomerCommand, DeletedIndividualCustomerResponse> {
        private final IndividualCustomerRepository individualCustomerRepository;
        private final CustomerRule customerRule;
        private final IndividualCustomerMapper individualCustomerMapper;
        private final CustomerValidation customerValidation;

        @Override
        public DeletedIndividualCustomerResponse handle(DeleteIndividualCustomerCommand command) {
            // Validate ID
            customerValidation.validateId(command.getId());

            IndividualCustomer individualCustomer = individualCustomerRepository.findById(command.getId()).orElse(null);
            customerRule.checkCustomerExists(individualCustomer);
            individualCustomerRepository.delete(individualCustomer);
            return individualCustomerMapper.convertIndividualCustomerToDeletedIndividualCustomerResponse(individualCustomer);
        }
    }
}
