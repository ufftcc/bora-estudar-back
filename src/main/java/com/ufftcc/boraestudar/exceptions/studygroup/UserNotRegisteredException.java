package com.ufftcc.boraestudar.exceptions.studygroup;

public class UserNotRegisteredException extends RuntimeException {

    public UserNotRegisteredException(Long userId) {
        super("Usuário " + userId + " não cadastrado no grupo de estudo");
    }
}
