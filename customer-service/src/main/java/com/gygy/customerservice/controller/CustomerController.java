package com.gygy.customerservice.controller;

import an.awesome.pipelinr.Pipeline;
import com.gygy.customerservice.application.customer.command.delete.DeleteCustomerCommand;
import com.gygy.customerservice.application.customer.command.delete.DeletedCustomerResponse;
import com.gygy.customerservice.application.customer.query.*;
import com.gygy.customerservice.core.web.BaseController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


     @DeleteMapping
     @ResponseStatus(HttpStatus.NO_CONTENT)
     public DeletedCustomerResponse deleteCustomer(@RequestBody DeleteCustomerCommand command) {
         return command.execute(pipeline);
     }
}
