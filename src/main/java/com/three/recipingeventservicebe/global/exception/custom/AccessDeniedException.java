package com.three.recipingeventservicebe.global.exception.custom;

public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(String message) {
        super(message);
    }
}
