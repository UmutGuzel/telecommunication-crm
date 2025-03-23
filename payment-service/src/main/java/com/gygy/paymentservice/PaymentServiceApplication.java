package com.gygy.paymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.gygy.paymentservice.presentation.controllers",
        "com.gygy.paymentservice.application",
        "com.gygy.paymentservice.persistence",
        "com.gygy.paymentservice.core.configuration"
})
@EntityScan(basePackages = "com.gygy.paymentservice.domain.entity")
@EnableJpaRepositories(basePackages = "com.gygy.paymentservice.persistence")
@EnableScheduling
public class PaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
    }

}
