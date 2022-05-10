package eus.natureops.natureops.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
public class JWTUtil implements Serializable {

    @Value("${natureops.security.jwt.secret}")
    private String secretKey;
    private String issuer = "natureops.eus";

    public String generateToken(UserDetails userDetails) {
        List<String> claims = new ArrayList<>();
        return createToken(claims, userDetails.getUsername());
    }

    public String generateRefreshToken(UserDetails userDetails) {
      return createRefreshToken(userDetails.getUsername());
  }

    private String createToken(List<String> claims, String subject) {
        return JWT.create().withSubject(subject)
                    .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60))
                    .withIssuer(issuer)
                    .withClaim("roles", claims)
                    .sign(Algorithm.HMAC256(secretKey));
    }

    private String createRefreshToken(String subject) {
      return JWT.create().withSubject(subject)
                  .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                  .withIssuer(issuer)
                  .sign(Algorithm.HMAC256(secretKey));
  }

    public DecodedJWT verifyToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
        return verifier.verify(token);
    }
}