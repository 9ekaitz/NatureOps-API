package eus.natureops.natureops.exceptions;

public class SubmissionReadingException extends RuntimeException {
  public SubmissionReadingException() {
    super("An error ocurrer whe parsing the submission");
  }
}
