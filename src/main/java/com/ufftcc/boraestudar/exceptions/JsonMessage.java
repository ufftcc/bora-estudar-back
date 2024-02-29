package com.ufftcc.boraestudar.exceptions;

import java.time.LocalDateTime;

public class JsonMessage {

    private final String message;
    private final LocalDateTime timestamp;
    public JsonMessage(String mensagem) {
        this.message = mensagem;
        this.timestamp = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

}
