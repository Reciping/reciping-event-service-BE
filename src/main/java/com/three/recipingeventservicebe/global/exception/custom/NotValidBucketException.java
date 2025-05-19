package com.three.recipingeventservicebe.global.exception.custom;

public class NotValidBucketException extends RuntimeException {

    public NotValidBucketException(String message) {
        super(message);
    }
}
