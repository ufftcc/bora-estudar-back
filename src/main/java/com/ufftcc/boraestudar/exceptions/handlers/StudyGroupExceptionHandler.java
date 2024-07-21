package com.ufftcc.boraestudar.exceptions.handlers;

import com.ufftcc.boraestudar.exceptions.studygroup.InsufficientPrivilegesException;
import com.ufftcc.boraestudar.exceptions.JsonMessage;
import com.ufftcc.boraestudar.exceptions.studygroup.NoStudentsSlotsAvailableException;
import com.ufftcc.boraestudar.exceptions.studygroup.StudyGroupNotFoundException;
import com.ufftcc.boraestudar.exceptions.studygroup.TutorAlreadyRegisteredException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class StudyGroupExceptionHandler {

    @ExceptionHandler({InsufficientPrivilegesException.class})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public JsonMessage insufficientPrivilegesExceptionHandler(InsufficientPrivilegesException ex) {
        return new JsonMessage(ex.getMessage());
    }

    @ExceptionHandler({NoStudentsSlotsAvailableException.class})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public JsonMessage noStudentsSlotsAvailableExceptionHandler(NoStudentsSlotsAvailableException ex) {
        return new JsonMessage(ex.getMessage());
    }

    @ExceptionHandler({TutorAlreadyRegisteredException.class})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public JsonMessage tutorAlreadyRegisteredExceptionHandler(TutorAlreadyRegisteredException ex) {
        return new JsonMessage(ex.getMessage());
    }

    @ExceptionHandler({StudyGroupNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public JsonMessage userNotFoundExceptionHandle(StudyGroupNotFoundException ex) {
        return new JsonMessage(ex.getMessage());
    }
}