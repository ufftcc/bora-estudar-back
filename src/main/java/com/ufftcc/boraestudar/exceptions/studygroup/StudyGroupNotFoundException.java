package com.ufftcc.boraestudar.exceptions.studygroup;

public class StudyGroupNotFoundException extends RuntimeException {
    public StudyGroupNotFoundException(Long id) {
        super("Study group not found with id: " + id);
    }
}
