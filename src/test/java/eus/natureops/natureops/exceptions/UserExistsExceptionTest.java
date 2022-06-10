package eus.natureops.natureops.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserExistsExceptionTest {

  private final static String MSG = "El usuario ya esta registrado";

  @Test
  void throwException() {
    assertThrowsExactly(UserExistsException.class, () -> {throw new UserExistsException();});
  }

  @Test
  void exceptionMsg() {
    try {
      throw new UserExistsException();
    } catch ( Exception e) {
      assertEquals(MSG, e.getMessage());
    }
  }

}
