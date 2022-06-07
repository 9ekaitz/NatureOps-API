package eus.natureops.natureops.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

import eus.natureops.natureops.domain.News;
import eus.natureops.natureops.service.NewsService;
import eus.natureops.natureops.utils.ISystem;
import eus.natureops.natureops.utils.JWTUtil;

@ExtendWith(SpringExtension.class)
@WebMvcTest({ NewsResource.class })
@ActiveProfiles("ci")
class NoticiasResourcesTest {

    private final static String SECRET = "secret";

    @Autowired
    MockMvc mvc;
    static UserDetails dummy;

    @MockBean
    NewsService newsService;

    @MockBean
    JWTUtil jwtUtil;

    @MockBean
    UserDetailsService userDetailsService;


    @BeforeAll
    public static void setUp() {
      dummy = new User("eka", BCrypt.hashpw("123", BCrypt.gensalt()), Stream.of("ROLE_USER").map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
    }

    @Test
    void testgetSize() throws Exception {
        String accessToken = createAccesstoken(+1000 * 60);
        when(newsService.getNewsSize()).thenReturn(6);
        when(userDetailsService.loadUserByUsername("eka")).thenReturn(dummy);
        when(jwtUtil.verifyToken(accessToken)).then(new Answer<DecodedJWT>() {
          @Override
          public DecodedJWT answer(InvocationOnMock invocation) throws Throwable {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            return verifier.verify(accessToken);
          }
        });

        MvcResult result  = mvc.perform(get("http://localhost:8080/api/news/size")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
        .andExpect(status().isOk()).andReturn();

        assertEquals(result.getResponse().getContentAsString(), "6");
    }
    
    @Test
    void testgetAll() throws Exception {
        String accessToken = createAccesstoken(+1000 * 60);
        List<News> lista = new ArrayList<>();
        when(newsService.findAll(0,1)).thenReturn(lista);

        when(userDetailsService.loadUserByUsername("eka")).thenReturn(dummy);
        when(jwtUtil.verifyToken(accessToken)).then(new Answer<DecodedJWT>() {
          @Override
          public DecodedJWT answer(InvocationOnMock invocation) throws Throwable {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            return verifier.verify(accessToken);
          }
        });

        MvcResult result  = mvc.perform(get("http://localhost:8080/api/news/0/1")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
        .andExpect(status().isOk()).andReturn();

        assertEquals(result.getResponse().getContentAsString(), lista.toString());
    }

    private String createAccesstoken(int delta) {
        return JWT.create().withSubject(dummy.getUsername())
            .withExpiresAt(new Date(ISystem.currentTimeMillis() + delta))
            .withIssuer("natureops.eus")
            .withClaim("roles", dummy.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
            .sign(Algorithm.HMAC256(SECRET));
      }
}
