package com.ufftcc.boraestudar.exceptions.study_group;

public class StudyGroupNotFoundException extends RuntimeException {
    public StudyGroupNotFoundException(Long id) {
        super("Study group not found with id: " + id);
    }
}
