package com.gygy.notificationservice.core.configuration;

import com.gygy.common.events.PasswordResetEvent;
import com.gygy.common.events.UserActivationEvent;
import com.gygy.notificationservice.tempdto.*;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    private <T> ConsumerFactory<String, T> createConsumerFactory(Class<T> clazz) {
        JsonDeserializer<T> deserializer = new JsonDeserializer<>(clazz);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    private <T> ConcurrentKafkaListenerContainerFactory<String, T> createFactory(Class<T> clazz) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(createConsumerFactory(clazz));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PasswordResetEvent> passwordResetKafkaListenerContainerFactory() {
        return createFactory(PasswordResetEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserActivationEvent> userActivationKafkaListenerContainerFactory() {
        return createFactory(UserActivationEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ContractDetailEvent> contractDetailKafkaListenerContainerFactory() {
        return createFactory(ContractDetailEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BillCreatedEvent> billCreatedKafkaListenerContainerFactory() {
        return createFactory(BillCreatedEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BillOverdueEvent> billOverdueKafkaListenerContainerFactory() {
        return createFactory(BillOverdueEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BillPaidEvent> billPaidKafkaListenerContainerFactory() {
        return createFactory(BillPaidEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentFailedEvent> paymentFailedKafkaListenerContainerFactory() {
        return createFactory(PaymentFailedEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentSuccessEvent> paymentSuccessKafkaListenerContainerFactory() {
        return createFactory(PaymentSuccessEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserRoleChangedEvent> userRoleChangedKafkaListenerContainerFactory() {
        return createFactory(UserRoleChangedEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserPermissionChangedEvent> userPermissionChangedKafkaListenerContainerFactory() {
        return createFactory(UserPermissionChangedEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, IndividualCustomerCreatedEvent> individualCustomerCreatedKafkaListenerContainerFactory() {
        return createFactory(IndividualCustomerCreatedEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, IndividualCustomerUpdatedEvent> individualCustomerUpdatedKafkaListenerContainerFactory() {
        return createFactory(IndividualCustomerUpdatedEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CorporateCustomerCreatedEvent> corporateCustomerCreatedKafkaListenerContainerFactory() {
        return createFactory(CorporateCustomerCreatedEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CorporateCustomerUpdatedEvent> corporateCustomerUpdatedKafkaListenerContainerFactory() {
        return createFactory(CorporateCustomerUpdatedEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PlanCampaignEvent> planCampaignKafkaListenerContainerFactory() {
        return createFactory(PlanCampaignEvent.class);
    }

    @Bean
    public NewTopic passwordResetEventsTopic(@Value("${kafka.topics.passwordResetEventsTopic}") String topic) {
        return new NewTopic(topic, 1, (short) 1);
    }

    @Bean
    public NewTopic userActivationEventsTopic(@Value("${kafka.topics.userActivationEventsTopic}") String topic) {
        return new NewTopic(topic, 1, (short) 1);
    }

    @Bean
    public NewTopic contractDetailCreatedTopic(@Value("${kafka.topics.contractDetailCreatedTopic}") String topic) {
        return new NewTopic(topic, 1, (short) 1);
    }

    @Bean
    public NewTopic billCreatedEventsTopic(@Value("${kafka.topics.billCreatedEventsTopic}") String topic) {
        return new NewTopic(topic, 1, (short) 1);
    }

    @Bean
    public NewTopic billOverdueEventsTopic(@Value("${kafka.topics.billOverdueEventsTopic}") String topic) {
        return new NewTopic(topic, 1, (short) 1);
    }

    @Bean
    public NewTopic billPaidEventsTopic(@Value("${kafka.topics.billPaidEventsTopic}") String topic) {
        return new NewTopic(topic, 1, (short) 1);
    }

    @Bean
    public NewTopic paymentFailedEventsTopic(@Value("${kafka.topics.paymentFailedEventsTopic}") String topic) {
        return new NewTopic(topic, 1, (short) 1);
    }

    @Bean
    public NewTopic paymentSuccessEventsTopic(@Value("${kafka.topics.paymentSuccessEventsTopic}") String topic) {
        return new NewTopic(topic, 1, (short) 1);
    }

    @Bean
    public NewTopic userRoleChangedTopic(@Value("${kafka.topics.userRoleChangedTopic}") String topic) {
        return new NewTopic(topic, 1, (short) 1);
    }

    @Bean
    public NewTopic userPermissionChangedTopic(@Value("${kafka.topics.userPermissionChangedTopic}") String topic) {
        return new NewTopic(topic, 1, (short) 1);
    }

    @Bean
    public NewTopic individualCustomerCreatedTopic(
            @Value("${kafka.topics.individualCustomerCreatedTopic}") String topic) {
        return new NewTopic(topic, 1, (short) 1);
    }

    @Bean
    public NewTopic individualCustomerUpdatedTopic(
            @Value("${kafka.topics.individualCustomerUpdatedTopic}") String topic) {
        return new NewTopic(topic, 1, (short) 1);
    }

    @Bean
    public NewTopic corporateCustomerCreatedTopic(
            @Value("${kafka.topics.corporateCustomerCreatedTopic}") String topic) {
        return new NewTopic(topic, 1, (short) 1);
    }

    @Bean
    public NewTopic corporateCustomerUpdatedTopic(
            @Value("${kafka.topics.corporateCustomerUpdatedTopic}") String topic) {
        return new NewTopic(topic, 1, (short) 1);
    }

    @Bean
    public NewTopic planCampaignTopic(@Value("${kafka.topics.planCampaignTopic}") String topic) {
        return new NewTopic(topic, 1, (short) 1);
    }
}
