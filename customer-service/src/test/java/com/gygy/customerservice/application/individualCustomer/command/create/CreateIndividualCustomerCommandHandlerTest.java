package com.gygy.customerservice.application.individualCustomer.command.create;

import com.gygy.customerservice.application.customer.dto.CreateAddressDto;
import com.gygy.customerservice.application.customer.mapper.AddressMapper;
import com.gygy.customerservice.application.customer.rule.CustomerRule;
import com.gygy.customerservice.application.customer.validation.AddressValidation;
import com.gygy.customerservice.application.customer.validation.CustomerValidation;
import com.gygy.customerservice.application.individualCustomer.mapper.IndividualCustomerMapper;
import com.gygy.customerservice.domain.entity.Address;
import com.gygy.customerservice.domain.entity.IndividualCustomer;
import com.gygy.customerservice.domain.enums.IndividualCustomerGender;
import com.gygy.customerservice.infrastructure.messaging.service.KafkaProducerService;
import com.gygy.customerservice.infrastructure.persistence.repository.AddressRepository;
import com.gygy.customerservice.infrastructure.persistence.repository.IndividualCustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CreateIndividualCustomerCommandHandlerTest {

    @Mock
    private IndividualCustomerRepository individualCustomerRepository;

    @Mock
    private IndividualCustomerMapper individualCustomerMapper;

    @Mock
    private CustomerRule customerRule;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private AddressMapper addressMapper;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @Mock
    private CustomerValidation customerValidation;

    @Mock
    private AddressValidation addressValidation;

    @InjectMocks
    private CreateIndividualCustomerCommand.CreateIndividualCustomerCommandHandler handler;

    @Test
    void testHandleCreateIndividualCustomerCommand() {
        // Given
        CreateIndividualCustomerCommand command = new CreateIndividualCustomerCommand();
        command.setEmail("test@example.com");
        command.setPhoneNumber("5551234567");
        command.setIdentityNumber("12345678901");
        command.setName("John");
        command.setSurname("Doe");
        command.setFatherName("Michael");
        command.setMotherName("Sarah");
        command.setGender(IndividualCustomerGender.MALE);
        command.setBirthDate(LocalDate.of(1990, 1, 1));

        CreateAddressDto addressDto = new CreateAddressDto();
        addressDto.setStreet("Test Street");
        addressDto.setDistrict("Test District");
        addressDto.setApartmentNumber("123");
        addressDto.setCity("Test City");
        addressDto.setCountry("Test Country");
        command.setAddress(addressDto);

        Address savedAddress = new Address();
        savedAddress.setId(UUID.randomUUID());
        
        IndividualCustomer savedCustomer = new IndividualCustomer();
        savedCustomer.setId(UUID.randomUUID());
        savedCustomer.setAddress(savedAddress);

        CreatedIndividualCustomerResponse expectedResponse = new CreatedIndividualCustomerResponse();
        expectedResponse.setId(savedCustomer.getId());

        when(addressMapper.convertCreateAddressDtoToAddress(any())).thenReturn(savedAddress);
        when(addressRepository.save(any())).thenReturn(savedAddress);
        when(individualCustomerMapper.convertCreateCommandToIndividualCustomer(any())).thenReturn(savedCustomer);
        when(individualCustomerRepository.save(any())).thenReturn(savedCustomer);
        when(individualCustomerMapper.convertIndividualCustomerToResponse(any())).thenReturn(expectedResponse);

        // When
        CreatedIndividualCustomerResponse response = handler.handle(command);

        // Then
        assertNotNull(response);
        assertEquals(expectedResponse.getId(), response.getId());

        verify(customerValidation, times(1)).validateCreateIndividualCustomer(command);
        verify(addressValidation, times(1)).validateCreateAddress(addressDto);
        verify(customerRule, times(1)).validateIndividualCustomer(
            command.getEmail(), 
            command.getPhoneNumber(), 
            command.getIdentityNumber()
        );
        verify(addressRepository, times(1)).save(savedAddress);
        verify(individualCustomerRepository, times(1)).save(savedCustomer);
        verify(kafkaProducerService, times(1)).sendCreatedIndividualCustomerEvent(any());
        verify(kafkaProducerService, times(1)).sendCreatedIndividualCustomerReadEvent(any());
    }
} 