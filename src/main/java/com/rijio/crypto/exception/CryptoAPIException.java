package com.rijio.crypto.exception;

import org.springframework.http.HttpStatus;

public class CryptoAPIException extends RuntimeException {
    private HttpStatus status;
    private String message;

    public CryptoAPIException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public CryptoAPIException(String arg0, HttpStatus status, String message) {
        super(arg0);
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
    
}
