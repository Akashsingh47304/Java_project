package com.Ak.resumeBuilder.exception;

public class ResourceExistsException extends RuntimeException {
    public ResourceExistsException(String message){
        super(message);
    }
}