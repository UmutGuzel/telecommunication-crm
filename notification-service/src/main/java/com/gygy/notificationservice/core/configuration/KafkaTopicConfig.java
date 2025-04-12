package com.gygy.notificationservice.core.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "kafka.topics")
@Data
public class KafkaTopicConfig {
    private String passwordResetEventsTopic;
    private String userActivationEventsTopic;
    private String contractDetailCreatedTopic;
    private String billCreatedEventsTopic;
    private String billOverdueEventsTopic;
    private String billPaidEventsTopic;
    private String paymentFailedEventsTopic;
    private String paymentSuccessEventsTopic;
    private String userRoleChangedTopic;
    private String userPermissionChangedTopic;
    private String individualCustomerCreatedTopic;
    private String individualCustomerUpdatedTopic;
    private String corporateCustomerCreatedTopic;
    private String corporateCustomerUpdatedTopic;
    private String planCampaignTopic;
}
