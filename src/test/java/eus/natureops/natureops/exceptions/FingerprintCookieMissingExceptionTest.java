package eus.natureops.natureops.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FingerprintCookieMissingExceptionTest {

  private final static String MSG = "Fingerprint cookie is missing";

  @Test
  void throwException() {
    assertThrowsExactly(FingerprintCookieMissingException.class, () -> {throw new FingerprintCookieMissingException();});
  }

  @Test
  void exceptionMsg() {
    try {
      throw new FingerprintCookieMissingException();
    } catch ( Exception e) {
      assertEquals(MSG, e.getMessage());
    }
  }

}
