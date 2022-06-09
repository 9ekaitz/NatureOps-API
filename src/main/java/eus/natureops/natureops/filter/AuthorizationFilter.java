package eus.natureops.natureops.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import eus.natureops.natureops.utils.FingerprintHelper;
import eus.natureops.natureops.utils.JWTUtil;

public class AuthorizationFilter extends OncePerRequestFilter {

  private JWTUtil jwtUtil;
  private FingerprintHelper fingerprintHelper;

  public AuthorizationFilter(JWTUtil jwtUtil, FingerprintHelper fingerprintHelper) {
    this.jwtUtil = jwtUtil;
    this.fingerprintHelper = fingerprintHelper;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (request.getRequestURI().equals("/api/login") || request.getRequestURI().equals("/api/token/refresh"))
      filterChain.doFilter(request, response);
    else {
      String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

      String userFingerprint = getFingerprint(request);

      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ") && userFingerprint != null) {
        try {
          String token = authorizationHeader.substring("Bearer ".length());

          DecodedJWT decodedJWT = jwtUtil.verifyToken(token);

          String hashFgp = decodedJWT.getClaim("fingerprint").asString();
          fingerprintHelper.verifyFingerprint(hashFgp, userFingerprint);

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

  private String getFingerprint(HttpServletRequest request) {
    String userFingerprint = null;
    if (request.getCookies() != null && request.getCookies().length > 0) {
      List<Cookie> cookies = Arrays.stream(request.getCookies()).collect(Collectors.toList());
      Optional<Cookie> cookie = cookies.stream().filter(c -> "Fgp"
          .equals(c.getName())).findFirst();
      if (cookie.isPresent()) {
        userFingerprint = cookie.get().getValue();
      }
    }

    return userFingerprint;
  }
}
