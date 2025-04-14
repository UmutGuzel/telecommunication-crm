package com.gygy.customerservice.application.individualCustomer.service;

import com.gygy.customerservice.application.individualCustomer.query.GetListIndividualCustomerItemDto;
import com.gygy.customerservice.domain.enums.IndividualCustomerGender;
import com.gygy.customerservice.infrastructure.persistence.repository.IndividualCustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class IndividualCustomerQueryServiceTest {

    @Mock
    private IndividualCustomerRepository individualCustomerRepository;

    @InjectMocks
    private IndividualCustomerQueryService individualCustomerQueryService;

    @Test
    void testGetAllIndividualCustomers() {
        // Given
        GetListIndividualCustomerItemDto customer1 = new GetListIndividualCustomerItemDto(
            UUID.randomUUID(),
            "john.doe@example.com",
            "5551234567",
            "John",
            "Doe",
            null,
            null
        );

        GetListIndividualCustomerItemDto customer2 = new GetListIndividualCustomerItemDto(
            UUID.randomUUID(),
            "jane.smith@example.com",
            "5559876543",
            "Jane",
            "Smith",
            null,
            null
        );

        List<GetListIndividualCustomerItemDto> expectedCustomers = Arrays.asList(customer1, customer2);
        
        when(individualCustomerRepository.findAllAsDto()).thenReturn(expectedCustomers);

        // When
        List<GetListIndividualCustomerItemDto> result = individualCustomerQueryService.getAllIndividualCustomers();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedCustomers, result);
        
        verify(individualCustomerRepository, times(1)).findAllAsDto();
    }
} 