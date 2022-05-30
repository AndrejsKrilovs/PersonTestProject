package com.example.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoPersonElementException extends NoSuchElementException {
    public NoPersonElementException(String exception) {
        super(exception);
    }

    public NoPersonElementException() {
        super();
    }
}
