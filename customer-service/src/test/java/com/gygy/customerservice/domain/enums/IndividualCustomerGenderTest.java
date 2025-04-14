package com.gygy.customerservice.domain.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IndividualCustomerGenderTest {

    @Test
    void testIndividualCustomerGenderValues() {
        // Given
        IndividualCustomerGender male = IndividualCustomerGender.MALE;
        IndividualCustomerGender female = IndividualCustomerGender.FEMALE;

        // Then
        assertEquals("MALE", male.name());
        assertEquals("FEMALE", female.name());
        assertEquals(2, IndividualCustomerGender.values().length);
    }

    @Test
    void testIndividualCustomerGenderValueOf() {
        // When
        IndividualCustomerGender male = IndividualCustomerGender.valueOf("MALE");
        IndividualCustomerGender female = IndividualCustomerGender.valueOf("FEMALE");

        // Then
        assertEquals(IndividualCustomerGender.MALE, male);
        assertEquals(IndividualCustomerGender.FEMALE, female);
    }
} 