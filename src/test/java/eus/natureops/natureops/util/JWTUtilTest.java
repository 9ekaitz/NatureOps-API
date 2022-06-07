package eus.natureops.natureops.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import eus.natureops.natureops.utils.ISystem;
import eus.natureops.natureops.utils.JWTUtil;

@ExtendWith(MockitoExtension.class)
class JWTUtilTest {

  private final static String ISSUER = "natureops.eus";
  private final static String SECRET = "secret";

  @InjectMocks
  static JWTUtil jwtUtil;

  static UserDetails dummy;

  @BeforeAll
  static void setup() {
    jwtUtil = new JWTUtil();
    dummy = new User("eka", "123", Stream.of("ROLE_USER")
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList()));
    ReflectionTestUtils.setField(jwtUtil, "secretKey", SECRET);
    ReflectionTestUtils.setField(jwtUtil, "issuer", ISSUER);
  }

  @Test
  void createTokenWithFingerprintTest() {
    long currentTime = ISystem.currentTimeMillis();

    try (MockedStatic<ISystem> utilities = Mockito.mockStatic(ISystem.class)) {
      utilities.when(ISystem::currentTimeMillis).thenReturn(currentTime);
      String accessTokenTest = jwtUtil.generateToken(dummy, "RANDOM");

      String accessToken = JWT.create().withSubject(dummy.getUsername())
          .withExpiresAt(new Date(currentTime + 1000 * 60))
          .withIssuer(ISSUER)
          .withClaim("roles", Stream.of("ROLE_USER").collect(Collectors.toList()))
          .withClaim("fingerprint", "RANDOM")
          .sign(Algorithm.HMAC256(SECRET));
      assertEquals(accessTokenTest, accessToken, "The access tokens aren\'t equal");
    }
  }

  @Test
  void createRefreshWithFingerprintTest() {
    long currentTime = ISystem.currentTimeMillis();

    try (MockedStatic<ISystem> utilities = Mockito.mockStatic(ISystem.class)) {
      utilities.when(ISystem::currentTimeMillis).thenReturn(currentTime);
      String refreshTokenTest = jwtUtil.generateRefreshToken(dummy, "RANDOM");

      String refreshToken = JWT.create().withSubject(dummy.getUsername())
          .withExpiresAt(new Date(currentTime + 1000L * 60 * 60 * 24 * 180))
          .withIssuer(ISSUER)
          .withClaim("fingerprint", "RANDOM")
          .sign(Algorithm.HMAC256(SECRET));
      assertEquals(refreshTokenTest, refreshToken, "The refrsh tokens aren\'t equal");
    }
  }

  @Test
  void verifyExpiredTest() {
    long currentTime = ISystem.currentTimeMillis();

    String token = JWT.create().withSubject(dummy.getUsername())
        .withExpiresAt(new Date(currentTime - 1000 * 60))
        .withIssuer(ISSUER)
        .withClaim("roles", Stream.of("ROLE_USER").collect(Collectors.toList()))
        .sign(Algorithm.HMAC256(SECRET));

    assertThrowsExactly(TokenExpiredException.class, () -> jwtUtil.verifyToken(token), "The token is expired but no exception was thrown");
  }

  @Test
  void verifyWrongSecretTest() {
    long currentTime = ISystem.currentTimeMillis();

    String token = JWT.create().withSubject(dummy.getUsername())
        .withExpiresAt(new Date(currentTime + 1000 * 60))
        .withIssuer(ISSUER)
        .withClaim("roles", Stream.of("ROLE_USER").collect(Collectors.toList()))
        .sign(Algorithm.HMAC256("notSECRET"));

    assertThrowsExactly(SignatureVerificationException.class, () -> jwtUtil.verifyToken(token));
  }

  @Test
  void verifyTest() {
    long currentTime = ISystem.currentTimeMillis();

    String token = JWT.create().withSubject(dummy.getUsername())
        .withExpiresAt(new Date(currentTime + 1000 * 60))
        .withIssuer(ISSUER)
        .withClaim("roles", Stream.of("ROLE_USER").collect(Collectors.toList()))
        .sign(Algorithm.HMAC256(SECRET));
    Object jwt = jwtUtil.verifyToken(token);
    assertInstanceOf(DecodedJWT.class, jwt);
    DecodedJWT decodedJWT = (DecodedJWT) jwt;
    assertEquals(dummy.getUsername(), decodedJWT.getSubject());
  }
}
