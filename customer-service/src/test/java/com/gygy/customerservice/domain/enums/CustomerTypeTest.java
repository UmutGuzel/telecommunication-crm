package com.gygy.customerservice.domain.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTypeTest {

    @Test
    void testCustomerTypeValues() {
        // Given
        CustomerType individual = CustomerType.INDIVIDUAL;
        CustomerType corporate = CustomerType.CORPORATE;

        // Then
        assertEquals("INDIVIDUAL", individual.name());
        assertEquals("CORPORATE", corporate.name());
        assertEquals(2, CustomerType.values().length);
    }

    @Test
    void testCustomerTypeValueOf() {
        // When
        CustomerType individual = CustomerType.valueOf("INDIVIDUAL");
        CustomerType corporate = CustomerType.valueOf("CORPORATE");

        // Then
        assertEquals(CustomerType.INDIVIDUAL, individual);
        assertEquals(CustomerType.CORPORATE, corporate);
    }
} 