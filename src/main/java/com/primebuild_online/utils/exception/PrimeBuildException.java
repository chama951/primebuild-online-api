package com.primebuild_online.utils.exception;

import org.springframework.http.HttpStatus;

public class PrimeBuildException extends RuntimeException{
    private final HttpStatus status;

    public PrimeBuildException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
