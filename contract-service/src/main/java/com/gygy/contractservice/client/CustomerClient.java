package com.gygy.contractservice.client;

import com.gygy.customerservice.application.customer.query.GetCustomerByEmailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name = "customer-service")
public interface CustomerClient {
    @GetMapping("/api/v1/customers/by-email/{email}")
    GetCustomerByEmailResponse getCustomerByEmail(String email);

}

    





