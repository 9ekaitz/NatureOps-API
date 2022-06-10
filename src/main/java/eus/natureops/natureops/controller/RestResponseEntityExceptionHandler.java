package eus.natureops.natureops.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import eus.natureops.natureops.exceptions.UserExistsException;
import eus.natureops.natureops.exceptions.AMQPCommunicationException;
import eus.natureops.natureops.exceptions.RefreshTokenMissingException;
import eus.natureops.natureops.exceptions.SubmissionReadingException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({ UserExistsException.class })
  public ResponseEntity<Object> handleConflictException(
      Exception ex) {
    return new ResponseEntity<>(
        ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler({ RefreshTokenMissingException.class })
  public ResponseEntity<Object> handleAccessDeniedException(
      Exception ex) {
    return new ResponseEntity<>(
        ex.getMessage(), new HttpHeaders(), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler({ AMQPCommunicationException.class })
  public ResponseEntity<Object> handleAMQPException(
      Exception ex) {
    return new ResponseEntity<>(
        ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_GATEWAY);
  }

  @ExceptionHandler({ SubmissionReadingException.class })
  public ResponseEntity<Object> handleSubmissionException(
      Exception ex) {
    return new ResponseEntity<>(
        ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }
}
