package io.genobank;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import org.web3j.crypto.Hash;

/**
 * This is a specific laboratory result which can be notarized on the
 * GenoBank.io platform by a permitee.
 * 
 * @author William Entriken
 */
public class PermitteeRepresentations {

  private static final String versionCode = "V1";

  private static final String namespaceSuffix = ".certificates.v1.permittee-certification";
  
  public final Network network;

  private final String patientName;

  private final String patientPassport;

  private final LaboratoryProcedure procedure;
  
  private final LaboratoryProcedureResult result;
  
  private final String serial;

  private final java.time.Instant time;

  public PermitteeRepresentations(
    Network network,
    String patientName,
    String patientPassport,
    LaboratoryProcedure procedure,
    LaboratoryProcedureResult result,
    String serial,
    java.time.Instant time
  ) throws IllegalArgumentException {
    // Network
    java.util.Objects.requireNonNull(network);
    this.network = network;
    
    // Patient name
    if (!Pattern.matches("[A-Za-z0-9 .-]+", patientName)) {
      throw new IllegalArgumentException("Patient name does not use required format");
    }
    this.patientName = patientName;
    
    // Patient passport
    if (!Pattern.matches("[A-Z0-9 .-]+", patientPassport)) {
      throw new IllegalArgumentException("Patient passport does not use required format");
    }
    this.patientPassport = patientPassport;
    
    // Laboratory procedure
    java.util.Objects.requireNonNull(procedure);
    this.procedure = procedure;
    
    // Laboratory result
    java.util.Objects.requireNonNull(result);
    this.result = result;
    
    // Serial number
    if (!Pattern.matches("[A-Z0-9 -]*", patientPassport)) {
      throw new IllegalArgumentException("Serial does not use required format");
    }
    this.serial = serial;
    
    // Time
    if (time.compareTo(Instant.parse("2021-01-01T00:00:00Z")) < 0) {
      throw new IllegalArgumentException("Time is too early, it is before 2021-01-01");
    }
    this.time = time;
  }

  public String getFullSerialization() {
    /*
    DateTimeFormatter ISO8601 = DateTimeFormatter
      .ofPattern("yyyy-MM-dd\\THH:mm:ss\\Z")
      .withZone(ZoneOffset.UTC);
      */

    return String.join("|", new String[]{
      network.namespacePrefix + namespaceSuffix,
      patientName,
      patientPassport,
      procedure.internationalName,
      result.internationalName,
      serial,
      DateTimeFormatter.ISO_INSTANT.format(time)
    });
  }

  public String getTightSerialization() {
    return String.join("|", new String[]{
      patientName,
      patientPassport,
      procedure.code,
      result.code,
      serial,
      time.getEpochSecond()+""
    });  
  }  

  public byte[] getClaim() {
    //TODO:  SEE notes on signing, padding, etc. https://github.com/web3j/web3j/issues/208
    return Hash.sha3(getTightSerialization()).getBytes() ;
  }
}
