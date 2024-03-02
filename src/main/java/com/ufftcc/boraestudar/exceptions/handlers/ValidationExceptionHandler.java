package com.ufftcc.boraestudar.exceptions.handlers;

import com.ufftcc.boraestudar.exceptions.JsonMessage;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Map<String, List<JsonMessage>> validationExceptionHandler (MethodArgumentNotValidException ex) {
        return mapFieldErrors(ex);
    }

    private Map<String, List<JsonMessage>> mapFieldErrors(MethodArgumentNotValidException ex) {
        Map<String, List<JsonMessage>> messagesMap = new HashMap<>();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        for (FieldError fieldError : fieldErrors) {
            String field = fieldError.getField();
            String message = fieldError.getDefaultMessage();

            JsonMessage jsonMessage = new JsonMessage(message);
            createOrAddError(field, jsonMessage, messagesMap);
        }
        return messagesMap;
    }

    private void createOrAddError(String field, JsonMessage errorMessage,
                                  Map<String, List<JsonMessage>> messagesMap) {

        List<JsonMessage> errors = new ArrayList<>();
        if (messagesMap.containsKey(field)) {
            errors.addAll(messagesMap.get(field));
            errors.add(errorMessage);
        } else {
            errors.add(errorMessage);
        }
        messagesMap.put(field, errors);
    }
}
