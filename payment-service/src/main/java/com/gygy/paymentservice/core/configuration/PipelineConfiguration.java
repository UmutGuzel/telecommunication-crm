package com.gygy.paymentservice.core.configuration;

import an.awesome.pipelinr.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;


@Configuration
public class PipelineConfiguration {

    @Bean
    Pipeline pipeline(ObjectProvider<Command.Handler> commandHandlers,
                      ObjectProvider<Notification.Handler> notificationHandlers,
                      ObjectProvider<Command.Middleware> middlewares) {
        return new Pipelinr()
                .with((CommandHandlers) commandHandlers::stream)
                .with((NotificationHandlers) notificationHandlers::stream)
                .with((Command.Middlewares) middlewares::stream);
    }
}
