package com.three.recipingeventservicebe.global.exception.custom;

public class EventClosedException extends RuntimeException {

    public EventClosedException(String message) {
        super(message);
    }
}
