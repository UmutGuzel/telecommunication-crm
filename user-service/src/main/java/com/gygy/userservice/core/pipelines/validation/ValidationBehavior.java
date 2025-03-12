package com.gygy.userservice.core.pipelines.validation;

import com.gygy.userservice.core.pipelines.Exception.ValidationException;

import an.awesome.pipelinr.Command;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidationBehavior implements Command.Middleware {
    private final Validator validator;

    @Override
    public <R, C extends Command<R>> R invoke(C c, Next<R> next) {
        Set<ConstraintViolation<C>> violations = validator.validate(c);
        if (!violations.isEmpty()) {
            var messages = violations.stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .toList();
            throw new ValidationException(messages);
        }
        return next.invoke();
    }
}