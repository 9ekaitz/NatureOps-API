package eus.natureops.natureops.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eus.natureops.natureops.domain.User;
import eus.natureops.natureops.service.UserService;
import eus.natureops.natureops.utils.JWTUtil;

@RestController
@RequestMapping("/api")
public class UserResource {

  @Autowired
  private JWTUtil jwtUtil;

  @Autowired
  private UserService userService;

  @Autowired
  private UserDetailsService userDetailsService;

  @GetMapping("/get")
  public ResponseEntity<User> get() {
    return ResponseEntity.ok().body(userService.findByUsername("eka"));
  }

  @GetMapping("/token/refresh")
  public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException{
    String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      try {
        String refreshToken = authorizationHeader.substring("Bearer ".length());
        DecodedJWT decodedJWT = jwtUtil.verifyToken(refreshToken);

        String username = decodedJWT.getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        String accessToken = jwtUtil.generateToken(userDetails);

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
      throw new RuntimeException("Refresh token is missing");
    }
  }

  public String getAll() {
    return "";
  }

  public String auth(String username, String password) {
    return "";
  }

  public String save(User user) {
    return "";
  }
}
