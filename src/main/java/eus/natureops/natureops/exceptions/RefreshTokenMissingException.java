package eus.natureops.natureops.exceptions;

public class RefreshTokenMissingException extends RuntimeException {
  public RefreshTokenMissingException() {
    super("Refresh token is missing");
  }
}
