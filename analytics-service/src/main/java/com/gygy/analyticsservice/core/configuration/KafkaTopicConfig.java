package com.gygy.analyticsservice.core.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "kafka.topics")
@Data
public class KafkaTopicConfig {
    private String userActivityTopic;
    private String systemMetricsTopic;
    private String planUsageTopic;
}