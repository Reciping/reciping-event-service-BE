package com.three.recipingeventservicebe.global.exception;

import com.three.recipingeventservicebe.common.dto.ExceptionDto;
import com.three.recipingeventservicebe.global.exception.custom.AlreadyParticipatedException;
import com.three.recipingeventservicebe.global.exception.custom.EventClosedException;
import com.three.recipingeventservicebe.global.exception.custom.EventNotFoundException;
import com.three.recipingeventservicebe.global.exception.custom.TimeOutLockException;
import com.three.recipingeventservicebe.global.exception.custom.UserNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j(topic = "GlobalExceptionHandler")
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionDto> userNotFoundException(final UserNotFoundException e) {
        log.error("UserNotFoundException: ", e);
        return createResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ExceptionDto> eventNotFoundException(final EventNotFoundException e) {
        log.error("EventNotFoundException: ", e);
        return createResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDto> entityNotFoundException(final EntityNotFoundException e) {
        log.error("EntityNotFoundException: ", e);
        return createResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(TimeOutLockException.class)
    public ResponseEntity<ExceptionDto> timeOutLockException(final TimeOutLockException e) {
        log.error("TimeOutLockException: ", e);
        return createResponse(HttpStatus.REQUEST_TIMEOUT, e.getMessage());
    }

    @ExceptionHandler(AlreadyParticipatedException.class)
    public ResponseEntity<ExceptionDto> alreadyParticipatedException(final AlreadyParticipatedException e) {
        log.error("AlreadyParticipatedException: ", e);
        return createResponse(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(EventClosedException.class)
    public ResponseEntity<ExceptionDto> eventClosedException(final EventClosedException e) {
        log.error("EventClosedException: ", e);
        return createResponse(HttpStatus.GONE, e.getMessage());
    }



    @ExceptionHandler(Exception.class)
    ResponseEntity<ExceptionDto> exception(final Exception e) {
        log.error("Exception: ", e);
        return createResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    private ResponseEntity<ExceptionDto> createResponse(
            final HttpStatus status,
            final String message
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ExceptionDto(status, message));
    }
}
