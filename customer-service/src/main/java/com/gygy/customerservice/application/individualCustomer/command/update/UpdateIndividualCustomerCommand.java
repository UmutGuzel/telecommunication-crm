package com.gygy.customerservice.application.individualCustomer.command.update;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.gygy.customerservice.application.customer.dto.UpdateAddressDto;
import com.gygy.customerservice.application.customer.rule.CustomerRule;
import com.gygy.customerservice.application.customer.service.CustomerService;
import com.gygy.customerservice.application.customer.validation.AddressValidation;
import com.gygy.customerservice.application.customer.validation.CustomerValidation;
import com.gygy.customerservice.application.individualCustomer.mapper.IndividualCustomerMapper;
import com.gygy.customerservice.domain.entity.IndividualCustomer;
import com.gygy.customerservice.domain.enums.IndividualCustomerGender;
import com.gygy.customerservice.infrastructure.messaging.service.KafkaProducerService;
import com.gygy.customerservice.infrastructure.persistence.repository.IndividualCustomerRepository;

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
public class UpdateIndividualCustomerCommand implements Command<UpdatedIndividualCustomerResponse> {
    // String format to check the UUID pattern format then convert it to UUID
    private String id;
    private String email;
    private String phoneNumber;

    private String name;
    private String surname;
    private String fatherName;
    private String motherName;
    private IndividualCustomerGender gender;
    private LocalDate birthDate;

    private UpdateAddressDto address;

    @Component
    @RequiredArgsConstructor
    public static class UpdateIndividualCustomerCommandHandler implements Command.Handler<UpdateIndividualCustomerCommand, UpdatedIndividualCustomerResponse> {
        private final IndividualCustomerRepository individualCustomerRepository;
        private final IndividualCustomerMapper individualCustomerMapper;
        private final CustomerRule customerRule;
        private final CustomerValidation customerValidation;
        private final AddressValidation addressValidation;
        private final KafkaProducerService kafkaProducerService;
        private final CustomerService customerService;

        @Override
        public UpdatedIndividualCustomerResponse handle(UpdateIndividualCustomerCommand command) {
            customerValidation.validateUpdateIndividualCustomer(command);

            if (command.getAddress() != null) {
                addressValidation.validateUpdateAddress(command.getAddress());
            }

            UUID customerId = customerService.convertStringToUUID(command.getId());

            IndividualCustomer individualCustomer = individualCustomerRepository.findById(customerId).orElse(null);
            customerRule.checkCustomerExists(individualCustomer);

            customerRule.validateUpdateIndividualCustomer(command.getEmail(), command.getPhoneNumber());

            individualCustomerMapper.updateIndividualCustomer(individualCustomer, command);
            individualCustomerRepository.save(individualCustomer);

            kafkaProducerService.sendUpdatedIndividualCustomerEvent(
                individualCustomerMapper.convertToUpdatedIndividualCustomerEvent(individualCustomer)
            );

            kafkaProducerService.sendUpdatedIndividualCustomerReadEvent(
                individualCustomerMapper.convertToUpdatedIndividualCustomerReadEvent(individualCustomer)
            );

            return individualCustomerMapper.convertIndividualCustomerToUpdatedIndividualCustomerResponse(individualCustomer);
        }
    }
}
