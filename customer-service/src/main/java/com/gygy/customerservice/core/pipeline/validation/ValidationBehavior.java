package com.gygy.customerservice.core.pipeline.validation;

import an.awesome.pipelinr.Command;
import com.gygy.customerservice.core.exception.type.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

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