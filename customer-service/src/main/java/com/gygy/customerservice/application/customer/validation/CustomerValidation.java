package com.gygy.customerservice.application.customer.validation;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.gygy.customerservice.application.corporateCustomer.command.create.CreateCorporateCustomerCommand;
import com.gygy.customerservice.application.corporateCustomer.command.update.UpdateCorporateCustomerCommand;
import com.gygy.customerservice.application.individualCustomer.command.create.CreateIndividualCustomerCommand;
import com.gygy.customerservice.application.individualCustomer.command.update.UpdateIndividualCustomerCommand;
import com.gygy.customerservice.core.exception.type.ValidationException;
import com.gygy.customerservice.domain.enums.IndividualCustomerGender;

@Component
public class CustomerValidation {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+90[0-9]{10}$");
    private static final Pattern TAX_NUMBER_PATTERN = Pattern.compile("^[1-9][0-9]{9}$");
    private static final Pattern IDENTITY_NUMBER_PATTERN = Pattern.compile("^[1-9][0-9]{10}$");
    private static final Pattern UUID_PATTERN = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-ZğüşıöçĞÜŞİÖÇ\\s]+$");
    private static final Pattern COMPANY_NAME_PATTERN = Pattern.compile("^[a-zA-ZğüşıöçĞÜŞİÖÇ0-9\\s.,&'-]+$");

    private List<String> errors;

    public CustomerValidation() {
        this.errors = new ArrayList<>();
    }

    private void addError(String error) {
        this.errors.add(error);
    }

    private void throwIfErrorsExist() {
        if (!errors.isEmpty()) {
            List<String> errorList = new ArrayList<>(errors);
            errors.clear();
            throw new ValidationException(errorList);
        }
    }
    
    public void validateId(String id) {
        if (id == null) {
            addError("ID cannot be null");
        } else if (!UUID_PATTERN.matcher(id).matches()) {
            addError("Invalid UUID format");
        }
    }

    public void validateIdAndThrowValidationError(String id) {
        validateId(id);
        throwIfErrorsExist();
    }

    private <E extends Enum<E>> boolean isValidEnumValue(Class<E> enumClass, E value) {
        return value != null && EnumSet.allOf(enumClass).contains(value);
    }

    public void validateGender(IndividualCustomerGender gender) {
        if (gender == null) {
            addError("Gender cannot be null");
        } else if (!isValidEnumValue(IndividualCustomerGender.class, gender)) {
            addError("Invalid gender value. Valid values are: " + Arrays.toString(IndividualCustomerGender.values()));
        }
    }


    public void validateIdentityNumber(String identityNumber) {
        if (identityNumber == null || identityNumber.trim().isEmpty()) {
            addError("Identity number cannot be empty");
        } else if (!IDENTITY_NUMBER_PATTERN.matcher(identityNumber).matches()) {
            addError("Invalid identity number format. Must be 11 digits and cannot start with 0");
        } else {
            int[] digits = identityNumber.chars().map(Character::getNumericValue).toArray();
            if (digits[10] % 2 != 0) {
                addError("Identity number's last digit must be even");
            }
        }
    }

    public void validateName(String name, String fieldName) {
        if (name == null || name.trim().isEmpty()) {
            addError(fieldName + " cannot be empty");
        } else if (!NAME_PATTERN.matcher(name).matches()) {
            addError(fieldName + " can only contain letters and spaces");
        } else if (name.length() < 2 || name.length() > 50) {
            addError(fieldName + " must be between 2 and 50 characters");
        }
    }

    public void validateCompanyName(String companyName) {
        if (companyName == null || companyName.trim().isEmpty()) {
            addError("Company name cannot be empty");
        } else if (!COMPANY_NAME_PATTERN.matcher(companyName).matches()) {
            addError("Company name can only contain letters, numbers, and special characters (.,&'-)");
        } else if (companyName.length() < 3 || companyName.length() > 100) {
            addError("Company name must be between 3 and 100 characters");
        }
    }

    public void validateTaxNumber(String taxNumber) {
        if (taxNumber == null || taxNumber.trim().isEmpty()) {
            addError("Tax number cannot be empty");
        } else if (!TAX_NUMBER_PATTERN.matcher(taxNumber).matches()) {
            addError("Invalid tax number format. Must be 10 digits and cannot start with 0");
        }
    }

    public void validateBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            addError("Birth date cannot be empty");
        } else {
            LocalDate now = LocalDate.now();
            if (birthDate.isAfter(now)) {
                addError("Birth date cannot be in the future");
            } else if (birthDate.isBefore(now.minusYears(120))) {
                addError("Invalid birth date");
            } else if (birthDate.isAfter(now.minusYears(18))) {
                addError("Customer must be at least 18 years old");
            }
        }
    }

    public void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            addError("Phone number cannot be empty");
        } else if (!PHONE_PATTERN.matcher(phoneNumber).matches()) {
            addError("Invalid phone number format. Must start with +90 followed by 10 digits");
        }
    }

    public void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            addError("Email cannot be empty");
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            addError("Invalid email format");
        }
    }

    public void validateEmailAndThrowValidationError(String email) {
        validateEmail(email);
        throwIfErrorsExist();
    }

    public void validatePhoneNumberAndThrowValidationError(String phoneNumber) {
        validatePhoneNumber(phoneNumber);
        throwIfErrorsExist();
    }

    public void validateCreateIndividualCustomer(CreateIndividualCustomerCommand command) {
        validateEmail(command.getEmail());
        validatePhoneNumber(command.getPhoneNumber());
        validateIdentityNumber(command.getIdentityNumber());
        validateName(command.getName(), "Name");
        validateName(command.getSurname(), "Surname");
        validateName(command.getFatherName(), "Father name");
        validateName(command.getMotherName(), "Mother name");
        validateBirthDate(command.getBirthDate());
        validateGender(command.getGender());

        if (command.getAddress() == null) {
            addError("Address cannot be empty");
        }
        throwIfErrorsExist();
    }

    public void validateCreateCorporateCustomer(CreateCorporateCustomerCommand command) {
        validateEmail(command.getEmail());
        validatePhoneNumber(command.getPhoneNumber());
        validateTaxNumber(command.getTaxNumber());
        validateCompanyName(command.getCompanyName());
        validateName(command.getContactPersonName(), "Contact person name");
        validateName(command.getContactPersonSurname(), "Contact person surname");

        if (command.getAddress() == null) {
            addError("Address cannot be empty");
        }
        throwIfErrorsExist();
    }

    public void validateUpdateIndividualCustomer(UpdateIndividualCustomerCommand command) {
        validateId(command.getId());

        if (command.getEmail() != null && !command.getEmail().trim().isEmpty()) {
            validateEmail(command.getEmail());
        }

        if (command.getPhoneNumber() != null && !command.getPhoneNumber().trim().isEmpty()) {
            validatePhoneNumber(command.getPhoneNumber());
        }

        if (command.getName() != null && !command.getName().trim().isEmpty()) {
            validateName(command.getName(), "Name");
        }

        if (command.getSurname() != null && !command.getSurname().trim().isEmpty()) {
            validateName(command.getSurname(), "Surname");
        }

        if (command.getFatherName() != null && !command.getFatherName().trim().isEmpty()) {
            validateName(command.getFatherName(), "Father name");
        }

        if (command.getMotherName() != null && !command.getMotherName().trim().isEmpty()) {
            validateName(command.getMotherName(), "Mother name");
        }

        if (command.getBirthDate() != null) {
            validateBirthDate(command.getBirthDate());
        }

        if (command.getGender() != null) {
            validateGender(command.getGender());
        }
        throwIfErrorsExist();
    }

    public void validateUpdateCorporateCustomer(UpdateCorporateCustomerCommand command) {
        validateId(command.getId());

        if (command.getEmail() != null && !command.getEmail().trim().isEmpty()) {
            validateEmail(command.getEmail());
        }

        if (command.getPhoneNumber() != null && !command.getPhoneNumber().trim().isEmpty()) {
            validatePhoneNumber(command.getPhoneNumber());
        }

        if (command.getCompanyName() != null && !command.getCompanyName().trim().isEmpty()) {
            validateCompanyName(command.getCompanyName());
        }

        if (command.getContactPersonName() != null && !command.getContactPersonName().trim().isEmpty()) {
            validateName(command.getContactPersonName(), "Contact person name");
        }

        if (command.getContactPersonSurname() != null && !command.getContactPersonSurname().trim().isEmpty()) {
            validateName(command.getContactPersonSurname(), "Contact person surname");
        }
        throwIfErrorsExist();
    }
}