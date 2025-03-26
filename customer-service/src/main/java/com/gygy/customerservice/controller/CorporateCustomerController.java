package com.gygy.customerservice.controller;

import an.awesome.pipelinr.Pipeline;
import com.gygy.customerservice.application.corporateCustomer.command.create.CreateCorporateCustomerCommand;
import com.gygy.customerservice.application.corporateCustomer.command.create.CreatedCorporateCustomerResponse;
import com.gygy.customerservice.application.corporateCustomer.command.delete.DeleteCorporateCustomerCommand;
import com.gygy.customerservice.application.corporateCustomer.command.delete.DeletedCorporateCustomerResponse;
import com.gygy.customerservice.application.corporateCustomer.command.update.UpdateCorporateCustomerCommand;
import com.gygy.customerservice.application.corporateCustomer.command.update.UpdatedCorporateCustomerResponse;
import com.gygy.customerservice.application.corporateCustomer.query.GetListCorporateCustomerItemDto;
import com.gygy.customerservice.application.corporateCustomer.query.GetListCorporateCustomerQuery;
import com.gygy.customerservice.core.web.BaseController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/corporate-customers")
public class CorporateCustomerController extends BaseController {
    public CorporateCustomerController(Pipeline pipeline) {
        super(pipeline);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GetListCorporateCustomerItemDto> getAllCorporateCustomers() {
        GetListCorporateCustomerQuery query = new GetListCorporateCustomerQuery();
        return query.execute(pipeline);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedCorporateCustomerResponse createCorporateCustomer(@RequestBody CreateCorporateCustomerCommand command) {
        return command.execute(pipeline);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public UpdatedCorporateCustomerResponse updateCorporateCustomer(@RequestBody UpdateCorporateCustomerCommand command) {
        return command.execute(pipeline);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public DeletedCorporateCustomerResponse deleteCustomer(@RequestBody DeleteCorporateCustomerCommand command) {
        return command.execute(pipeline);
    }
}
