package com.gygy.customerservice.application.individualCustomer.command.update;

import an.awesome.pipelinr.Command;
import com.gygy.customerservice.application.customer.dto.UpdateAddressDto;
import com.gygy.customerservice.application.customer.rule.CustomerRule;
import com.gygy.customerservice.application.individualCustomer.mapper.IndividualCustomerMapper;
import com.gygy.customerservice.domain.entity.IndividualCustomer;
import com.gygy.customerservice.domain.enums.IndividualCustomerGender;
import com.gygy.customerservice.persistance.repository.IndividualCustomerRepository;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateIndividualCustomerCommand implements Command<UpdatedIndividualCustomerResponse> {

    private UUID id;
    private String email;
    private String phoneNumber;
    private UpdateAddressDto address;

    private String identityNumber;
    private String name;
    private String surname;
    private String fatherName;
    private String motherName;
    private IndividualCustomerGender gender;
    private Date birthDate;

    @Component
    @RequiredArgsConstructor
    public static class UpdateIndividualCustomerCommandHandler implements Command.Handler<UpdateIndividualCustomerCommand, UpdatedIndividualCustomerResponse> {
        private final IndividualCustomerRepository individualCustomerRepository;
        private final IndividualCustomerMapper individualCustomerMapper;
        private final CustomerRule customerRule;

        @Override
        public UpdatedIndividualCustomerResponse handle(UpdateIndividualCustomerCommand command) {
            IndividualCustomer individualCustomer = individualCustomerRepository.findById(command.getId()).orElse(null);
            customerRule.checkCustomerExists(individualCustomer);

            individualCustomerMapper.updateIndividualCustomer(individualCustomer, command);
            individualCustomerRepository.save(individualCustomer);
            return individualCustomerMapper.convertIndividualCustomerToUpdatedIndividualCustomerResponse(individualCustomer);
        }
    }
}
