package com.ufftcc.boraestudar.exceptions.studygroup;

public class UserAlreadyRegisteredException extends RuntimeException {
    public UserAlreadyRegisteredException(Long userId) {
        super("Estudante " + userId +  " ja esta cadastrado no grupo de estudos.");
    }
}
