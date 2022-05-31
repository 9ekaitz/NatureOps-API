package eus.natureops.natureops.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import eus.natureops.natureops.exceptions.UserExistsException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({ UserExistsException.class })
  public ResponseEntity<Object> handleAccessDeniedException(
      Exception ex) {
    return new ResponseEntity<>(
        ex.getMessage(), new HttpHeaders(), HttpStatus.FORBIDDEN);
  }
}
