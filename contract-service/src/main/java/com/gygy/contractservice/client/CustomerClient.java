package com.gygy.contractservice.client;

import com.gygy.contractservice.dto.contractDetail.GetCustomerByEmailResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "customer-service", url ="http://customer-service:9020")
public interface CustomerClient {
    @GetMapping("/api/v1/customers/by-email/{email}")
    GetCustomerByEmailResponse getCustomerByEmail(@PathVariable("email") String email);
}








