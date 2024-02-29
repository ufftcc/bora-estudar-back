package com.ufftcc.boraestudar.exceptions.study_group;

public class UserAlreadyRegisteredException extends RuntimeException {
    public UserAlreadyRegisteredException(String s) {
        super(s);
    }
}
