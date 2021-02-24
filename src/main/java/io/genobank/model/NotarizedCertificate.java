package io.genobank;

import java.net.URI;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

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

  public final byte[] txHash;

  NotarizedCertificate(
    PermitteeRepresentations permitteeRepresentations,
    byte[] permiteeSignature,
    java.time.Instant notarizedTime,
    byte[] notarizedSignature,
    byte[] txHash
  ) {
    java.util.Objects.requireNonNull(permitteeRepresentations);
    java.util.Objects.requireNonNull(permiteeSignature);
    java.util.Objects.requireNonNull(notarizedTime);
    java.util.Objects.requireNonNull(notarizedSignature);
    java.util.Objects.requireNonNull(txHash);

    this.permitteeRepresentations = permitteeRepresentations;
    this.permitteeSignature = permiteeSignature;
    this.notarizedTime = notarizedTime;
    this.notarizedSignature = notarizedSignature;
    this.txHash = txHash;
    this.network = permitteeRepresentations.network;
  }

  public String getTightSerialization() {
    return String.join("|", new String[]{
      permitteeRepresentations.getTightSerialization(),
      Numeric.toHexString(permitteeSignature),
      notarizedTime.toEpochMilli() + "",
      Numeric.toHexString(notarizedSignature),
      Numeric.toHexString(txHash)
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
      Numeric.toHexString(permitteeSignature),
      DateTimeFormatter.ISO_INSTANT.format(notarizedTime),
      Numeric.toHexString(notarizedSignature)
    });
  }

  public String toURL() {
    URI certificateURL;
    try {
      certificateURL = new URI(
        network.certificateUrlBase.getScheme(),
        network.certificateUrlBase.getSchemeSpecificPart(),
        getTightSerialization()
      );
    } catch(java.net.URISyntaxException exception) {
      throw new RuntimeException("URL construction error");
    }
    return certificateURL.toASCIIString();
  }
}