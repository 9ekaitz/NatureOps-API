package eus.natureops.natureops.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import eus.natureops.natureops.utils.JWTUtil;

public class AuthorizationFilter extends OncePerRequestFilter {

  private JWTUtil jwtUtil;

  public AuthorizationFilter(JWTUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (request.getRequestURI().equals("/api/login") || request.getRequestURI().equals("/api/token/refresh"))
      filterChain.doFilter(request, response);
    else {
      String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        try {
          String token = authorizationHeader.substring("Bearer ".length());

          DecodedJWT decodedJWT = jwtUtil.verifyToken(token);

          String username = decodedJWT.getSubject();
          String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
          Collection<SimpleGrantedAuthority> authorities = Arrays.stream(roles)
              .map(SimpleGrantedAuthority::new)
              .collect(Collectors.toList());

          SecurityContextHolder.getContext()
              .setAuthentication(new UsernamePasswordAuthenticationToken(username, null, authorities));

          filterChain.doFilter(request, response);
        } catch (Exception e) {
          Map<String, String> error = new HashMap<>();
          error.put("error_message", e.getMessage());

          response.setStatus(HttpStatus.FORBIDDEN.value());
          response.setContentType(MediaType.APPLICATION_JSON_VALUE);
          new ObjectMapper().writeValue(response.getOutputStream(), error);
        }

      } else
        filterChain.doFilter(request, response);
    }
  }
}
