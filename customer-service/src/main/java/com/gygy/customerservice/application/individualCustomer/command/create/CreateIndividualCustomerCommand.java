package com.gygy.customerservice.application.individualCustomer.command.create;

import java.time.LocalDate;

import com.gygy.customerservice.application.customer.validation.AddressValidation;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.gygy.customerservice.application.customer.dto.CreateAddressDto;
import com.gygy.customerservice.application.customer.mapper.AddressMapper;
import com.gygy.customerservice.application.customer.rule.CustomerRule;
import com.gygy.customerservice.application.customer.validation.CustomerValidation;
import com.gygy.customerservice.application.individualCustomer.mapper.IndividualCustomerMapper;
import com.gygy.customerservice.domain.entity.Address;
import com.gygy.customerservice.domain.entity.Customer;
import com.gygy.customerservice.domain.entity.IndividualCustomer;
import com.gygy.customerservice.domain.enums.CustomerType;
import com.gygy.customerservice.domain.enums.IndividualCustomerGender;
import com.gygy.customerservice.infrastructure.messaging.service.KafkaProducerService;
import com.gygy.customerservice.persistance.repository.AddressRepository;
import com.gygy.customerservice.persistance.repository.CustomerRepository;
import com.gygy.customerservice.persistance.repository.IndividualCustomerRepository;

import an.awesome.pipelinr.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateIndividualCustomerCommand implements Command<CreatedIndividualCustomerResponse> {

    private String email;
    private String phoneNumber;

    private String identityNumber;
    private String name;
    private String surname;
    private String fatherName;
    private String motherName;
    private IndividualCustomerGender gender;
    private LocalDate birthDate;

    private CreateAddressDto address;

    @Slf4j
    @Component
    @RequiredArgsConstructor
    public static class CreateIndividualCustomerCommandHandler implements Handler<CreateIndividualCustomerCommand, CreatedIndividualCustomerResponse> {
        private final CustomerRepository customerRepository;
        private final IndividualCustomerRepository individualCustomerRepository;
        private final IndividualCustomerMapper individualCustomerMapper;
        private final CustomerRule customerRule;
        private final AddressRepository addressRepository;
        private final AddressMapper addressMapper;
        private final KafkaProducerService kafkaProducerService;
        private final CustomerValidation customerValidation;
        private final AddressValidation addressValidation;

        @Override
        @Transactional
        public CreatedIndividualCustomerResponse handle(CreateIndividualCustomerCommand command) {
            customerValidation.validateCreateIndividualCustomer(command);
            addressValidation.validateCreateAddress(command.getAddress());
            customerRule.validateIndividualCustomer(command.getEmail(), command.getPhoneNumber(), command.getIdentityNumber());

            Address newAddress = addressMapper.convertCreateAddressDtoToAddress(command.getAddress());
            addressRepository.save(newAddress);

            IndividualCustomer newIndividualCustomer = individualCustomerMapper.convertCreateCommandToIndividualCustomer(command);
            newIndividualCustomer.setAddress(newAddress);

            individualCustomerRepository.save(newIndividualCustomer);
            log.debug("Customer saved to database");

            kafkaProducerService.sendCreatedIndividualCustomerEvent(
                individualCustomerMapper.convertToCreatedIndividualCustomerEvent(newIndividualCustomer)
            );

            return individualCustomerMapper.convertIndividualCustomerToResponse(newIndividualCustomer);
        }
    }
}
