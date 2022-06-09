package eus.natureops.natureops.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.Cookie;
import javax.xml.bind.DatatypeConverter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
import org.springframework.test.web.servlet.MockMvc;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import eus.natureops.natureops.service.RoleService;
import eus.natureops.natureops.service.UserService;
import eus.natureops.natureops.utils.FingerprintHelper;
import eus.natureops.natureops.utils.ISystem;
import eus.natureops.natureops.utils.JWTUtil;

@WebMvcTest({ UserResource.class })
@ActiveProfiles("ci")
class UserResourceTest {

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

  // @Test
  // void testUpdate() throws Exception {
  // String refreshToken = createRefreshtoken(-1000 * 60 * 60 * 24,
  // createHash("RANDOM"));

  // when(userDetailsService.loadUserByUsername("eka")).thenReturn(dummy);
  // when(jwtUtil.verifyToken(refreshToken)).then(new Answer<DecodedJWT>() {
  // @Override
  // public DecodedJWT answer(InvocationOnMock invocation) throws Throwable {
  // JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
  // return verifier.verify(refreshToken);
  // }
  // });
  // Mockito.doNothing().when(fingerprintHelper).verifyFingerprint(createHash("RANDOM"),
  // "RANDOM");

  // UserView userView = new UserView(1L, "modified", "name", "email");

  // Cookie cookie = new Cookie("Fgp", "SECRET");
  // mvc.perform(post("http://localhost:8080/api/update")
  // .contentType(MediaType.APPLICATION_JSON)
  // .content(asJsonString(userView))
  // .cookie(cookie))
  // .andExpect(status().isForbidden())
  // .andExpect(result -> assertTrue(result.getResolvedException() instanceof
  // RefreshTokenMissingException));
  // }

  @Test
  void testDelete() throws Exception {
    String accessToken = createAccesstoken(1000L * 60, createHash("RANDOM"));

    when(userDetailsService.loadUserByUsername("eka")).thenReturn(dummy);
    when(jwtUtil.verifyToken(accessToken)).then(new Answer<DecodedJWT>() {
      @Override
      public DecodedJWT answer(InvocationOnMock invocation) throws Throwable {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("SECRET")).build();
        return verifier.verify(accessToken);
      }
    });

    Cookie cookie = new Cookie("Fgp", "SECRET");
    mvc.perform(delete("http://localhost:8080/api/delete")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
        .cookie(cookie))
        .andExpect(status().isOk());
    verify(userService, times(1)).findByUsername(anyString());
    verify(userService, times(1)).disable(any());
  }

  private String createAccesstoken(long delta, String fingerprint) {
    return JWT.create().withSubject(dummy.getUsername())
        .withExpiresAt(new Date(ISystem.currentTimeMillis() + delta))
        .withIssuer("natureops.eus")
        .withClaim("roles", dummy.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
        .withClaim("fingerprint", fingerprint)
        .sign(Algorithm.HMAC256("SECRET"));
  }

  private String createHash(String fgp) {
    try {
      byte[] userFingerprintDigest = digest.digest(fgp.getBytes("utf-8"));
      return DatatypeConverter.printHexBinary(userFingerprintDigest);
    } catch (Exception e) {
      return null;
    }
  }

  private static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
