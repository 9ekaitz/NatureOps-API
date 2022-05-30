package eus.natureops.natureops.exceptions;

public class UserExistsException extends RuntimeException {
  public UserExistsException() {
    super("El usuario ya esta registrado");
  }
}