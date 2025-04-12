package com.gygy.customerservice.application.individualCustomer.controller;

import an.awesome.pipelinr.Pipeline;
import com.gygy.customerservice.application.individualCustomer.command.create.CreateIndividualCustomerCommand;
import com.gygy.customerservice.application.individualCustomer.command.create.CreatedIndividualCustomerResponse;
import com.gygy.customerservice.application.individualCustomer.command.update.UpdateIndividualCustomerCommand;
import com.gygy.customerservice.application.individualCustomer.command.update.UpdatedIndividualCustomerResponse;
import com.gygy.customerservice.application.individualCustomer.query.GetListIndividualCustomerItemDto;
import com.gygy.customerservice.application.individualCustomer.query.GetListIndividualCustomerQuery;
import com.gygy.customerservice.core.web.BaseController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/individual-customers")
public class IndividualCustomerController extends BaseController {

    public IndividualCustomerController(Pipeline pipeline) {
        super(pipeline);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GetListIndividualCustomerItemDto> getAllIndividualCustomers() {
        GetListIndividualCustomerQuery query = new GetListIndividualCustomerQuery();
        return query.execute(pipeline);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedIndividualCustomerResponse createIndividualCustomer(@RequestBody CreateIndividualCustomerCommand command) {
        return command.execute(pipeline);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public UpdatedIndividualCustomerResponse updateIndividualCustomer(@RequestBody UpdateIndividualCustomerCommand command) {
        return command.execute(pipeline);
    }
}
