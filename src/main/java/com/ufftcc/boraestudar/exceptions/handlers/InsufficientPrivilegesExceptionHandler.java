package com.ufftcc.boraestudar.exceptions.handlers;

import com.ufftcc.boraestudar.exceptions.InsufficientPrivilegesException;
import com.ufftcc.boraestudar.exceptions.JsonMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InsufficientPrivilegesExceptionHandler {

    @ExceptionHandler({InsufficientPrivilegesException.class})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public JsonMessage insufficientPrivilegesExceptionHandler(InsufficientPrivilegesException ex) {
        return new JsonMessage(ex.getMessage());
    }
}