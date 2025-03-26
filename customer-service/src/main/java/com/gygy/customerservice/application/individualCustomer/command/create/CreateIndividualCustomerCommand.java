package com.gygy.customerservice.application.individualCustomer.command.create;

import an.awesome.pipelinr.Command;
import com.gygy.customerservice.application.customer.dto.CreateAddressDto;
import com.gygy.customerservice.application.customer.mapper.AddressMapper;
import com.gygy.customerservice.application.customer.rule.CustomerRule;
import com.gygy.customerservice.application.individualCustomer.mapper.IndividualCustomerMapper;
import com.gygy.customerservice.domain.entity.Address;
import com.gygy.customerservice.domain.entity.Customer;
import com.gygy.customerservice.domain.entity.IndividualCustomer;
import com.gygy.customerservice.domain.enums.IndividualCustomerGender;
import com.gygy.customerservice.infrastructure.messaging.KafkaProducerService;
import com.gygy.customerservice.persistance.repository.AddressRepository;
import com.gygy.customerservice.persistance.repository.CustomerRepository;
import com.gygy.customerservice.persistance.repository.IndividualCustomerRepository;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

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

        @Override
        public CreatedIndividualCustomerResponse handle(CreateIndividualCustomerCommand command) {
            Customer customer = customerRepository.findByEmail(command.getEmail()).orElse(null);
            customerRule.checkCustomerNotExists(customer);

            CreateAddressDto addressDto = command.getAddress();
            Address existingAddress = addressRepository.findByStreetAndDistrictAndCityAndCountry(
            addressDto.getStreet(), addressDto.getDistrict(), addressDto.getCity(), addressDto.getCountry()).orElse(null);

            Address finalAddress = (existingAddress != null) ? existingAddress : addressMapper.convertCreateAddressDtoToAddress(addressDto);
            if (existingAddress == null) {
                addressRepository.save(finalAddress);
            }

            IndividualCustomer newIndividualCustomer = individualCustomerMapper.convertCreateCommandToIndividualCustomer(command);
            newIndividualCustomer.setAddress(finalAddress);

            individualCustomerRepository.save(newIndividualCustomer);

//            kafkaProducerService.sendCreatedIndividualCustomerEvent(CreatedIndividualCustomerEvent.builder()
//                    .id(newIndividualCustomer.getId())
//                    .email(newIndividualCustomer.getEmail())
//                    .name(newIndividualCustomer.getName())
//                    .surname(newIndividualCustomer.getSurname())
//                    .build());

            return individualCustomerMapper.convertIndividualCustomerToResponse(newIndividualCustomer);
        }
    }
}
