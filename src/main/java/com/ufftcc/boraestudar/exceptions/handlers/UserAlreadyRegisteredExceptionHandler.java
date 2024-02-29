package com.ufftcc.boraestudar.exceptions.handlers;

import com.ufftcc.boraestudar.exceptions.InsufficientPrivilegesException;
import com.ufftcc.boraestudar.exceptions.JsonMessage;
import com.ufftcc.boraestudar.exceptions.study_group.UserAlreadyRegisteredException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserAlreadyRegisteredExceptionHandler {

    @ExceptionHandler({UserAlreadyRegisteredException.class})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public JsonMessage insufficientPrivilegesExceptionHandler(UserAlreadyRegisteredException ex) {
        return new JsonMessage(ex.getMessage());
    }
}