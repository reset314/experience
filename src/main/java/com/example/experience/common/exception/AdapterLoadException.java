package com.example.experience.common.exception;

public class AdapterLoadException extends RuntimeException {

    public AdapterLoadException(String message) {
        super(message);
    }

    public AdapterLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
