package com.gygy.customerservice.application.corporateCustomer.controller;

import an.awesome.pipelinr.Pipeline;
import com.gygy.customerservice.application.corporateCustomer.query.read.GetListCorporateCustomerItemReadDto;
import com.gygy.customerservice.application.corporateCustomer.query.read.GetListCorporateCustomerReadQuery;
import com.gygy.customerservice.core.web.BaseController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/read/corporate-customers")
public class CorporateCustomerReadController extends BaseController {

    public CorporateCustomerReadController(Pipeline pipeline) {
        super(pipeline);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GetListCorporateCustomerItemReadDto> getAllCorporateCustomersReadDb() {
        GetListCorporateCustomerReadQuery query = new GetListCorporateCustomerReadQuery();
        return query.execute(pipeline);
    }
}
