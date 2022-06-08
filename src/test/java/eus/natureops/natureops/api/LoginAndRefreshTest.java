package eus.natureops.natureops.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.Cookie;
import javax.xml.bind.DatatypeConverter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import eus.natureops.natureops.exceptions.RefreshTokenMissingException;
import eus.natureops.natureops.service.RoleService;
import eus.natureops.natureops.service.UserService;
import eus.natureops.natureops.utils.FingerprintHelper;
import eus.natureops.natureops.utils.ISystem;
import eus.natureops.natureops.utils.JWTUtil;

@ExtendWith(SpringExtension.class)
@WebMvcTest({ UserResource.class })
@ActiveProfiles("ci")
class LoginAndRefreshTest {

  private final static String SECRET = "secret";

  @Autowired
  MockMvc mvc;

  @MockBean
  JWTUtil jwtUtil;

  @MockBean
  FingerprintHelper fingerprintHelper;

  @MockBean
  UserService userService;

  @MockBean
  UserDetailsService userDetailsService;

  @MockBean
  RoleService roleService;

  static UserDetails dummy;

  static MessageDigest digest;

  @BeforeAll
  public static void setUp() throws NoSuchAlgorithmException {
    dummy = new User("eka", BCrypt.hashpw("123", BCrypt.gensalt()),
        Stream.of("ROLE_USER").map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
    digest = MessageDigest.getInstance("SHA-256");
  }

  @Test
  void testAccessTokenOnLoginRightPassword() throws Exception {

    when(userDetailsService.loadUserByUsername("eka")).thenReturn(dummy);

    MvcResult result = mvc.perform(post("http://localhost:8080/api/login")
        .param("username", "eka")
        .param("password", "123"))
        .andExpect(status().isOk())
        .andReturn();

    JSONObject response = new JSONObject(result.getResponse().getContentAsString());
    String accessToken = response.getString("access_token");
    String refresh_token = response.getString("refresh_token");

    assertNotNull(accessToken, "The access token returned null");
    assertNotNull(refresh_token, "The refresh token returned null");
  }

  @Test
  void testAccessTokenOnLoginWrongPassword() throws Exception {

    when(userDetailsService.loadUserByUsername("eka")).thenReturn(dummy);

    mvc.perform(post("http://localhost:8080/api/login")
        .param("username", "eka")
        .param("password", "12"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void testRefreshToken() throws Exception {
    String refreshToken = createRefreshtoken(1000 * 60 * 60 * 24, createHash("RANDOM"));

    when(userDetailsService.loadUserByUsername("eka")).thenReturn(dummy);
    when(jwtUtil.verifyToken(refreshToken)).then(new Answer<DecodedJWT>() {
      @Override
      public DecodedJWT answer(InvocationOnMock invocation) throws Throwable {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        return verifier.verify(refreshToken);
      }
    });

    Mockito.doNothing().when(fingerprintHelper).verifyFingerprint(createHash("RANDOM"), "RANDOM");

    when(jwtUtil.generateToken(dummy, "RANDOM")).thenReturn(createAccesstoken(1000 * 60, "RANDOM"));

    Cookie cookie = new Cookie("Fgp", "SECRET");
    MvcResult result = mvc.perform(get("http://localhost:8080/api/token/refresh")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + refreshToken)
        .cookie(cookie))
        .andExpect(status().isOk())
        .andReturn();

    JSONObject response = new JSONObject(result.getResponse().getContentAsString());
    String accessToken = response.getString("access_token");
    String refresh_token = response.getString("refresh_token");

    assertNotNull(accessToken, "The access token returned null");
    assertNotNull(refresh_token, "The refresh token returned null");
  }

  @Test
  void testRefreshTokenExpired() throws Exception {
    String refreshToken = createRefreshtoken(-1000 * 60 * 60 * 24, createHash("RANDOM"));

    when(userDetailsService.loadUserByUsername("eka")).thenReturn(dummy);
    when(jwtUtil.verifyToken(refreshToken)).then(new Answer<DecodedJWT>() {
      @Override
      public DecodedJWT answer(InvocationOnMock invocation) throws Throwable {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        return verifier.verify(refreshToken);
      }
    });
    Mockito.doNothing().when(fingerprintHelper).verifyFingerprint(createHash("RANDOM"), "RANDOM");

    Cookie cookie = new Cookie("Fgp", "SECRET");
    MvcResult result = mvc.perform(get("http://localhost:8080/api/token/refresh")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + refreshToken)
        .cookie(cookie))
        .andExpect(status().isForbidden())
        .andReturn();

    JSONObject response = new JSONObject(result.getResponse().getContentAsString());
    String errorMessage = response.getString("error_message");

    assertNotNull(errorMessage, "No error message was returned");
  }

  @Test
  void testRefreshTokenWithoutToken() throws Exception {
    String refreshToken = createRefreshtoken(-1000 * 60 * 60 * 24, createHash("RANDOM"));

    when(userDetailsService.loadUserByUsername("eka")).thenReturn(dummy);
    when(jwtUtil.verifyToken(refreshToken)).then(new Answer<DecodedJWT>() {
      @Override
      public DecodedJWT answer(InvocationOnMock invocation) throws Throwable {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        return verifier.verify(refreshToken);
      }
    });
    Mockito.doNothing().when(fingerprintHelper).verifyFingerprint(createHash("RANDOM"), "RANDOM");

    Cookie cookie = new Cookie("Fgp", "SECRET");
    mvc.perform(get("http://localhost:8080/api/token/refresh")
        .cookie(cookie))
        .andExpect(status().isForbidden())
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof RefreshTokenMissingException));
  }

