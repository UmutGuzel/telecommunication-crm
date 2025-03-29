package com.gygy.customerservice.application.customer.command.create;

import an.awesome.pipelinr.Command;

import com.gygy.customerservice.application.customer.dto.CreateAddressDto;
import com.gygy.customerservice.application.customer.mapper.CustomerMapper;
import com.gygy.customerservice.application.customer.rule.CustomerRule;
import com.gygy.customerservice.domain.entity.Customer;
import com.gygy.customerservice.persistance.repository.CustomerRepository;

import lombok.*;

import org.springframework.stereotype.Component;

import com.gygy.customerservice.application.customer.mapper.AddressMapper;
import com.gygy.customerservice.domain.entity.Address;
import com.gygy.customerservice.persistance.repository.AddressRepository;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerCommand implements Command<CreatedCustomerResponse> {

    private String email;
    private String phoneNumber;
    private CreateAddressDto address;

    @Component
    @RequiredArgsConstructor
    public static class CreateCustomerCommandHandler implements Command.Handler<CreateCustomerCommand, CreatedCustomerResponse> {
        private final CustomerRepository customerRepository;
        private final CustomerMapper customerMapper;
        private final CustomerRule customerRule;
        private final AddressRepository addressRepository;
        private final AddressMapper addressMapper;

        @Override
        public CreatedCustomerResponse handle(CreateCustomerCommand command) {
            Customer customer = customerRepository.findByEmail(command.getEmail()).orElse(null);
            customerRule.checkCustomerNotExists(customer);

            CreateAddressDto addressDto = command.getAddress();
            Address existingAddress = addressRepository.findByStreetAndDistrictAndCityAndCountry(
            addressDto.getStreet(), addressDto.getDistrict(), addressDto.getCity(), addressDto.getCountry()).orElse(null);

            Address finalAddress = (existingAddress != null) ? existingAddress : addressMapper.convertCreateAddressDtoToAddress(addressDto);
            if (existingAddress == null) {
                addressRepository.save(finalAddress);
            }

            Customer newCustomer = customerMapper.convertCreateCommandToCustomer(command);
            newCustomer.setAddress(finalAddress);
            customerRepository.save(newCustomer);
            return customerMapper.convertCustomerToCreatedCustomerResponse(newCustomer);
        }
    }
}
