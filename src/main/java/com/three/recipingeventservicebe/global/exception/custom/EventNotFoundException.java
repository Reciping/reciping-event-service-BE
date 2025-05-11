package com.three.recipingeventservicebe.global.exception.custom;

public class EventNotFoundException extends RuntimeException {

    public EventNotFoundException(String message) {
        super(message);
    }
}
