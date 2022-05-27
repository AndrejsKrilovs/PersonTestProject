package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;

@ControllerAdvice
public class RestExceptionHandler {
  @ExceptionHandler
  public ResponseEntity<Object> handleRuntimeException(RuntimeException exception) {
    ExceptionResponse response = ExceptionResponse.builder()
        .errorCode(HttpStatus.NOT_MODIFIED.value())
        .message(exception.getMessage())
        .dateTime(LocalDateTime.now())
        .build();
    return new ResponseEntity<>(response, HttpStatus.NOT_MODIFIED);
  }

  @ExceptionHandler
  public ResponseEntity<Object> handleNoSuchElementException(NoSuchFieldException exception) {
    ExceptionResponse response = ExceptionResponse.builder()
        .errorCode(HttpStatus.NOT_FOUND.value())
        .message(exception.getMessage())
        .dateTime(LocalDateTime.now())
        .build();
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler
  public ResponseEntity<Object> generalException(Exception exception) {
    ExceptionResponse response = ExceptionResponse.builder()
        .errorCode(HttpStatus.NOT_ACCEPTABLE.value())
        .message(exception.getMessage())
        .dateTime(LocalDateTime.now())
        .build();
    return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
  }
}