package com.gygy.userservice.core.configration;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.ObjectProvider;
import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Pipelinr;
import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Notification;
import org.springframework.context.annotation.Bean;

@Configuration
public class PipelineConfiguration {
    @Bean
    public Pipeline pipeline(ObjectProvider<Command.Handler> handlers,
            ObjectProvider<Notification.Handler> notifications,
            ObjectProvider<Command.Middleware> middlewares) {
        return new Pipelinr()
                .with(() -> handlers.stream())
                .with(() -> notifications.stream())
                .with(() -> middlewares.orderedStream());
    }
}
