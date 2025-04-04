package com.gygy.customerservice.application.corporateCustomer.command.create;

import org.springframework.stereotype.Component;

import com.gygy.customerservice.application.corporateCustomer.mapper.CorporateCustomerMapper;
import com.gygy.customerservice.application.customer.dto.CreateAddressDto;
import com.gygy.customerservice.application.customer.mapper.AddressMapper;
import com.gygy.customerservice.application.customer.rule.CustomerRule;
import com.gygy.customerservice.application.customer.validation.CustomerValidation;
import com.gygy.customerservice.domain.entity.Address;
import com.gygy.customerservice.domain.entity.CorporateCustomer;
import com.gygy.customerservice.domain.entity.Customer;
import com.gygy.customerservice.infrastructure.messaging.event.CreatedCorporateCustomerEvent;
import com.gygy.customerservice.infrastructure.messaging.service.KafkaProducerService;
import com.gygy.customerservice.persistance.repository.AddressRepository;
import com.gygy.customerservice.persistance.repository.CorporateCustomerRepository;
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
public class CreateCorporateCustomerCommand implements Command<CreatedCorporateCustomerResponse> {

    private String email;
    private String phoneNumber;

    private String taxNumber;
    private String companyName;
    private String contactPersonName;
    private String contactPersonSurname;

    private CreateAddressDto address;

    @Component
    @RequiredArgsConstructor
    public static class CreateCorporateCustomerCommandHandler implements Handler<CreateCorporateCustomerCommand, CreatedCorporateCustomerResponse> {
        private final CustomerRepository customerRepository;
        private final CorporateCustomerRepository corporateCustomerRepository;
        private final CorporateCustomerMapper corporateCustomerMapper;
        private final CustomerRule customerRule;
        private final AddressRepository addressRepository;
        private final AddressMapper addressMapper;
        private final KafkaProducerService kafkaProducerService;
        private final CustomerValidation customerValidation;

        @Override
        public CreatedCorporateCustomerResponse handle(CreateCorporateCustomerCommand command) {
            // Validate all fields at once
            customerValidation.validateCreateCorporateCustomer(command);

            Customer customer = customerRepository.findByEmail(command.getEmail()).orElse(null);
            customerRule.checkCustomerNotExists(customer);

            CreateAddressDto addressDto = command.getAddress();
            Address existingAddress = addressRepository.findByStreetAndDistrictAndCityAndCountry(
            addressDto.getStreet(), addressDto.getDistrict(), addressDto.getCity(), addressDto.getCountry()).orElse(null);

            Address finalAddress = (existingAddress != null) ? existingAddress : addressMapper.convertCreateAddressDtoToAddress(addressDto);
            if (existingAddress == null) {
                addressRepository.save(finalAddress);
            }

            CorporateCustomer corporateCustomer = corporateCustomerMapper.convertCreateCommandToCorporateCustomer(command);
            corporateCustomer.setAddress(finalAddress);

            CorporateCustomer newCorporateCustomer = corporateCustomerRepository.save(corporateCustomer);

            kafkaProducerService.sendCreatedCorporateCustomerEvent(CreatedCorporateCustomerEvent.builder()
                    .id(newCorporateCustomer.getId())
                    .email(newCorporateCustomer.getEmail())
                    .taxNumber(newCorporateCustomer.getTaxNumber())
                    .companyName(newCorporateCustomer.getCompanyName())
                    .contactPersonName(newCorporateCustomer.getContactPersonName())
                    .contactPersonSurname(newCorporateCustomer.getContactPersonSurname())
                    .build());

            return corporateCustomerMapper.convertCorporateCustomerToResponse(newCorporateCustomer);
        }
    }
}
