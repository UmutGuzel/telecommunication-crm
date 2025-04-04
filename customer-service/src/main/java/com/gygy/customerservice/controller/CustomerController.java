package com.gygy.customerservice.controller;

import java.util.List;

import com.gygy.customerservice.application.customer.command.delete.DeleteCustomerCommand;
import com.gygy.customerservice.application.customer.command.delete.DeletedCustomerResponse;
import com.gygy.customerservice.application.customer.command.update.UpdateCustomerCommand;
import com.gygy.customerservice.application.customer.command.update.UpdatedCustomerResponse;
import com.gygy.customerservice.application.customer.query.GetListCustomerItemDto;
import com.gygy.customerservice.application.customer.query.GetListCustomerQuery;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import com.gygy.customerservice.application.customer.command.create.CreateCustomerCommand;
import com.gygy.customerservice.application.customer.command.create.CreatedCustomerResponse;
import com.gygy.customerservice.core.web.BaseController;

import an.awesome.pipelinr.Pipeline;


@RestController
@RequestMapping("api/v1/customers")
public class CustomerController extends BaseController {

    public CustomerController(Pipeline pipeline) {
        super(pipeline);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GetListCustomerItemDto> getAllCustomers() {
        GetListCustomerQuery query = new GetListCustomerQuery();
        return query.execute(pipeline);
    }

//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public CreatedCustomerResponse createCustomer(@RequestBody CreateCustomerCommand command) {
//        return command.execute(pipeline);
//    }
//
//    @PutMapping
//    @ResponseStatus(HttpStatus.OK)
//    public UpdatedCustomerResponse updateCustomer(@RequestBody UpdateCustomerCommand command) {
//        return command.execute(pipeline);
//    }
//
//    @DeleteMapping
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public DeletedCustomerResponse deleteCustomer(@RequestBody DeleteCustomerCommand command) {
//        return command.execute(pipeline);
//    }
}
