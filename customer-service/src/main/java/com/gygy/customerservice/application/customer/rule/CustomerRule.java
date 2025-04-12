package com.gygy.customerservice.application.customer.rule;

import org.springframework.stereotype.Component;

import com.gygy.customerservice.core.exception.type.BusinessException;
import com.gygy.customerservice.domain.entity.Customer;
import com.gygy.customerservice.domain.enums.CustomerType;
import com.gygy.customerservice.infrastructure.persistence.repository.CustomerRepository;
import com.gygy.customerservice.infrastructure.persistence.repository.IndividualCustomerRepository;
import com.gygy.customerservice.infrastructure.persistence.repository.CorporateCustomerRepository;
import com.gygy.customerservice.domain.entity.CustomerReadEntity;

import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomerRule {
    private final CustomerRepository customerRepository;
    private final IndividualCustomerRepository individualCustomerRepository;
    private final CorporateCustomerRepository corporateCustomerRepository;

    public void checkCustomerExists(Customer customer) {
        if (customer == null) {
            throw new BusinessException("Customer not found");
        }
    }

    public void validateIndividualCustomer(String email, String phoneNumber, String identityNumber) {
        List<String> errors = new ArrayList<>();

        if (email != null && !email.trim().isEmpty() && customerRepository.findByEmail(email).isPresent()) {
            errors.add("Email is already used by another customer");
        }

        if (phoneNumber != null && !phoneNumber.trim().isEmpty() && 
            customerRepository.findByPhoneNumberAndType(phoneNumber, CustomerType.INDIVIDUAL).isPresent()) {
            errors.add("Phone number is already used by another individual customer");
        }

        if (identityNumber != null && !identityNumber.trim().isEmpty() && 
            individualCustomerRepository.findByIdentityNumber(identityNumber).isPresent()) {
            errors.add("Identity number is already used by another customer");
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }
    }

    public void validateCorporateCustomer(String email, String phoneNumber, String taxNumber) {
        List<String> errors = new ArrayList<>();

        if (email != null && !email.trim().isEmpty() && customerRepository.findByEmail(email).isPresent()) {
            errors.add("Email is already used by another customer");
        }

        if (phoneNumber != null && !phoneNumber.trim().isEmpty() && 
            customerRepository.findByPhoneNumberAndType(phoneNumber, CustomerType.CORPORATE).isPresent()) {
            errors.add("Phone number is already used by another corporate customer");
        }

        if (taxNumber != null && !taxNumber.trim().isEmpty() && 
            corporateCustomerRepository.findByTaxNumber(taxNumber).isPresent()) {
            errors.add("Tax number is already used by another customer");
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }
    }

    public void validateUpdateIndividualCustomer(String email, String phoneNumber) {
        List<String> errors = new ArrayList<>();

        if (email != null && !email.trim().isEmpty() && customerRepository.findByEmail(email).isPresent()) {
            errors.add("Email is already used by another customer");
        }

        if (phoneNumber != null && !phoneNumber.trim().isEmpty() &&
            customerRepository.findByPhoneNumberAndType(phoneNumber, CustomerType.INDIVIDUAL).isPresent()) {
            errors.add("Phone number is already used by another individual customer");
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }
    }

    public void validateUpdateCorporateCustomer(String email, String phoneNumber) {
        List<String> errors = new ArrayList<>();

        if (email != null && !email.trim().isEmpty() && customerRepository.findByEmail(email).isPresent()) {
            errors.add("Email is already used by another customer");
        }

        if (phoneNumber != null && !phoneNumber.trim().isEmpty() &&
            customerRepository.findByPhoneNumberAndType(phoneNumber, CustomerType.CORPORATE).isPresent()) {
            errors.add("Phone number is already used by another corporate customer");
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }
    }

    public void checkCustomerReadEntityExists(CustomerReadEntity customer) {
        if (customer == null) {
            throw new BusinessException("Customer not found");
        }
    }
}
