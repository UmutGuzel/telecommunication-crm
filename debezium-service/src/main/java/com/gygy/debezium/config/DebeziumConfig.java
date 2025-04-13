package com.gygy.debezium.config;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gygy.debezium.service.DebeziumService;

import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DebeziumConfig {

    private final DebeziumService debeziumService;

    @Bean
    public DebeziumEngine<ChangeEvent<String, String>> debeziumEngine() {
        log.info("Creating Debezium engine with configuration...");
        
        Properties props = new Properties();
        props.setProperty("name", "outbox-connector");
        props.setProperty("connector.class", "io.debezium.connector.postgresql.PostgresConnector");
        props.setProperty("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore");
        props.setProperty("offset.storage.file.filename", "offset.dat");
        props.setProperty("offset.flush.interval.ms", "60000");
        props.setProperty("database.hostname", "localhost");
        props.setProperty("database.port", "5437");
        props.setProperty("database.user", "postgres");
        props.setProperty("database.password", "test");
        props.setProperty("database.dbname", "customer-service");
        props.setProperty("database.server.name", "customer-service");
        props.setProperty("topic.prefix", "debezium");
        props.setProperty("table.include.list", "public.outbox");
        props.setProperty("plugin.name", "pgoutput");
        props.setProperty("slot.name", "outbox_slot");
        props.setProperty("publication.name", "outbox_publication");
        props.setProperty("publication.autocreate.mode", "filtered");
        props.setProperty("key.converter", "org.apache.kafka.connect.json.JsonConverter");
        props.setProperty("value.converter", "org.apache.kafka.connect.json.JsonConverter");
        props.setProperty("transforms", "unwrap");
        props.setProperty("transforms.unwrap.type", "io.debezium.transforms.ExtractNewRecordState");
        props.setProperty("transforms.unwrap.drop.tombstones", "false");
        props.setProperty("transforms.unwrap.delete.handling.mode", "rewrite");
        props.setProperty("transforms.unwrap.add.fields", "op,source.ts_ms");
        props.setProperty("transforms.unwrap.add.headers", "op");

        log.debug("Debezium properties: {}", props);

        return DebeziumEngine.create(Json.class)
                .using(props)
                .notifying(debeziumService::handleEvent)
                .build();
    }

    @Bean
    public ExecutorService executorService() {
        log.info("Creating single thread executor for Debezium engine");
        return Executors.newSingleThreadExecutor();
    }
} 