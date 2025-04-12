package com.gygy.customerservice.application.customer.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gygy.customerservice.application.customer.command.delete.DeleteCustomerCommand;
import com.gygy.customerservice.application.customer.command.delete.DeletedCustomerResponse;
import com.gygy.customerservice.application.customer.query.GetCustomerByEmailQuery;
import com.gygy.customerservice.application.customer.query.GetCustomerByEmailResponse;
import com.gygy.customerservice.application.customer.query.GetCustomerByPhoneNumberQuery;
import com.gygy.customerservice.application.customer.query.GetCustomerByPhoneNumberResponse;
import com.gygy.customerservice.application.customer.query.GetListCustomerItemDto;
import com.gygy.customerservice.application.customer.query.GetListCustomerQuery;
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

    @GetMapping("/by-email/{email}")
    @ResponseStatus(HttpStatus.OK)
    public GetCustomerByEmailResponse getCustomerByEmail(@PathVariable String email) {
        GetCustomerByEmailQuery query = new GetCustomerByEmailQuery(email);
        return query.execute(pipeline);
    }

    @GetMapping("/by-phone/{phoneNumber}")
    @ResponseStatus(HttpStatus.OK)
    public GetCustomerByPhoneNumberResponse getCustomerByPhoneNumber(@PathVariable String phoneNumber) {
        GetCustomerByPhoneNumberQuery query = new GetCustomerByPhoneNumberQuery(phoneNumber);
        return query.execute(pipeline);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public DeletedCustomerResponse deleteCustomer(@RequestBody DeleteCustomerCommand command) {
        return command.execute(pipeline);
    }
}
