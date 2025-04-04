package com.gygy.userservice.core.pipelines;

import an.awesome.pipelinr.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@Slf4j
public class LoggingPipelineBehavior implements Command.Middleware {

    @Override
    public <R, C extends Command<R>> R invoke(C command, Next<R> next) {
        String commandName = command.getClass().getSimpleName();

        try {
            log.info("Executing command: {} with payload: {}", commandName, command);
            long startTime = System.currentTimeMillis();

            R response = next.invoke();

            long executionTime = System.currentTimeMillis() - startTime;
            log.info("Command {} completed in {}ms with response: {}", commandName, executionTime, response);

            return response;
        } catch (Exception e) {
            log.error("Command {} failed with error: {}", commandName, e.getMessage(), e);
            throw e;
        }
    }
}