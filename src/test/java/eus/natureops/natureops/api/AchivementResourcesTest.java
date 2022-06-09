package eus.natureops.natureops.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.Cookie;
import javax.xml.bind.DatatypeConverter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import eus.natureops.natureops.dto.AchievementView;
import eus.natureops.natureops.service.AchivementService;
import eus.natureops.natureops.utils.FingerprintHelper;
import eus.natureops.natureops.utils.ISystem;
import eus.natureops.natureops.utils.JWTUtil;

@ExtendWith(SpringExtension.class)
@WebMvcTest({ AchivementResource.class })
@ActiveProfiles("ci")
class AchivementResourcesTest {

  private final static String SECRET = "secret";

  @Autowired
  MockMvc mvc;

  @MockBean
  AchivementService achivementService;

  @MockBean
  JWTUtil jwtUtil;

  @MockBean
  UserDetailsService userDetailsService;

  @MockBean
  FingerprintHelper fingerprintHelper;

  static UserDetails dummy;

  static MessageDigest digest;

  @BeforeAll
  public static void setUp() throws NoSuchAlgorithmException {
    dummy = new User("eka", BCrypt.hashpw("123", BCrypt.gensalt()),
        Stream.of("ROLE_USER").map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
    digest = MessageDigest.getInstance("SHA-256");
  }

  @Test
  void testgetSize() throws Exception {
    String accessToken = createAccesstoken(+1000 * 60, "RANDOM");
    when(achivementService.achievementsSize()).thenReturn(6);
    when(userDetailsService.loadUserByUsername("eka")).thenReturn(dummy);
    when(jwtUtil.verifyToken(accessToken)).then(new Answer<DecodedJWT>() {
      @Override
      public DecodedJWT answer(InvocationOnMock invocation) throws Throwable {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        return verifier.verify(accessToken);
      }
    });

    MvcResult result = mvc.perform(get("http://localhost:8080/api/achivements/size")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
        .andExpect(status().isOk()).andReturn();

    assertEquals(result.getResponse().getContentAsString(), "6");
  }

  @Test
  void testgetAll() throws Exception {
    String accessToken = createAccesstoken(1000L * 60, createHash("RANDOM"));

    List<AchievementView> lista = new ArrayList<>();
    when(achivementService.getPage(0, 3, "eka")).thenReturn(lista);

    when(userDetailsService.loadUserByUsername("eka")).thenReturn(dummy);
    when(jwtUtil.verifyToken(accessToken)).then(new Answer<DecodedJWT>() {
      @Override
      public DecodedJWT answer(InvocationOnMock invocation) throws Throwable {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        return verifier.verify(accessToken);
      }
    });

    Cookie cookie = new Cookie("Fgp", "RANDOM");
    MvcResult result = mvc.perform(get("http://localhost:8080/api/achivements/0/3")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
        .cookie(cookie))
        .andExpect(status().isOk()).andReturn();

    assertEquals(result.getResponse().getContentAsString(), lista.toString());
  }

  private String createAccesstoken(long delta, String fingerprint) {
    return JWT.create().withSubject(dummy.getUsername())
        .withExpiresAt(new Date(ISystem.currentTimeMillis() + delta))
        .withIssuer("natureops.eus")
        .withClaim("roles", dummy.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
        .withClaim("fingerprint", fingerprint)
        .sign(Algorithm.HMAC256(SECRET));
  }

  private String createHash(String fgp) {
    try {
      byte[] userFingerprintDigest = digest.digest(fgp.getBytes("utf-8"));
      return DatatypeConverter.printHexBinary(userFingerprintDigest);
    } catch (Exception e) {
      return null;
    }
  }
}
