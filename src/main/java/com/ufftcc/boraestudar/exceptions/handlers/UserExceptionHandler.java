package com.ufftcc.boraestudar.exceptions.handlers;

import com.ufftcc.boraestudar.exceptions.JsonMessage;
import com.ufftcc.boraestudar.exceptions.subject.SubjectNotFoundException;
import com.ufftcc.boraestudar.exceptions.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public JsonMessage userNotFoundExceptionHandle(UserNotFoundException ex) {
        return new JsonMessage(ex.getMessage());
    }
}