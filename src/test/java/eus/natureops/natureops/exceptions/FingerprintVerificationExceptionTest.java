package eus.natureops.natureops.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FingerprintVerificationExceptionTest {

  private final static String MSG = "Test message";

  @Test
  void throwException() {
    assertThrowsExactly(FingerprintVerificationException.class, () -> {throw new FingerprintVerificationException(MSG);});
  }

  @Test
  void exceptionMsg() {
    try {
      throw new FingerprintVerificationException(MSG);
    } catch ( Exception e) {
      assertEquals(MSG, e.getMessage());
    }
  }

}
