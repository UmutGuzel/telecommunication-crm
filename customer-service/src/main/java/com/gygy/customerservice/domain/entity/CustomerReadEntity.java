package com.gygy.customerservice.domain.entity;

import com.gygy.customerservice.domain.enums.CustomerType;
import com.gygy.customerservice.domain.enums.IndividualCustomerGender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "customers")
public class CustomerReadEntity {
    @Id
    @Field(targetType = FieldType.BINARY)
    private UUID id;
    private String email;
    private String phoneNumber;
    private CustomerType type;

    // Individual Customer fields
    private String name;
    private String surname;
    private IndividualCustomerGender gender;
    private LocalDate birthDate;

    // Corporate Customer fields
    private String companyName;
    private String contactPersonName;
    private String contactPersonSurname;
} 