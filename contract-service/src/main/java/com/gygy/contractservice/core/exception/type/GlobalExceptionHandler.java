package com.gygy.contractservice.core.exception.type;

import com.gygy.contractservice.core.exception.result.BusinessExceptionResult;
import com.gygy.contractservice.core.exception.result.ExceptionResult;
import com.gygy.contractservice.core.exception.result.NotFoundExceptionResult;
import com.gygy.contractservice.core.exception.result.ValidationExceptionResult;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({Exception.class})
    @ResponseStatus(code= HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResult handleException(Exception e) {return new ExceptionResult("Internal Server Error");}
    @ExceptionHandler({BusinessException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BusinessExceptionResult handlerRuntimeException(BusinessException e) {
        return new BusinessExceptionResult(e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationExceptionResult handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ValidationExceptionResult(e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error ->error.getDefaultMessage())
                .toList());
    }
    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public NotFoundExceptionResult handleNotFoundException(ChangeSetPersister.NotFoundException e) {
        return new NotFoundExceptionResult(e.getMessage());
    }
}
