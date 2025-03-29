package com.gygy.customerservice.application.corporateCustomer.command.create;

import an.awesome.pipelinr.Command;
import com.gygy.customerservice.application.corporateCustomer.mapper.CorporateCustomerMapper;
import com.gygy.customerservice.application.customer.dto.CreateAddressDto;
import com.gygy.customerservice.application.customer.mapper.AddressMapper;
import com.gygy.customerservice.application.customer.rule.CustomerRule;
import com.gygy.customerservice.domain.entity.Address;
import com.gygy.customerservice.domain.entity.CorporateCustomer;
import com.gygy.customerservice.domain.entity.Customer;
import com.gygy.customerservice.infrastructure.messaging.KafkaProducerService;
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

        @Override
        public CreatedCorporateCustomerResponse handle(CreateCorporateCustomerCommand command) {
            Customer customer = customerRepository.findByEmail(command.getEmail()).orElse(null);
            customerRule.checkCustomerNotExists(customer);

            CreateAddressDto addressDto = command.getAddress();
            Address existingAddress = addressRepository.findByStreetAndDistrictAndCityAndCountry(
            addressDto.getStreet(), addressDto.getDistrict(), addressDto.getCity(), addressDto.getCountry()).orElse(null);

            Address finalAddress;
            if (existingAddress != null) {
                finalAddress = existingAddress;
            } else {
                finalAddress = addressMapper.convertCreateAddressDtoToAddress(addressDto);
                addressRepository.save(finalAddress);
            }

            CorporateCustomer corporateCustomer = corporateCustomerMapper.convertCreateCommandToCorporateCustomer(command);
            corporateCustomer.setAddress(finalAddress);

            corporateCustomerRepository.save(corporateCustomer);

//            kafkaProducerService.sendCreatedCorporateCustomerEvent(new CreatedCorporateCustomerEvent(corporateCustomer.getId()));

            return corporateCustomerMapper.convertCorporateCustomerToResponse(corporateCustomer);
        }
    }
}
