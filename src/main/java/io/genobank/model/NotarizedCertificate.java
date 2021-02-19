package io.genobank;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import org.web3j.crypto.Hash;

/**
 * This is a signed and billed response from GenoBank.io which notarizes a
 * specific representation made by a laboratory.
 * 
 * @author William Entriken
 */
public class NotarizedCertificate {
  public final Network network;

  public final PermitteeRepresentations permitteeRepresentations;

  public final byte[] permitteeSignature;

  public final java.time.Instant notarizedTime;

  public final byte[] notarizedSignature;

  NotarizedCertificate(
    PermitteeRepresentations permitteeRepresentations,
    byte[] permiteeSignature,
    java.time.Instant notarizedTime,
    byte[] notarizedSignature
  ) {
    java.util.Objects.requireNonNull(permitteeRepresentations);
    java.util.Objects.requireNonNull(permiteeSignature);
    java.util.Objects.requireNonNull(notarizedTime);
    java.util.Objects.requireNonNull(notarizedSignature);

    this.permitteeRepresentations = permitteeRepresentations;
    this.permitteeSignature = permiteeSignature;
    this.notarizedTime = notarizedTime;
    this.notarizedSignature = notarizedSignature;
    this.network = permitteeRepresentations.network;
  }

  public String getTightSerialization() {
    return String.join("|", new String[]{
      permitteeRepresentations.getTightSerialization(),
      bytesToHex(permitteeSignature),
      notarizedTime.getEpochSecond()+"",
      bytesToHex(notarizedSignature)
    });
  }

  public String getFullSerialization() {
    /*
    DateTimeFormatter ISO8601 = DateTimeFormatter
      .ofPattern("yyyy-MM-dd\\THH:mm:ss\\Z")
      .withZone(ZoneOffset.UTC);
      */

    return String.join("|", new String[]{
      permitteeRepresentations.getFullSerialization(),
      bytesToHex(permitteeSignature),
      DateTimeFormatter.ISO_INSTANT.format(notarizedTime),
      bytesToHex(notarizedSignature)
    });
  }

  public Boolean validateNotarization() {
    String platformClaim = Hash.sha3(
      String.join("|", new String[]{
        bytesToHex(permitteeSignature),
        DateTimeFormatter.ISO_INSTANT.format(notarizedTime)
      })
    );

    // crypte ECRECOVER here
    return false;
  }

  public String toURL() {
    return "https://genobank.io/...";
  }

  private String bytesToHex(byte[] bytes) {
    final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

    char[] chars = new char[2 * bytes.length];
    for (int i = 0; i < bytes.length; ++i) {
        chars[2 * i] = HEX_CHARS[(bytes[i] & 0xF0) >>> 4];
        chars[2 * i + 1] = HEX_CHARS[bytes[i] & 0x0F];
    }
    return new String(chars);
  }

}
