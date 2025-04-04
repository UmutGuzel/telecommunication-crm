package com.gygy.customerservice.application.individualCustomer.mapper;

import com.gygy.customerservice.application.customer.dto.AddressResponse;
import com.gygy.customerservice.application.customer.mapper.AddressMapper;
import com.gygy.customerservice.application.individualCustomer.command.create.CreateIndividualCustomerCommand;
import com.gygy.customerservice.application.individualCustomer.command.create.CreatedIndividualCustomerResponse;
import com.gygy.customerservice.application.individualCustomer.command.delete.DeletedIndividualCustomerResponse;
import com.gygy.customerservice.application.individualCustomer.command.update.UpdateIndividualCustomerCommand;
import com.gygy.customerservice.application.individualCustomer.command.update.UpdatedIndividualCustomerResponse;
import com.gygy.customerservice.application.individualCustomer.query.GetListIndividualCustomerItemDto;
import com.gygy.customerservice.domain.entity.Address;
import com.gygy.customerservice.domain.entity.IndividualCustomer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class IndividualCustomerMapper {
    private final AddressMapper addressMapper;

    public IndividualCustomer convertCreateCommandToIndividualCustomer(CreateIndividualCustomerCommand command) {
        Address address = addressMapper.convertCreateAddressDtoToAddress(command.getAddress());

        IndividualCustomer individualCustomer = new IndividualCustomer();
        individualCustomer.setEmail(command.getEmail());
        individualCustomer.setPhoneNumber(command.getPhoneNumber());
        individualCustomer.setAddress(address);
        individualCustomer.setCreatedAt(LocalDateTime.now());
        individualCustomer.setUpdatedAt(LocalDateTime.now());

        individualCustomer.setIdentityNumber(command.getIdentityNumber());
        individualCustomer.setName(command.getName());
        individualCustomer.setSurname(command.getSurname());
        individualCustomer.setFatherName(command.getFatherName());
        individualCustomer.setMotherName(command.getMotherName());
        individualCustomer.setGender(command.getGender());
        individualCustomer.setBirthDate(command.getBirthDate());

        return individualCustomer;
    }

    public CreatedIndividualCustomerResponse convertIndividualCustomerToResponse(IndividualCustomer individualCustomer) {
        AddressResponse addressResponse = addressMapper.convertAddressToAddressResponse(individualCustomer.getAddress());

        return CreatedIndividualCustomerResponse.builder()
                .id(individualCustomer.getId())
                .email(individualCustomer.getEmail())
                .phoneNumber(individualCustomer.getPhoneNumber())
                .identityNumber(individualCustomer.getIdentityNumber())
                .name(individualCustomer.getName())
                .surname(individualCustomer.getSurname())
                .fatherName(individualCustomer.getFatherName())
                .motherName(individualCustomer.getMotherName())
                .gender(individualCustomer.getGender())
                .birthDate(individualCustomer.getBirthDate())
                .address(addressResponse)
                .build();
    }

    public void updateIndividualCustomer(IndividualCustomer individualCustomer, UpdateIndividualCustomerCommand command) {
        if (command.getEmail() != null && !command.getEmail().isEmpty()) {
            individualCustomer.setEmail(command.getEmail());
        }
        if (command.getPhoneNumber() != null && !command.getPhoneNumber().isEmpty()) {
            individualCustomer.setPhoneNumber(command.getPhoneNumber());
        }
        if (command.getAddress() != null) {
            Address updatedAddress = addressMapper.convertUpdateAddressDtoToAddress(individualCustomer.getAddress(), command.getAddress());
            individualCustomer.setAddress(updatedAddress);
        }
        if (command.getIdentityNumber() != null && !command.getIdentityNumber().isEmpty()) {
            individualCustomer.setIdentityNumber(command.getIdentityNumber());
        }
        if (command.getName() != null && !command.getName().isEmpty()) {
            individualCustomer.setName(command.getName());
        }
        if (command.getSurname() != null && !command.getSurname().isEmpty()) {
            individualCustomer.setSurname(command.getSurname());
        }
        if (command.getFatherName() != null && !command.getFatherName().isEmpty()) {
            individualCustomer.setFatherName(command.getFatherName());
        }
        if (command.getMotherName() != null && !command.getMotherName().isEmpty()) {
            individualCustomer.setMotherName(command.getMotherName());
        }
        if (command.getGender() != null) {
            individualCustomer.setGender(command.getGender());
        }
        if (command.getBirthDate() != null) {
            individualCustomer.setBirthDate(command.getBirthDate());
        }
        individualCustomer.setUpdatedAt(LocalDateTime.now());
    }

    public UpdatedIndividualCustomerResponse convertIndividualCustomerToUpdatedIndividualCustomerResponse(IndividualCustomer individualCustomer) {
        return UpdatedIndividualCustomerResponse.builder()
                .id(individualCustomer.getId())
                .email(individualCustomer.getEmail())
                .phoneNumber(individualCustomer.getPhoneNumber())
                .identityNumber(individualCustomer.getIdentityNumber())
                .name(individualCustomer.getName())
                .surname(individualCustomer.getSurname())
                .fatherName(individualCustomer.getFatherName())
                .motherName(individualCustomer.getMotherName())
                .gender(individualCustomer.getGender())
                .birthDate(individualCustomer.getBirthDate())
                .address(addressMapper.convertAddressToAddressResponse(individualCustomer.getAddress()))
                .build();
    }

    public DeletedIndividualCustomerResponse convertIndividualCustomerToDeletedIndividualCustomerResponse(IndividualCustomer individualCustomer) {
        return DeletedIndividualCustomerResponse.builder()
                .id(individualCustomer.getId())
                .build();
    }

    public GetListIndividualCustomerItemDto convertIndividualCustomerToGetListIndividualCustomerItemDto(IndividualCustomer individualCustomer) {
        return GetListIndividualCustomerItemDto.builder()
                .id(individualCustomer.getId())
                .email(individualCustomer.getEmail())
                .phoneNumber(individualCustomer.getPhoneNumber())
                .name(individualCustomer.getName())
                .surname(individualCustomer.getSurname())
                .gender(individualCustomer.getGender() != null ? individualCustomer.getGender().name() : null)
                .birthDate(individualCustomer.getBirthDate())
                .build();
    }
}
