package com.gygy.customerservice.application.customer.validation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.gygy.customerservice.application.corporateCustomer.command.create.CreateCorporateCustomerCommand;
import com.gygy.customerservice.application.corporateCustomer.command.update.UpdateCorporateCustomerCommand;
import com.gygy.customerservice.application.individualCustomer.command.create.CreateIndividualCustomerCommand;
import com.gygy.customerservice.application.individualCustomer.command.update.UpdateIndividualCustomerCommand;
import com.gygy.customerservice.core.exception.type.ValidationException;
import com.gygy.customerservice.domain.enums.CustomerStatus;
import com.gygy.customerservice.domain.enums.IndividualCustomerGender;

@Component
public class CustomerValidation {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+90[0-9]{10}$");
    private static final Pattern TAX_NUMBER_PATTERN = Pattern.compile("^[1-9][0-9]{9}$");
    private static final Pattern IDENTITY_NUMBER_PATTERN = Pattern.compile("^[1-9][0-9]{10}$");
    private static final Pattern UUID_PATTERN = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-ZğüşıöçĞÜŞİÖÇ\\s]+$");
    private static final Pattern COMPANY_NAME_PATTERN = Pattern.compile("^[a-zA-ZğüşıöçĞÜŞİÖÇ0-9\\s.,&'-]+$");

    public void validateId(UUID id) {
        List<String> errors = new ArrayList<>();
        if (id == null) {
            errors.add("ID cannot be null");
        } else if (!UUID_PATTERN.matcher(id.toString()).matches()) {
            errors.add("Invalid UUID format");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private <E extends Enum<E>> boolean isValidEnumValue(Class<E> enumClass, E value) {
        return value != null && EnumSet.allOf(enumClass).contains(value);
    }

    public void validateGender(IndividualCustomerGender gender) {
        List<String> errors = new ArrayList<>();
        if (gender == null) {
            errors.add("Gender cannot be null");
        } else if (!isValidEnumValue(IndividualCustomerGender.class, gender)) {
            errors.add("Invalid gender value. Valid values are: " + Arrays.toString(IndividualCustomerGender.values()));
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public void validateStatus(CustomerStatus status) {
        List<String> errors = new ArrayList<>();
        if (status == null) {
            errors.add("Status cannot be null");
        } else if (!isValidEnumValue(CustomerStatus.class, status)) {
            errors.add("Invalid gender value. Valid values are: " + Arrays.toString(CustomerStatus.values()));
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public void validateEmailIfProvided(String email) {
        if (email != null && !email.trim().isEmpty()) {
            List<String> errors = new ArrayList<>();
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                errors.add("Invalid email format");
            }
            if (!errors.isEmpty()) {
                throw new ValidationException(errors);
            }
        }
    }

    public void validatePhoneNumberIfProvided(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            List<String> errors = new ArrayList<>();
            if (!PHONE_PATTERN.matcher(phoneNumber).matches()) {
                errors.add("Invalid phone number format. Must start with +90 followed by 10 digits");
            }
            if (!errors.isEmpty()) {
                throw new ValidationException(errors);
            }
        }
    }

    public void validateCorporateCustomerFieldsIfProvided(String taxNumber, String companyName, String contactPersonName, String contactPersonSurname) {
        List<String> errors = new ArrayList<>();

        if (taxNumber != null && !taxNumber.trim().isEmpty()) {
            if (!TAX_NUMBER_PATTERN.matcher(taxNumber).matches()) {
                errors.add("Invalid tax number format. Must be 10 digits and cannot start with 0");
            }
        }

        if (companyName != null && !companyName.trim().isEmpty()) {
            if (companyName.length() < 3 || companyName.length() > 100) {
                errors.add("Company name must be between 3 and 100 characters");
            }
        }

        if (contactPersonName != null && !contactPersonName.trim().isEmpty()) {
            if (contactPersonName.length() < 2 || contactPersonName.length() > 50) {
                errors.add("Contact person name must be between 2 and 50 characters");
            }
        }

        if (contactPersonSurname != null && !contactPersonSurname.trim().isEmpty()) {
            if (contactPersonSurname.length() < 2 || contactPersonSurname.length() > 50) {
                errors.add("Contact person surname must be between 2 and 50 characters");
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public void validateIndividualCustomerFieldsIfProvided(String identityNumber, String name, String surname, String fatherName, String motherName, LocalDate birthDate, IndividualCustomerGender gender) {
        List<String> errors = new ArrayList<>();

        if (identityNumber != null && !identityNumber.trim().isEmpty()) {
            if (!IDENTITY_NUMBER_PATTERN.matcher(identityNumber).matches()) {
                errors.add("Invalid identity number format. Must be 11 digits and cannot start with 0");
            } else {
                int[] digits = identityNumber.chars().map(Character::getNumericValue).toArray();
                if (digits[10] % 2 != 0) {
                    errors.add("Identity number's last digit must be even");
                }
            }
        }

        if (name != null && !name.trim().isEmpty()) {
            if (name.length() < 2 || name.length() > 50) {
                errors.add("Name must be between 2 and 50 characters");
            }
        }

        if (surname != null && !surname.trim().isEmpty()) {
            if (surname.length() < 2 || surname.length() > 50) {
                errors.add("Surname must be between 2 and 50 characters");
            }
        }

        if (fatherName != null && !fatherName.trim().isEmpty()) {
            if (fatherName.length() < 2 || fatherName.length() > 50) {
                errors.add("Father name must be between 2 and 50 characters");
            }
        }

        if (motherName != null && !motherName.trim().isEmpty()) {
            if (motherName.length() < 2 || motherName.length() > 50) {
                errors.add("Mother name must be between 2 and 50 characters");
            }
        }

        if (birthDate != null) {
            if (birthDate.isAfter(LocalDate.now())) {
                errors.add("Birth date cannot be in the future");
            } else if (birthDate.isBefore(LocalDate.now().minusYears(120))) {
                errors.add("Invalid birth date");
            } else if (birthDate.isAfter(LocalDate.now().minusYears(18))) {
                errors.add("Customer must be at least 18 years old");
            }
        }

        if (gender != null) {
            validateGender(gender);
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public void validateIdentityNumber(String identityNumber) {
        List<String> errors = new ArrayList<>();
        if (identityNumber == null || identityNumber.trim().isEmpty()) {
            errors.add("Identity number cannot be empty");
        } else if (!IDENTITY_NUMBER_PATTERN.matcher(identityNumber).matches()) {
            errors.add("Invalid identity number format. Must be 11 digits and cannot start with 0");
        } else {
            int[] digits = identityNumber.chars().map(Character::getNumericValue).toArray();
            if (digits[10] % 2 != 0) {
                errors.add("Identity number's last digit must be even");
            }
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public void validateName(String name, String fieldName) {
        List<String> errors = new ArrayList<>();
        if (name == null || name.trim().isEmpty()) {
            errors.add(fieldName + " cannot be empty");
        } else if (!NAME_PATTERN.matcher(name).matches()) {
            errors.add(fieldName + " can only contain letters and spaces");
        } else if (name.length() < 2 || name.length() > 50) {
            errors.add(fieldName + " must be between 2 and 50 characters");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public void validateCompanyName(String companyName) {
        List<String> errors = new ArrayList<>();
        if (companyName == null || companyName.trim().isEmpty()) {
            errors.add("Company name cannot be empty");
        } else if (!COMPANY_NAME_PATTERN.matcher(companyName).matches()) {
            errors.add("Company name can only contain letters, numbers, and special characters (.,&'-)");
        } else if (companyName.length() < 3 || companyName.length() > 100) {
            errors.add("Company name must be between 3 and 100 characters");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public void validateTaxNumber(String taxNumber) {
        List<String> errors = new ArrayList<>();
        if (taxNumber == null || taxNumber.trim().isEmpty()) {
            errors.add("Tax number cannot be empty");
        } else if (!TAX_NUMBER_PATTERN.matcher(taxNumber).matches()) {
            errors.add("Invalid tax number format. Must be 10 digits and cannot start with 0");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public void validateBirthDate(LocalDate birthDate) {
        List<String> errors = new ArrayList<>();
        if (birthDate == null) {
            errors.add("Birth date cannot be empty");
        } else {
            LocalDate now = LocalDate.now();
            if (birthDate.isAfter(now)) {
                errors.add("Birth date cannot be in the future");
            } else if (birthDate.isBefore(now.minusYears(120))) {
                errors.add("Invalid birth date");
            } else if (birthDate.isAfter(now.minusYears(18))) {
                errors.add("Customer must be at least 18 years old");
            }
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public void validatePhoneNumber(String phoneNumber) {
        List<String> errors = new ArrayList<>();
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            errors.add("Phone number cannot be empty");
        } else if (!PHONE_PATTERN.matcher(phoneNumber).matches()) {
            errors.add("Invalid phone number format. Must start with +90 followed by 10 digits");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public void validateEmail(String email) {
        List<String> errors = new ArrayList<>();
        if (email == null || email.trim().isEmpty()) {
            errors.add("Email cannot be empty");
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            errors.add("Invalid email format");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public void validateUpdateIndividualCustomer(UpdateIndividualCustomerCommand command) {
        List<String> errors = new ArrayList<>();

        validateId(command.getId());

        if (command.getEmail() != null && !command.getEmail().trim().isEmpty()) {
            validateEmail(command.getEmail());
        }

        if (command.getPhoneNumber() != null && !command.getPhoneNumber().trim().isEmpty()) {
            validatePhoneNumber(command.getPhoneNumber());
        }

        if (command.getIdentityNumber() != null && !command.getIdentityNumber().trim().isEmpty()) {
            validateIdentityNumber(command.getIdentityNumber());
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

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public void validateUpdateCorporateCustomer(UpdateCorporateCustomerCommand command) {
        List<String> errors = new ArrayList<>();

        validateId(command.getId());

        if (command.getEmail() != null && !command.getEmail().trim().isEmpty()) {
            validateEmail(command.getEmail());
        }

        if (command.getPhoneNumber() != null && !command.getPhoneNumber().trim().isEmpty()) {
            validatePhoneNumber(command.getPhoneNumber());
        }

        if (command.getTaxNumber() != null && !command.getTaxNumber().trim().isEmpty()) {
            validateTaxNumber(command.getTaxNumber());
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

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public void validateCreateIndividualCustomer(CreateIndividualCustomerCommand command) {
        List<String> errors = new ArrayList<>();

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
            errors.add("Address cannot be empty");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public void validateCreateCorporateCustomer(CreateCorporateCustomerCommand command) {
        List<String> errors = new ArrayList<>();

        validateEmail(command.getEmail());
        validatePhoneNumber(command.getPhoneNumber());
        validateTaxNumber(command.getTaxNumber());
        validateCompanyName(command.getCompanyName());
        validateName(command.getContactPersonName(), "Contact person name");
        validateName(command.getContactPersonSurname(), "Contact person surname");

        if (command.getAddress() == null) {
            errors.add("Address cannot be empty");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}