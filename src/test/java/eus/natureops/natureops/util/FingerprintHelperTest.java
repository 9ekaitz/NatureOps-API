package eus.natureops.natureops.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import eus.natureops.natureops.exceptions.FingerprintVerificationException;
import eus.natureops.natureops.utils.FingerprintHelper;

@ExtendWith(MockitoExtension.class)
class FingerprintHelperTest {

  @InjectMocks
  static FingerprintHelper fingerprintHelper;

  @BeforeAll
  static void setup() throws NoSuchAlgorithmException {
    fingerprintHelper = new FingerprintHelper();
  }

  @Test
  void generateFingerprintTest() {
    String fingerprint = fingerprintHelper.generateFingerprint();

    assertNotNull(fingerprint);
  }

  @Test
  void hashFingerprintTest() throws NoSuchAlgorithmException, UnsupportedEncodingException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] userFingerprintDigest = digest.digest("RANDOM".getBytes(StandardCharsets.UTF_8));
    String hash = DatatypeConverter.printHexBinary(userFingerprintDigest);
    assertEquals(hash, fingerprintHelper.hashFingerprint("RANDOM"));
  }

  @Test
  void verifyFingerprintNotMatchingTest() throws NoSuchAlgorithmException, UnsupportedEncodingException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] userFingerprintDigest = digest.digest("RANDOM".getBytes(StandardCharsets.UTF_8));
    String hash = DatatypeConverter.printHexBinary(userFingerprintDigest);
    assertThrowsExactly(FingerprintVerificationException.class, () -> fingerprintHelper.verifyFingerprint(hash, "NOT_RANDOM"));
  }

  @Test
  void verifyFingerprintTest() throws NoSuchAlgorithmException, UnsupportedEncodingException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] userFingerprintDigest = digest.digest("RANDOM".getBytes("utf-8"));
    String hash = DatatypeConverter.printHexBinary(userFingerprintDigest);
    assertDoesNotThrow(() -> fingerprintHelper.verifyFingerprint(hash, "RANDOM"));
  }

}
