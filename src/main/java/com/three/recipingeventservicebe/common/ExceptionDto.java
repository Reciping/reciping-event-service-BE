package com.three.recipingeventservicebe.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ExceptionDto {

    private HttpStatus status;
    private String message;
}
