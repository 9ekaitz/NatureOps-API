package eus.natureops.natureops.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
public class JWTUtil implements Serializable {

    //FIXME: Subsitute the secret with a proper encrypted secret
    private String SECRET_KEY = "secret";
    private String ISSUER = "natureops.eus";

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
                    .withIssuer(ISSUER)
                    .withClaim("roles", claims)
                    .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    private String createRefreshToken(String subject) {
      return JWT.create().withSubject(subject)
                  .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                  .withIssuer(ISSUER)
                  .sign(Algorithm.HMAC256(SECRET_KEY));
  }

    public DecodedJWT verifyToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET_KEY)).build();
        return verifier.verify(token);
    }
}