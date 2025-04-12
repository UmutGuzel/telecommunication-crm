package com.gygy.customerservice.application.customer.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.gygy.customerservice.application.customer.dto.CreateAddressDto;
import com.gygy.customerservice.application.customer.dto.UpdateAddressDto;
import com.gygy.customerservice.core.exception.type.ValidationException;

@Component
public class AddressValidation {
    private static final int MAX_STREET_LENGTH = 200;
    private static final int MAX_DISTRICT_LENGTH = 100;
    private static final int MAX_APARTMENT_NUMBER_LENGTH = 10;
    private static final int MAX_CITY_LENGTH = 100;
    private static final int MAX_COUNTRY_LENGTH = 100;

    public void validateCreateAddress(CreateAddressDto address) {
        List<String> errors = new ArrayList<>();
        
        if (address == null) {
            errors.add("Address cannot be null");
        } else {
            validateStreet(address.getStreet(), errors);
            validateDistrict(address.getDistrict(), errors);
            validateApartmentNumber(address.getApartmentNumber(), errors);
            validateCity(address.getCity(), errors);
            validateCountry(address.getCountry(), errors);
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public void validateUpdateAddress(UpdateAddressDto address) {
        List<String> errors = new ArrayList<>();

        if (address != null) {
            if (address.getStreet() != null && !address.getStreet().trim().isEmpty()) {
                validateStreet(address.getStreet(), errors);
            }
            if (address.getDistrict() != null && !address.getDistrict().trim().isEmpty()) {
                validateDistrict(address.getDistrict(), errors);
            }
            if (address.getApartmentNumber() != null && !address.getApartmentNumber().trim().isEmpty()) {
                validateApartmentNumber(address.getApartmentNumber(), errors);
            }
            if (address.getCity() != null && !address.getCity().trim().isEmpty()) {
                validateCity(address.getCity(), errors);
            }
            if (address.getCountry() != null && !address.getCountry().trim().isEmpty()) {
                validateCountry(address.getCountry(), errors);
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private void validateStreet(String street, List<String> errors) {
        if (street == null || street.trim().isEmpty()) {
            errors.add("Street cannot be empty");
        } else if (street.length() > MAX_STREET_LENGTH) {
            errors.add("Street cannot be longer than " + MAX_STREET_LENGTH + " characters");
        }
    }

    private void validateDistrict(String district, List<String> errors) {
        if (district == null || district.trim().isEmpty()) {
            errors.add("District cannot be empty");
        } else if (district.length() > MAX_DISTRICT_LENGTH) {
            errors.add("District cannot be longer than " + MAX_DISTRICT_LENGTH + " characters");
        }
    }

    private void validateApartmentNumber(String apartmentNumber, List<String> errors) {
        if (apartmentNumber == null || apartmentNumber.trim().isEmpty()) {
            errors.add("Apartment number cannot be empty");
        }
        else if (apartmentNumber.length() > MAX_APARTMENT_NUMBER_LENGTH) {
            errors.add("Apartment number cannot be longer than " + MAX_APARTMENT_NUMBER_LENGTH + " characters");
        }
    }

    private void validateCity(String city, List<String> errors) {
        if (city == null || city.trim().isEmpty()) {
            errors.add("City cannot be empty");
        } else if (city.length() > MAX_CITY_LENGTH) {
            errors.add("City cannot be longer than " + MAX_CITY_LENGTH + " characters");
        }
    }

    private void validateCountry(String country, List<String> errors) {
        if (country == null || country.trim().isEmpty()) {
            errors.add("Country cannot be empty");
        } else if (country.length() > MAX_COUNTRY_LENGTH) {
            errors.add("Country cannot be longer than " + MAX_COUNTRY_LENGTH + " characters");
        }
    }
}