package com.gygy.discountservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@EnableBinding
public class DiscountServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiscountServiceApplication.class, args);
    }
} 