package com.gygy.customerservice.domain.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    @Test
    void testAddressCreation() {
        // Given
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        
        // When
        Address address = new Address();
        address.setId(id);
        address.setStreet("Test Street");
        address.setDistrict("Test District");
        address.setApartmentNumber("123");
        address.setCity("Test City");
        address.setCountry("Test Country");
        address.setCreatedAt(now);
        address.setUpdatedAt(now);

        // Then
        assertNotNull(address);
        assertEquals(id, address.getId());
        assertEquals("Test Street", address.getStreet());
        assertEquals("Test District", address.getDistrict());
        assertEquals("123", address.getApartmentNumber());
        assertEquals("Test City", address.getCity());
        assertEquals("Test Country", address.getCountry());
        assertEquals(now, address.getCreatedAt());
        assertEquals(now, address.getUpdatedAt());
    }

    @Test
    void testAddressSetters() {
        // Given
        Address address = new Address();
        LocalDateTime now = LocalDateTime.now();

        // When
        address.setStreet("New Street");
        address.setDistrict("New District");
        address.setApartmentNumber("456");
        address.setCity("New City");
        address.setCountry("New Country");
        address.setCreatedAt(now);
        address.setUpdatedAt(now);

        // Then
        assertEquals("New Street", address.getStreet());
        assertEquals("New District", address.getDistrict());
        assertEquals("456", address.getApartmentNumber());
        assertEquals("New City", address.getCity());
        assertEquals("New Country", address.getCountry());
        assertEquals(now, address.getCreatedAt());
        assertEquals(now, address.getUpdatedAt());
    }
} 