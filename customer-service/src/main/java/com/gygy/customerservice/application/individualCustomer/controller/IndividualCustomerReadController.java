package com.gygy.customerservice.application.individualCustomer.controller;

import an.awesome.pipelinr.Pipeline;
import com.gygy.customerservice.application.individualCustomer.query.read.GetListIndividualCustomerItemReadDto;
import com.gygy.customerservice.application.individualCustomer.query.read.GetListIndividualCustomerReadQuery;
import com.gygy.customerservice.core.web.BaseController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/read/individual-customers")
public class IndividualCustomerReadController extends BaseController {

    public IndividualCustomerReadController(Pipeline pipeline) {
        super(pipeline);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GetListIndividualCustomerItemReadDto> getAllIndividualCustomersReadDb() {
        GetListIndividualCustomerReadQuery query = new GetListIndividualCustomerReadQuery();
        return query.execute(pipeline);
    }
}
