package com.ufftcc.boraestudar.exceptions.studygroup;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InsufficientPrivilegesException extends RuntimeException {
    public InsufficientPrivilegesException(Long userId) {
        super("Usuario " + userId + " nao possui permissao para alterar o grupo de estudo");
    }
}
