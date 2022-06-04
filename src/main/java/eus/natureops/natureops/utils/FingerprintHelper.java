package eus.natureops.natureops.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.xml.bind.DatatypeConverter;

import org.springframework.stereotype.Service;

import eus.natureops.natureops.exceptions.FingerprintVerificationException;

@Service
public class FingerprintHelper {

  private SecureRandom secureRandom;
  private MessageDigest digest;

  public FingerprintHelper() throws NoSuchAlgorithmException {
    secureRandom = new SecureRandom();
    digest = MessageDigest.getInstance("SHA-256");
  }

  public String generateFingerprint() {
    byte[] fgp = new byte[50];
    secureRandom.nextBytes(fgp);

    return DatatypeConverter.printHexBinary(fgp);
  }

  public String hashFingerprint(String fgp) throws UnsupportedEncodingException {
    byte[] userFingerprintDigest = digest.digest(fgp.getBytes("utf-8"));
    return DatatypeConverter.printHexBinary(userFingerprintDigest);
  }

  public void verifyFingerprint(String hash, String fgp) throws UnsupportedEncodingException {
    byte[] userFingerprintDigest = digest.digest(fgp.getBytes("utf-8")); 
    String fgpHash = DatatypeConverter.printHexBinary(userFingerprintDigest);
    if (!hash.equals(fgpHash)) {
      throw new FingerprintVerificationException("JWT fingerprint verification error");
    }
  }
}
