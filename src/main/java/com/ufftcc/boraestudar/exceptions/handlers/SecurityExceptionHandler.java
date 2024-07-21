package com.ufftcc.boraestudar.exceptions.handlers;

import com.ufftcc.boraestudar.exceptions.JsonMessage;
import com.ufftcc.boraestudar.exceptions.security.TokenEmailNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SecurityExceptionHandler {

    @ExceptionHandler({BadCredentialsException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public JsonMessage handleBadCredentialsException(BadCredentialsException exception) {
        exception.printStackTrace();
        return new JsonMessage("The username or password is incorrect");
        
    }

    @ExceptionHandler({AccountStatusException.class})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public JsonMessage handleAccountStatusException(AccountStatusException exception) {
        exception.printStackTrace();
        return new JsonMessage("The account is locked");
        
    }

    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public JsonMessage handleAccessDeniedException(AccessDeniedException exception) {
        exception.printStackTrace();
        return new JsonMessage("You are not authorized to access this resource");
        
    }

    @ExceptionHandler({SignatureException.class})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public JsonMessage handleSignatureException(SignatureException exception) {
        exception.printStackTrace();
        return new JsonMessage("The JWT signature is invalid");
        
    }

    @ExceptionHandler({ExpiredJwtException.class})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public JsonMessage handleExpiredJwtException(ExpiredJwtException exception) {
        exception.printStackTrace();
        return new JsonMessage("The JWT token has expired");
    }

    @ExceptionHandler({TokenEmailNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public JsonMessage handleTokenEmailNotFoundException(TokenEmailNotFoundException exception) {
        exception.printStackTrace();
        return new JsonMessage(exception.getMessage());
    }
}
