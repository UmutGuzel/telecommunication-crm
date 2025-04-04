package com.gygy.customerservice.application.corporateCustomer.command.update;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.gygy.customerservice.application.corporateCustomer.mapper.CorporateCustomerMapper;
import com.gygy.customerservice.application.customer.dto.UpdateAddressDto;
import com.gygy.customerservice.application.customer.mapper.AddressMapper;
import com.gygy.customerservice.application.customer.rule.CustomerRule;
import com.gygy.customerservice.application.customer.validation.AddressValidation;
import com.gygy.customerservice.application.customer.validation.CustomerValidation;
import com.gygy.customerservice.domain.entity.CorporateCustomer;
import com.gygy.customerservice.infrastructure.messaging.event.UpdatedCorporateCustomerEvent;
import com.gygy.customerservice.infrastructure.messaging.service.KafkaProducerService;
import com.gygy.customerservice.persistance.repository.CorporateCustomerRepository;

import an.awesome.pipelinr.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCorporateCustomerCommand implements Command<UpdatedCorporateCustomerResponse> {

    private UUID id;
    private String email;
    private String phoneNumber;

    private String taxNumber;
    private String companyName;
    private String contactPersonName;
    private String contactPersonSurname;

    private UpdateAddressDto address;

    @Component
    @RequiredArgsConstructor
    public static class UpdateCorporateCustomerCommandHandler implements Command.Handler<UpdateCorporateCustomerCommand, UpdatedCorporateCustomerResponse> {
        private final CorporateCustomerRepository corporateCustomerRepository;
        private final CorporateCustomerMapper corporateCustomerMapper;
        private final CustomerRule customerRule;
        private final CustomerValidation customerValidation;
        private final AddressValidation addressValidation;
        private final KafkaProducerService kafkaProducerService;

        @Override
        public UpdatedCorporateCustomerResponse handle(UpdateCorporateCustomerCommand command) {
            // Validate all fields at once
            customerValidation.validateUpdateCorporateCustomer(command);

            // Validate address if provided
            if (command.getAddress() != null) {
                addressValidation.validateUpdateAddress(command.getAddress());
            }

            CorporateCustomer corporateCustomer = corporateCustomerRepository.findById(command.getId()).orElse(null);
            customerRule.checkCustomerExists(corporateCustomer);

            corporateCustomerMapper.updateCorporateCustomer(corporateCustomer, command);
            CorporateCustomer updatedCustomer = corporateCustomerRepository.save(corporateCustomer);

            kafkaProducerService.sendUpdatedCorporateCustomerEvent(UpdatedCorporateCustomerEvent.builder()
                    .id(updatedCustomer.getId())
                    .email(updatedCustomer.getEmail())
                    .phoneNumber(updatedCustomer.getPhoneNumber())
                    .taxNumber(updatedCustomer.getTaxNumber())
                    .companyName(updatedCustomer.getCompanyName())
                    .contactPersonName(updatedCustomer.getContactPersonName())
                    .contactPersonSurname(updatedCustomer.getContactPersonSurname())
                    .build());

            return corporateCustomerMapper.convertCorporateCustomerToUpdatedCorporateCustomerResponse(updatedCustomer);
        }
    }
}
