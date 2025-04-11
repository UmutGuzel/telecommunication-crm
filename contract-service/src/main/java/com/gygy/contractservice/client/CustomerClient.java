package com.gygy.contractservice.client;

import com.gygy.customerservice.application.customer.query.GetCustomerByPhoneNumberResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name = "customer-service",url = "http://localhost:9020")
public interface CustomerClient {
    @GetMapping("/api/v1/customers/by-phone/{phoneNumber}")
    GetCustomerByPhoneNumberResponse getCustomerByPhoneNumber(String phoneNumber);

}

    





