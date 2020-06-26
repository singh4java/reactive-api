package com.reactive.spring.reactiveapi.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GenricExceptionManager {

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> exceptionHandler(RuntimeException exception) {
    log.error("RuntimeException Occurred", exception);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
  }

}
