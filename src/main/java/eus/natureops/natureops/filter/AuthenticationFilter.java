package eus.natureops.natureops.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import eus.natureops.natureops.utils.JWTUtil;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter{

  private JWTUtil jwtUtil;
  private AuthenticationManager authenticationManager;

  public AuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException {
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

    return authenticationManager.authenticate(authenticationToken);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
      Authentication authentication) throws IOException, ServletException {
    
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    String accessToken = jwtUtil.generateToken(userDetails);
    String refreshToken = jwtUtil.generateRefreshToken(userDetails);

    Map<String, String> tokens = new HashMap<>();
    tokens.put("access_token", accessToken);
    tokens.put("refresh_token", refreshToken);

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    new ObjectMapper().writeValue(response.getOutputStream(), tokens);
  }
  
}
