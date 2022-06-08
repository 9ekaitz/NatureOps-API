package eus.natureops.natureops.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JWTUtil implements Serializable {

  private static final long ACCESS_OFFSET = 1000L * 60L;
  private static final long REFRESH_OFFSET = 1000L * 60L * 60L * 24L * 180L;

  @Value("${natureops.security.jwt.secret}")
  private String secretKey;

  @Value("${natureops.security.jwt.issuer}")
  private String issuer;

  /**
   * The generateToken function generates a JWT token and returns it as a String.
   * The function takes in the userDetails object, which contains the username and
   * authorities of the user.
   * The token generated includes the the hash of the passed fingerprint.
   * The function also takes in an expiration time for when to expire this token,
   * which is set to 1 hour by default.
   * 
   * @param userDetails Used to Get the user's authorities.
   * @param fingerprint Used to generate a hash and add a fingerprint to the token
   * @return
   */
  public String generateToken(UserDetails userDetails, String fingerprint) {
    List<String> claims = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());
    return createToken(claims, fingerprint, userDetails.getUsername());
  }

  /**
   * The generateRefreshToken method generates a new JWT refresh token and returns
   * it as String.
   * The function takes in the userDetails object, which contains the username of
   * the user.
   * The function return a JWT token linked to the user with a expiration time of
   * 6 months.
   * 
   * @param userDetails Used to get user's username
   * @param fingerPrint The hash of the fingerprint to validate the token in each request
   * @return A JWT token
   */
  public String generateRefreshToken(UserDetails userDetails, String fingerprint) {
    return createRefreshToken(userDetails.getUsername(), fingerprint);
  }

  private String createToken(List<String> claims, String fingerprint, String subject) {
    return JWT.create().withSubject(subject)
        .withExpiresAt(new Date(ISystem.currentTimeMillis() +ACCESS_OFFSET))
        .withIssuer(issuer)
        .withClaim("roles", claims)
        .withClaim("fingerprint", fingerprint)
        .sign(Algorithm.HMAC256(secretKey));
  }

  private String createRefreshToken(String subject, String fingerprint) {
    return JWT.create().withSubject(subject)
        .withExpiresAt(new Date(ISystem.currentTimeMillis()+REFRESH_OFFSET))
        .withIssuer(issuer)
        .withClaim("fingerprint", fingerprint)
        .sign(Algorithm.HMAC256(secretKey));
  }

  /**
   * The verifyToken method uses the HMAC256 algorithm and the pre-shared secret
   * to verify and decode the token.
   * This method takes a token as a String and returns the deocded token wrapped
   * in a {@link DecodedJWT} object
   * 
   * @param token The token as a String
   * @return A {@link DecodedJWT} object with the token
   */
  public DecodedJWT verifyToken(String token) {
    JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
    return verifier.verify(token);
  }
}