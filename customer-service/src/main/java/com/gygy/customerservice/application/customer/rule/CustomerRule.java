package com.gygy.customerservice.application.customer.rule;

import org.springframework.stereotype.Component;

import com.gygy.customerservice.core.exception.type.BusinessException;
import com.gygy.customerservice.domain.entity.Customer;
import com.gygy.customerservice.domain.enums.CustomerType;
import com.gygy.customerservice.persistance.repository.CustomerRepository;
import com.gygy.customerservice.persistance.repository.IndividualCustomerRepository;
import com.gygy.customerservice.persistance.repository.CorporateCustomerRepository;

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

    public void checkPhoneNumberUsage(String phoneNumber, CustomerType type) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return;
        }

        if (type == CustomerType.INDIVIDUAL) {
            if (customerRepository.findByPhoneNumberAndType(phoneNumber, CustomerType.INDIVIDUAL).size() > 0) {
                throw new BusinessException("Phone number is already used by another individual customer");
            }
        } else if (type == CustomerType.CORPORATE) {
            if (customerRepository.findByPhoneNumberAndType(phoneNumber, CustomerType.CORPORATE).size() > 0) {
                throw new BusinessException("Phone number is already used by another corporate customer");
            }
        }
    }

    public void checkEmailUsage(String email) {
        if (email == null || email.trim().isEmpty()) {
            return;
        }

        if (customerRepository.findByEmail(email).isPresent()) {
            throw new BusinessException("Email is already used by another customer");
        }
    }

    public void checkIdentityNumberUsage(String identityNumber) {
        if (identityNumber == null || identityNumber.trim().isEmpty()) {
            return;
        }

        if (individualCustomerRepository.findByIdentityNumber(identityNumber).isPresent()) {
            throw new BusinessException("Identity number is already used by another customer");
        }
    }

    public void checkTaxNumberUsage(String taxNumber) {
        if (taxNumber == null || taxNumber.trim().isEmpty()) {
            return;
        }

        if (corporateCustomerRepository.findByTaxNumber(taxNumber).isPresent()) {
            throw new BusinessException("Tax number is already used by another customer");
        }
    }

    public void validateIndividualCustomer(String email, String phoneNumber, String identityNumber) {
        List<String> errors = new ArrayList<>();

        if (email != null && !email.trim().isEmpty() && customerRepository.findByEmail(email).isPresent()) {
            errors.add("Email is already used by another customer");
        }

        if (phoneNumber != null && !phoneNumber.trim().isEmpty() && 
            customerRepository.findByPhoneNumberAndType(phoneNumber, CustomerType.INDIVIDUAL).size() > 0) {
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
            customerRepository.findByPhoneNumberAndType(phoneNumber, CustomerType.CORPORATE).size() > 0) {
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
}
