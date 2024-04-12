package com.ufftcc.boraestudar.exceptions.handlers;

import com.ufftcc.boraestudar.exceptions.JsonMessage;
import com.ufftcc.boraestudar.exceptions.study_group.UserAlreadyRegisteredException;
import com.ufftcc.boraestudar.exceptions.study_group.UserNotRegisteredException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserNotRegisteredExceptionHandler {

    @ExceptionHandler({UserNotRegisteredException.class})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public JsonMessage insufficientPrivilegesExceptionHandler(UserNotRegisteredException ex) {
        return new JsonMessage(ex.getMessage());
    }
}