  @Test
  void testAuthorizationWithoutToken() throws Exception {
    Cookie cookie = new Cookie("Fgp", "SECRET");

    mvc.perform(get("http://localhost:8080/api/get")
        .cookie(cookie))
        .andExpect(status().isForbidden());
  }

  @Test
  void testAuthorizationWithoutFingerprint() throws Exception {
    String refreshToken = createRefreshtoken(-1000 * 60 * 60 * 24, createHash("RANDOM"));
    when(userDetailsService.loadUserByUsername("eka")).thenReturn(dummy);
    when(jwtUtil.verifyToken(refreshToken)).then(new Answer<DecodedJWT>() {
      @Override
      public DecodedJWT answer(InvocationOnMock invocation) throws Throwable {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        return verifier.verify(refreshToken);
      }
    });

    mvc.perform(get("http://localhost:8080/api/get")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + refreshToken))
        .andExpect(status().isForbidden());
  }

  @Test
  void testAuthorizationExpiredToken() throws Exception {
    String accessToken = createAccesstoken(-1000 * 60, createHash("RANDOM"));

    when(userDetailsService.loadUserByUsername("eka")).thenReturn(dummy);
    when(jwtUtil.verifyToken(accessToken)).then(new Answer<DecodedJWT>() {
      @Override
      public DecodedJWT answer(InvocationOnMock invocation) throws Throwable {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        return verifier.verify(accessToken);
      }
    });
    Mockito.doNothing().when(fingerprintHelper).verifyFingerprint(createHash("RANDOM"), "RANDOM");

    Cookie cookie = new Cookie("Fgp", "SECRET");
    MvcResult result = mvc.perform(get("http://localhost:8080/api/get")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
        .cookie(cookie))
        .andExpect(status().isForbidden())
        .andReturn();

    JSONObject response = new JSONObject(result.getResponse().getContentAsString());
    String errorMessage = response.getString("error_message");

    assertNotNull(errorMessage, "No error message was returned");
  }

  @Test
  void testAuthorization() throws Exception {
    String accessToken = createAccesstoken(+1000 * 60, createHash("RANDOM"));

    when(userDetailsService.loadUserByUsername("eka")).thenReturn(dummy);
    when(jwtUtil.verifyToken(accessToken)).then(new Answer<DecodedJWT>() {
      @Override
      public DecodedJWT answer(InvocationOnMock invocation) throws Throwable {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        return verifier.verify(accessToken);
      }
    });
    Mockito.doNothing().when(fingerprintHelper).verifyFingerprint(createHash("RANDOM"), "RANDOM");

    Cookie cookie = new Cookie("Fgp", "SECRET");
    mvc.perform(get("http://localhost:8080/api/get")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
        .cookie(cookie))
        .andExpect(status().isOk());
  }

  private String createRefreshtoken(int delta, String fingerprint) {
    return JWT.create().withSubject("eka")
        .withExpiresAt(new Date(ISystem.currentTimeMillis() + delta))
        .withIssuer("natureops.eus")
        .withClaim("fingerprint", fingerprint)
        .sign(Algorithm.HMAC256(SECRET));
  }

  private String createAccesstoken(int delta, String fingerprint) {
    return JWT.create().withSubject(dummy.getUsername())
        .withExpiresAt(new Date(ISystem.currentTimeMillis() + delta))
        .withIssuer("natureops.eus")
        .withClaim("roles", dummy.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
        .withClaim("fingerprint", fingerprint)
        .sign(Algorithm.HMAC256(SECRET));
  }

  private String createHash(String fgp) throws UnsupportedEncodingException {
    byte[] userFingerprintDigest = digest.digest(fgp.getBytes("utf-8"));
    return DatatypeConverter.printHexBinary(userFingerprintDigest);
  }
}