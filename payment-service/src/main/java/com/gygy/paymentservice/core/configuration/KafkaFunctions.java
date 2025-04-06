package com.gygy.paymentservice.core.configuration;

import lombok.extern.slf4j.Slf4j;
import java.util.function.Function;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaFunctions {

    @Bean
    public Function<Message<?>, Message<?>> billCreatedFunction() {
        return message -> {
            log.info("Bill Created Function çalıştı - Message: {}", message.getPayload());
            return message;
        };
    }

    @Bean
    public Function<Message<?>, Message<?>> billPaidFunction() {
        return message -> {
            log.info("Bill Paid Function çalıştı - Message: {}", message.getPayload());
            return message;
        };
    }

    @Bean
    public Function<Message<?>, Message<?>> billOverdueFunction() {
        return message -> {
            log.info("Bill Overdue Function çalıştı - Message: {}", message.getPayload());
            return message;
        };
    }

    @Bean
    public Function<Message<?>, Message<?>> paymentSuccessFunction() {
        return message -> {
            log.info("Payment Success Function çalıştı - Message: {}", message.getPayload());
            return message;
        };
    }

    @Bean
    public Function<Message<?>, Message<?>> paymentFailedFunction() {
        return message -> {
            log.info("Payment Failed Function çalıştı - Message: {}", message.getPayload());
            return message;
        };
    }
}
