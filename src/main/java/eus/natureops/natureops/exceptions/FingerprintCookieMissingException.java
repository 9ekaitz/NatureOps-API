package eus.natureops.natureops.exceptions;

public class FingerprintCookieMissingException extends RuntimeException {
  public FingerprintCookieMissingException() {
    super("Fingerprint cookie is missing");
  }
}
