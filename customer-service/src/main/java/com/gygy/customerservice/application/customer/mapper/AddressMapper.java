package com.gygy.customerservice.application.customer.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.gygy.customerservice.application.customer.dto.AddressResponse;
import com.gygy.customerservice.application.customer.dto.CreateAddressDto;
import com.gygy.customerservice.application.customer.dto.UpdateAddressDto;
import com.gygy.customerservice.domain.entity.Address;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AddressMapper {
    public Address convertCreateAddressDtoToAddress(CreateAddressDto addressDto) {
        return Address.builder()
                .street(addressDto.getStreet())
                .district(addressDto.getDistrict())
                .apartmentNumber(addressDto.getApartmentNumber())
                .city(addressDto.getCity())
                .country(addressDto.getCountry())
                .postalCode(addressDto.getPostalCode())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public AddressResponse convertAddressToAddressResponse(Address address) {
        return AddressResponse.builder()
                .street(address.getStreet())
                .district(address.getDistrict())
                .apartmentNumber(address.getApartmentNumber())
                .city(address.getCity())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .build();
    }

    public Address convertUpdateAddressDtoToAddress(Address existingAddress, UpdateAddressDto addressDto) {
        
        existingAddress.setStreet(addressDto.getStreet() != null ? addressDto.getStreet() : existingAddress.getStreet());
        existingAddress.setDistrict(addressDto.getDistrict() != null ? addressDto.getDistrict() : existingAddress.getDistrict());
        existingAddress.setApartmentNumber(addressDto.getApartmentNumber() != null ? addressDto.getApartmentNumber() : existingAddress.getApartmentNumber());
        existingAddress.setCity(addressDto.getCity() != null ? addressDto.getCity() : existingAddress.getCity());
        existingAddress.setCountry(addressDto.getCountry() != null ? addressDto.getCountry() : existingAddress.getCountry());
        existingAddress.setPostalCode(addressDto.getPostalCode() != null ? addressDto.getPostalCode() : existingAddress.getPostalCode());
        existingAddress.setUpdatedAt(LocalDateTime.now());
    
        return existingAddress;
    }
}
