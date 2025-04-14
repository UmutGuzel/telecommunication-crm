package com.gygy.customerservice.domain.entity;

import com.gygy.customerservice.domain.enums.CustomerType;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void testCustomerCreation() {
        // Given
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        
        // When
        Customer customer = new Customer();
        customer.setId(id);
        customer.setEmail("test@example.com");
        customer.setPhoneNumber("5551234567");
        customer.setType(CustomerType.INDIVIDUAL);
        customer.setCreatedAt(now);
        customer.setUpdatedAt(now);

        // Then
        assertNotNull(customer);
        assertEquals(id, customer.getId());
        assertEquals("test@example.com", customer.getEmail());
        assertEquals("5551234567", customer.getPhoneNumber());
        assertEquals(CustomerType.INDIVIDUAL, customer.getType());
        assertEquals(now, customer.getCreatedAt());
        assertEquals(now, customer.getUpdatedAt());
    }

    @Test
    void testCustomerSetters() {
        // Given
        Customer customer = new Customer();
        LocalDateTime now = LocalDateTime.now();
        UUID id = UUID.randomUUID();

        // When
        customer.setId(id);
        customer.setEmail("new@example.com");
        customer.setPhoneNumber("5559876543");
        customer.setType(CustomerType.CORPORATE);
        customer.setCreatedAt(now);
        customer.setUpdatedAt(now);

        // Then
        assertEquals(id, customer.getId());
        assertEquals("new@example.com", customer.getEmail());
        assertEquals("5559876543", customer.getPhoneNumber());
        assertEquals(CustomerType.CORPORATE, customer.getType());
        assertEquals(now, customer.getCreatedAt());
        assertEquals(now, customer.getUpdatedAt());
    }

    @Test
    void testCustomerAddressAssociation() {
        // Given
        Customer customer = new Customer();
        Address address = new Address();
        address.setId(UUID.randomUUID());

        // When
        customer.setAddress(address);

        // Then
        assertNotNull(customer.getAddress());
        assertEquals(address.getId(), customer.getAddress().getId());
    }
} 