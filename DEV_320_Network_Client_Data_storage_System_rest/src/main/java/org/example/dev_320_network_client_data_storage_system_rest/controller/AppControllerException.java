package org.example.dev_320_network_client_data_storage_system_rest.controller;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.example.dev_320_network_client_data_storage_system_rest.exceptions.EAppException;
import org.example.dev_320_network_client_data_storage_system_rest.exceptions.MessageError;
import org.example.dev_320_network_client_data_storage_system_rest.exceptions.MessageErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.net.ConnectException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice(annotations = RestController.class)
@Slf4j
public class AppControllerException {

    @ExceptionHandler({ConnectException.class, EAppException.class})
    public MessageError getException(Exception exception){
        return new MessageError(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MessageErrors getException(MethodArgumentNotValidException exception){
        log.error(exception.getMessage());
        exception.printStackTrace();
        List<MessageErrors.Violation> violations = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new MessageErrors.Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return new MessageErrors(violations);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MessageErrors getException(ConstraintViolationException exception){
        log.error(exception.getMessage());
        exception.printStackTrace();
        List<MessageErrors.Violation> violations = exception.getConstraintViolations()
                .stream()
                .map(error -> new MessageErrors.Violation(error.getPropertyPath().toString(), error.getMessage()))
                .collect(Collectors.toList());
        return new MessageErrors(violations);
    }

}