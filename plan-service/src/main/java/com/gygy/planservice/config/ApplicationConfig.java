package com.gygy.planservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;

@Configuration
@Getter
public class ApplicationConfig {

    @Value("${app-config.kafka.topics.plan-campaign-topic}")
    private String planCampaignTopic;
}