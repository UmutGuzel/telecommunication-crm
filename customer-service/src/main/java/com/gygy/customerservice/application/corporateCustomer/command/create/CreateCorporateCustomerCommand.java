package com.gygy.customerservice.application.corporateCustomer.command.create;

import an.awesome.pipelinr.Command;
import com.gygy.customerservice.application.corporateCustomer.mapper.CorporateCustomerMapper;
import com.gygy.customerservice.application.customer.dto.CreateAddressDto;
import com.gygy.customerservice.application.customer.mapper.AddressMapper;
import com.gygy.customerservice.application.customer.rule.CustomerRule;
import com.gygy.customerservice.application.customer.validation.AddressValidation;
import com.gygy.customerservice.application.customer.validation.CustomerValidation;
import com.gygy.customerservice.domain.entity.Address;
import com.gygy.customerservice.domain.entity.CorporateCustomer;
import com.gygy.customerservice.infrastructure.messaging.service.KafkaProducerService;
import com.gygy.customerservice.persistance.repository.AddressRepository;
import com.gygy.customerservice.persistance.repository.CorporateCustomerRepository;
import com.gygy.customerservice.persistance.repository.CustomerRepository;
import lombok.*;
import org.springframework.stereotype.Component;

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
        private final AddressValidation addressValidation;

        @Override
        public CreatedCorporateCustomerResponse handle(CreateCorporateCustomerCommand command) {
            customerValidation.validateCreateCorporateCustomer(command);

            addressValidation.validateCreateAddress(command.getAddress());

            customerRule.validateCorporateCustomer(command.getEmail(), command.getPhoneNumber(), command.getTaxNumber());

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

            kafkaProducerService.sendCreatedCorporateCustomerEvent(
                corporateCustomerMapper.convertToCreatedCorporateCustomerEvent(newCorporateCustomer)
            );

            return corporateCustomerMapper.convertCorporateCustomerToResponse(newCorporateCustomer);
        }
    }
}
