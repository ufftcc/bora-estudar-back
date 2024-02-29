package com.ufftcc.boraestudar.exceptions.handlers;

import com.ufftcc.boraestudar.exceptions.JsonMessage;
import com.ufftcc.boraestudar.exceptions.study_group.StudyGroupNotFoundException;
import com.ufftcc.boraestudar.exceptions.study_group.TutorAlreadyRegisteredException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TutorAlreadyRegisteredExceptionHandler {

    @ExceptionHandler({TutorAlreadyRegisteredException.class})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public JsonMessage tutorAlreadyRegisteredExceptionHandler(TutorAlreadyRegisteredException ex) {
        return new JsonMessage(ex.getMessage());
    }
}