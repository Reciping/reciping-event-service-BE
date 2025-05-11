package com.three.recipingeventservicebe.global.exception.custom;

public class TimeOutLockException extends RuntimeException {

    public TimeOutLockException(String message) {
        super(message);
    }
}
