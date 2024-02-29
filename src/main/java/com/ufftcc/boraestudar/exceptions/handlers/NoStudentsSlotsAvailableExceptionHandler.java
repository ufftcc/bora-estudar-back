package com.ufftcc.boraestudar.exceptions.handlers;

import com.ufftcc.boraestudar.exceptions.JsonMessage;
import com.ufftcc.boraestudar.exceptions.study_group.NoStudentsSlotsAvailableException;
import com.ufftcc.boraestudar.exceptions.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NoStudentsSlotsAvailableExceptionHandler {

    @ExceptionHandler({NoStudentsSlotsAvailableException.class})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public JsonMessage noStudentsSlotsAvailableExceptionHandler(NoStudentsSlotsAvailableException ex) {
        return new JsonMessage(ex.getMessage());
    }
}