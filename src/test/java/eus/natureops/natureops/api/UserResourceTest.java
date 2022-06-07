package eus.natureops.natureops.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import eus.natureops.natureops.service.RoleService;
import eus.natureops.natureops.service.UserService;
import eus.natureops.natureops.utils.FingerprintHelper;
import eus.natureops.natureops.utils.JWTUtil;

@ExtendWith(SpringExtension.class)
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

  // @Test
  // void testUpdate() throws Exception {
    // String refreshToken = createRefreshtoken(-1000 * 60 * 60 * 24, createHash("RANDOM"));

    // when(userDetailsService.loadUserByUsername("eka")).thenReturn(dummy);
    // when(jwtUtil.verifyToken(refreshToken)).then(new Answer<DecodedJWT>() {
    //   @Override
    //   public DecodedJWT answer(InvocationOnMock invocation) throws Throwable {
    //     JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
    //     return verifier.verify(refreshToken);
    //   }
    // });
    // Mockito.doNothing().when(fingerprintHelper).verifyFingerprint(createHash("RANDOM"), "RANDOM");

    // UserView userView = new UserView(1L, "modified", "name", "email");

    // Cookie cookie = new Cookie("Fgp", "SECRET");
    // mvc.perform(post("http://localhost:8080/api/update")
    //     .contentType(MediaType.APPLICATION_JSON)
    //     .content(asJsonString(userView))
    //     .cookie(cookie))
    //     .andExpect(status().isForbidden())
    //     .andExpect(result -> assertTrue(result.getResolvedException() instanceof RefreshTokenMissingException));
  // }

  private static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
