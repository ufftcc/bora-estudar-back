package com.ufftcc.boraestudar.exceptions;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class JsonMessage {

    private final String message;
    private final LocalDateTime timestamp;

    public JsonMessage(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public JsonMessage(String message, LocalDateTime timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "{\n" +
                "\t\"message\": \"" + message + "\",\n" +
                "\t\"timestamp\": \"" + timestamp + "\"\n" +
                "}";
    }

}
