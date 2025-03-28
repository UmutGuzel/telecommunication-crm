package com.gygy.customerservice.application.corporateCustomer.command.delete;

import an.awesome.pipelinr.Command;
import com.gygy.customerservice.application.corporateCustomer.mapper.CorporateCustomerMapper;
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
public class DeleteCorporateCustomerCommand implements Command<DeletedCorporateCustomerResponse> {
    private UUID id;

    @Component
    @RequiredArgsConstructor
    public static class DeleteCorporateCustomerCommandHandler implements Command.Handler<DeleteCorporateCustomerCommand, DeletedCorporateCustomerResponse> {
        private final CorporateCustomerRepository corporateCustomerRepository;
        private final CustomerRule customerRule;
        private final CorporateCustomerMapper corporateCustomerMapper;

        @Override
        public DeletedCorporateCustomerResponse handle(DeleteCorporateCustomerCommand command) {
            CorporateCustomer corporateCustomer = corporateCustomerRepository.findById(command.getId()).orElse(null);
            customerRule.checkCustomerExists(corporateCustomer);
            corporateCustomerRepository.delete(corporateCustomer);
            return corporateCustomerMapper.convertCorporateCustomerToDeletedCorporateCustomerResponse(corporateCustomer);
        }
    }
}
