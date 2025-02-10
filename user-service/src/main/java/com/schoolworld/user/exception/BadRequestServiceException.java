package com.schoolworld.user.exception;

public class BadRequestServiceException extends RuntimeException {
    public BadRequestServiceException(String message) {
        super(message);
    }
}
