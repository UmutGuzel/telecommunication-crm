package com.gygy.customerservice.application.corporateCustomer.command.update;

import an.awesome.pipelinr.Command;
import com.gygy.customerservice.application.corporateCustomer.mapper.CorporateCustomerMapper;
import com.gygy.customerservice.application.customer.dto.UpdateAddressDto;
import com.gygy.customerservice.application.customer.rule.CustomerRule;
import com.gygy.customerservice.domain.entity.CorporateCustomer;
import com.gygy.customerservice.persistance.repository.CorporateCustomerRepository;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCorporateCustomerCommand implements Command<UpdatedCorporateCustomerResponse> {
    private UUID id;
    private String email;
    private String phoneNumber;
    private UpdateAddressDto address;

    private String taxNumber;
    private String companyName;
    private String contactPersonName;
    private String contactPersonSurname;

    @Component
    @RequiredArgsConstructor
    public static class UpdateCorporateCustomerCommandHandler implements Command.Handler<UpdateCorporateCustomerCommand, UpdatedCorporateCustomerResponse> {
        private final CorporateCustomerRepository corporateCustomerRepository;
        private final CorporateCustomerMapper corporateCustomerMapper;
        private final CustomerRule customerRule;

        @Override
        public UpdatedCorporateCustomerResponse handle(UpdateCorporateCustomerCommand command) {
            CorporateCustomer corporateCustomer = corporateCustomerRepository.findById(command.getId()).orElse(null);
            customerRule.checkCustomerExists(corporateCustomer);

            corporateCustomerMapper.updateCorporateCustomer(corporateCustomer, command);
            corporateCustomerRepository.save(corporateCustomer);
            return corporateCustomerMapper.convertCorporateCustomerToUpdatedCorporateCustomerResponse(corporateCustomer);
        }
    }
}
