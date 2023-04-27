package edu.mipt.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class AccountsExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    String frontBackExceptionHandler(IllegalStateException e) {
        return e.getMessage();
    }
}
