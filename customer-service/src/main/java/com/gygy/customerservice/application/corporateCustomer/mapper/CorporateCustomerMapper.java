package com.gygy.customerservice.application.corporateCustomer.mapper;

import com.gygy.customerservice.application.corporateCustomer.command.create.CreateCorporateCustomerCommand;
import com.gygy.customerservice.application.corporateCustomer.command.create.CreatedCorporateCustomerResponse;
import com.gygy.customerservice.application.corporateCustomer.command.update.UpdateCorporateCustomerCommand;
import com.gygy.customerservice.application.corporateCustomer.command.update.UpdatedCorporateCustomerResponse;
import com.gygy.customerservice.application.corporateCustomer.query.GetListCorporateCustomerItemDto;
import com.gygy.customerservice.application.customer.dto.AddressResponse;
import com.gygy.customerservice.application.customer.mapper.AddressMapper;
import com.gygy.customerservice.domain.entity.Address;
import com.gygy.customerservice.domain.entity.CorporateCustomer;
import com.gygy.customerservice.infrastructure.messaging.event.CreatedCorporateCustomerEvent;
import com.gygy.customerservice.infrastructure.messaging.event.UpdatedCorporateCustomerEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class CorporateCustomerMapper {
    private final AddressMapper addressMapper;

    public CorporateCustomer convertCreateCommandToCorporateCustomer(CreateCorporateCustomerCommand command) {
        Address address = addressMapper.convertCreateAddressDtoToAddress(command.getAddress());

        CorporateCustomer corporateCustomer = new CorporateCustomer();
        corporateCustomer.setEmail(command.getEmail());
        corporateCustomer.setPhoneNumber(command.getPhoneNumber());
        corporateCustomer.setAddress(address);
        corporateCustomer.setCreatedAt(LocalDateTime.now());
        corporateCustomer.setUpdatedAt(LocalDateTime.now());

        corporateCustomer.setTaxNumber(command.getTaxNumber());
        corporateCustomer.setCompanyName(command.getCompanyName());
        corporateCustomer.setContactPersonName(command.getContactPersonName());
        corporateCustomer.setContactPersonSurname(command.getContactPersonSurname());

        return corporateCustomer;
    }

    public CreatedCorporateCustomerResponse convertCorporateCustomerToResponse(CorporateCustomer corporateCustomer) {
        AddressResponse addressResponse = addressMapper.convertAddressToAddressResponse(corporateCustomer.getAddress());

        return CreatedCorporateCustomerResponse.builder()
                .id(corporateCustomer.getId())
                .email(corporateCustomer.getEmail())
                .phoneNumber(corporateCustomer.getPhoneNumber())
                .companyName(corporateCustomer.getCompanyName())
                .contactPersonName(corporateCustomer.getContactPersonName())
                .contactPersonSurname(corporateCustomer.getContactPersonSurname())
                .address(addressResponse)
                .build();
    }

    public void updateCorporateCustomer(CorporateCustomer corporateCustomer, UpdateCorporateCustomerCommand command) {
        if (command.getEmail() != null && !command.getEmail().isEmpty()) {
            corporateCustomer.setEmail(command.getEmail());
        }
        if (command.getPhoneNumber() != null && !command.getPhoneNumber().isEmpty()) {
            corporateCustomer.setPhoneNumber(command.getPhoneNumber());
        }
        if (command.getAddress() != null) {
            Address address = addressMapper.convertUpdateAddressDtoToAddress(corporateCustomer.getAddress(), command.getAddress());
            corporateCustomer.setAddress(address);
        }
        if (command.getCompanyName() != null && !command.getCompanyName().isEmpty()) {
            corporateCustomer.setCompanyName(command.getCompanyName());
        }
        if (command.getContactPersonName() != null && !command.getContactPersonName().isEmpty()) {
            corporateCustomer.setContactPersonName(command.getContactPersonName());
        }
        if (command.getContactPersonSurname() != null && !command.getContactPersonSurname().isEmpty()) {
            corporateCustomer.setContactPersonSurname(command.getContactPersonSurname());
        }
        corporateCustomer.setUpdatedAt(LocalDateTime.now());
    }

    public UpdatedCorporateCustomerResponse convertCorporateCustomerToUpdatedCorporateCustomerResponse(CorporateCustomer corporateCustomer) {
        AddressResponse addressResponse = addressMapper.convertAddressToAddressResponse(corporateCustomer.getAddress());

        return UpdatedCorporateCustomerResponse.builder()
                .id(corporateCustomer.getId())
                .email(corporateCustomer.getEmail())
                .phoneNumber(corporateCustomer.getPhoneNumber())
                .companyName(corporateCustomer.getCompanyName())
                .contactPersonName(corporateCustomer.getContactPersonName())
                .contactPersonSurname(corporateCustomer.getContactPersonSurname())
                .address(addressResponse)
                .build();
    }

    public CreatedCorporateCustomerEvent convertToCreatedCorporateCustomerEvent(CorporateCustomer customer) {
        return CreatedCorporateCustomerEvent.builder()
                .id(customer.getId())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .companyName(customer.getCompanyName())
                .contactPersonName(customer.getContactPersonName())
                .contactPersonSurname(customer.getContactPersonSurname())
                .customerType(customer.getType())
                .allowEmailMessages(customer.isAllowEmailMessages())
                .allowSmsMessages(customer.isAllowSmsMessages())
                .allowPromotionalEmails(customer.isAllowPromotionalEmails())
                .allowPromotionalSms(customer.isAllowPromotionalSms())
                .address(addressMapper.convertAddressToAddressResponse(customer.getAddress()))
                .build();
    }

    public UpdatedCorporateCustomerEvent convertToUpdatedCorporateCustomerEvent(CorporateCustomer customer) {
        return UpdatedCorporateCustomerEvent.builder()
                .id(customer.getId())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .companyName(customer.getCompanyName())
                .contactPersonName(customer.getContactPersonName())
                .contactPersonSurname(customer.getContactPersonSurname())
                .address(addressMapper.convertAddressToAddressResponse(customer.getAddress()))
                .build();
    }
}
