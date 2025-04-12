package com.gygy.customerservice.application.customer.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gygy.customerservice.application.customer.query.GetCustomerByEmailResponse;
import com.gygy.customerservice.application.customer.query.GetCustomerByPhoneNumberResponse;
import com.gygy.customerservice.application.customer.query.GetListCustomerItemDto;
import com.gygy.customerservice.application.customer.query.read.GetCustomerByEmailReadQuery;
import com.gygy.customerservice.application.customer.query.read.GetCustomerByPhoneNumberReadQuery;
import com.gygy.customerservice.application.customer.query.read.GetListCustomerReadQuery;
import com.gygy.customerservice.core.web.BaseController;

import an.awesome.pipelinr.Pipeline;

@RestController
@RequestMapping("api/v1/customers/read")
public class CustomerReadController extends BaseController {

    public CustomerReadController(Pipeline pipeline) {
        super(pipeline);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GetListCustomerItemDto> getAllCustomersReadDb() {
        GetListCustomerReadQuery query = new GetListCustomerReadQuery();
        return query.execute(pipeline);
    }

    @GetMapping("/by-email/{email}")
    @ResponseStatus(HttpStatus.OK)
    public GetCustomerByEmailResponse getCustomerByEmailReadDb(@PathVariable String email) {
        GetCustomerByEmailReadQuery query = new GetCustomerByEmailReadQuery(email);
        return query.execute(pipeline);
    }

    @GetMapping("/by-phone/{phoneNumber}")
    @ResponseStatus(HttpStatus.OK)
    public GetCustomerByPhoneNumberResponse getCustomerByPhoneNumberReadDb(@PathVariable String phoneNumber) {
        GetCustomerByPhoneNumberReadQuery query = new GetCustomerByPhoneNumberReadQuery(phoneNumber);
        return query.execute(pipeline);
    }
} 