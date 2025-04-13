package com.gygy.debezium;

import java.util.concurrent.ExecutorService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.debezium.engine.DebeziumEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class DebeziumServiceApplication {

    private final DebeziumEngine<?> debeziumEngine;
    private final ExecutorService executorService;

    public static void main(String[] args) {
        SpringApplication.run(DebeziumServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            executorService.execute(debeziumEngine);
            log.info("Debezium engine started");
        };
    }
} 