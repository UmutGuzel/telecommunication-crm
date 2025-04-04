package com.gygy.customerservice.application.individualCustomer.command.update;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.gygy.customerservice.application.corporateCustomer.mapper.CorporateCustomerMapper;
import com.gygy.customerservice.application.customer.dto.UpdateAddressDto;
import com.gygy.customerservice.application.customer.mapper.AddressMapper;
import com.gygy.customerservice.application.customer.rule.CustomerRule;
import com.gygy.customerservice.application.customer.validation.AddressValidation;
import com.gygy.customerservice.application.customer.validation.CustomerValidation;
import com.gygy.customerservice.application.individualCustomer.mapper.IndividualCustomerMapper;
import com.gygy.customerservice.domain.entity.IndividualCustomer;
import com.gygy.customerservice.domain.enums.IndividualCustomerGender;
import com.gygy.customerservice.infrastructure.messaging.event.UpdatedIndividualCustomerEvent;
import com.gygy.customerservice.infrastructure.messaging.service.KafkaProducerService;
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
public class UpdateIndividualCustomerCommand implements Command<UpdatedIndividualCustomerResponse> {

    private UUID id;
    private String email;
    private String phoneNumber;

    private String identityNumber;
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

        @Override
        public UpdatedIndividualCustomerResponse handle(UpdateIndividualCustomerCommand command) {
            // Validate all fields at once
            customerValidation.validateUpdateIndividualCustomer(command);

            // Validate address if provided
            if (command.getAddress() != null) {
                addressValidation.validateUpdateAddress(command.getAddress());
            }

            IndividualCustomer individualCustomer = individualCustomerRepository.findById(command.getId()).orElse(null);
            customerRule.checkCustomerExists(individualCustomer);

            individualCustomerMapper.updateIndividualCustomer(individualCustomer, command);
            IndividualCustomer updatedCustomer = individualCustomerRepository.save(individualCustomer);

            kafkaProducerService.sendUpdatedIndividualCustomerEvent(UpdatedIndividualCustomerEvent.builder()
                    .id(updatedCustomer.getId())
                    .email(updatedCustomer.getEmail())
                    .phoneNumber(updatedCustomer.getPhoneNumber())
                    .identityNumber(updatedCustomer.getIdentityNumber())
                    .name(updatedCustomer.getName())
                    .surname(updatedCustomer.getSurname())
                    .fatherName(updatedCustomer.getFatherName())
                    .motherName(updatedCustomer.getMotherName())
                    .gender(updatedCustomer.getGender())
                    .birthDate(updatedCustomer.getBirthDate())
                    .build());

            return individualCustomerMapper.convertIndividualCustomerToUpdatedIndividualCustomerResponse(updatedCustomer);
        }
    }
}
