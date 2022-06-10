package eus.natureops.natureops.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RefreshTokenMissingExceptionTest {

  private final static String MSG = "Refresh token is missing";

  @Test
  void throwException() {
    assertThrowsExactly(RefreshTokenMissingException.class, () -> {throw new RefreshTokenMissingException();});
  }

  @Test
  void exceptionMsg() {
    try {
      throw new RefreshTokenMissingException();
    } catch ( Exception e) {
      assertEquals(MSG, e.getMessage());
    }
  }

}
