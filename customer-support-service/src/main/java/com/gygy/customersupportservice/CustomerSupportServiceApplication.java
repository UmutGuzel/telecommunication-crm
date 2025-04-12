package com.gygy.customersupportservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.gygy.common.EnableCommon;

@SpringBootApplication
@EnableCommon
public class CustomerSupportServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerSupportServiceApplication.class, args);
    }

}
