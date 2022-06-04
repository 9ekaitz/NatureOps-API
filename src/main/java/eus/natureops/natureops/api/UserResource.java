package eus.natureops.natureops.api;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import eus.natureops.natureops.domain.User;
import eus.natureops.natureops.dto.UserView;
import eus.natureops.natureops.exceptions.UserExistsException;
import eus.natureops.natureops.exceptions.RefreshTokenMissingException;
import eus.natureops.natureops.service.UserService;
import eus.natureops.natureops.utils.FingerprintHelper;
import eus.natureops.natureops.utils.JWTUtil;

@RestController
@RequestMapping("/api")
public class UserResource {

  @Autowired
  private JWTUtil jwtUtil;

  @Autowired
  private FingerprintHelper fingerprintHelper;

  @Autowired
  private UserService userService;

  @Autowired
  private UserDetailsService userDetailsService;

  @GetMapping("/get")
  public ResponseEntity<UserView> get(Authentication auth) {
    return ResponseEntity.ok().body(userService.loadView(auth.getName()));
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody User user) {
    User createdUser;
    try {
      createdUser = userService.register(user);
    } catch (Exception e) {
      throw new UserExistsException();      
    }

    Map<String, String> tokens = new HashMap<>();

    UserDetails newUserDeatils = userDetailsService.loadUserByUsername(createdUser.getUsername());

    String accessToken = jwtUtil.generateToken(newUserDeatils);
    String refreshToken = jwtUtil.generateRefreshToken(newUserDeatils);

    tokens.put("access_token", accessToken);
    tokens.put("refresh_token", refreshToken);

    return new ResponseEntity<Object>(
        tokens, HttpStatus.CREATED);
  }

  @PostMapping("/update")
  public ResponseEntity<?> update(@RequestBody UserView userView, Authentication auth) {
    User createdUser;
    try {
      createdUser = userService.findByUsername(auth.getName());
      createdUser.setUsername(userView.getUsername());
      createdUser.setName(userView.getName());
      createdUser.setEmail(userView.getEmail());
      userService.save(createdUser);
    } catch (Exception e) {
      throw new UserExistsException();      
    }

    Map<String, String> tokens = new HashMap<>();

    UserDetails newUserDeatils = userDetailsService.loadUserByUsername(createdUser.getUsername());

    String accessToken = jwtUtil.generateToken(newUserDeatils);
    String refreshToken = jwtUtil.generateRefreshToken(newUserDeatils);

    tokens.put("access_token", accessToken);
    tokens.put("refresh_token", refreshToken);

    return new ResponseEntity<Object>(
        tokens, HttpStatus.OK);
  }

  /**
   * The refreshToken method is mappend to the <b>/api/token/refresh</b> path and
   * recives a <b>GET</b> request with the
   * refresh token in the Authorization header. It checks if the refresh token is
   * valid and hasn't
   * expired and sends a new access token back. If the refresh token is expired or
   * malformed the error
   * message generated by {@link com.auth0.jwt.JWTVerifier} is sent back
   * 
   * @param request  the {@link HttpServletRequest}
   * @param response the {@link HttpServletResponse}
   * @throws IOException
   */
  @GetMapping("/token/refresh")
  public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    String userFingerprint = null;
    if (request.getCookies() != null && request.getCookies().length > 0) {
      List<Cookie> cookies = Arrays.stream(request.getCookies()).collect(Collectors.toList());
      Optional<Cookie> cookie = cookies.stream().filter(c -> "Fgp"
          .equals(c.getName())).findFirst();
      if (cookie.isPresent()) {
        userFingerprint = cookie.get().getValue();
      }
    }
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      try {
        if (userFingerprint == null)
          throw new RuntimeException("Fingerprint cookie is missing");

        String refreshToken = authorizationHeader.substring("Bearer ".length());
        DecodedJWT decodedJWT = jwtUtil.verifyToken(refreshToken);

        String hashFgp = decodedJWT.getClaim("fingerprint").asString();
        fingerprintHelper.verifyFingerprint(hashFgp, userFingerprint);

        String username = decodedJWT.getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        String accessToken = jwtUtil.generateToken(userDetails, hashFgp);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);

      } catch (Exception e) {
        Map<String, String> error = new HashMap<>();
        error.put("error_message", e.getMessage());

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), error);
      }
    } else {
      throw new RefreshTokenMissingException();
    }
  }
}
