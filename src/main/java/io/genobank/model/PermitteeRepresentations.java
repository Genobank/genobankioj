package io.genobank;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.regex.Pattern;

import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;

import org.json.JSONObject;

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

  public final String patientName;

  public final String patientPassport;

  public final LaboratoryProcedure procedure;
  
  public final LaboratoryProcedureResult result;
  
  public final String serial;

  public final java.time.Instant time;

  public final Integer permitteeId;

  public final String jsonPassport;

  public final String jsonVaccineData;

  public final String jsonCovidTest;

  public PermitteeRepresentations(
    Network network,
    String patientName,
    String patientPassport,
    LaboratoryProcedure procedure,
    LaboratoryProcedureResult result,
    String serial,
    java.time.Instant time,
    Integer permitteeId,
    String jsonPassport,
    String jsonVaccineData,
    String jsonCovidTest
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
    if (!Pattern.matches("[A-Z0-9 -]*", serial)) {
      throw new IllegalArgumentException("Serial does not use required format");
    }
    this.serial = serial;
    
    // Time
    if (time.compareTo(Instant.parse("2021-01-01T00:00:00Z")) < 0) {
      throw new IllegalArgumentException("Time is too early, it is before 2021-01-01");
    }
    this.time = time;

    // Permittee ID
    this.permitteeId = permitteeId;

    // jsonPassport
    String regex = "^\\{.*\\}$";
    if (!Pattern.matches(regex, jsonPassport)) {
      throw new IllegalArgumentException("jsonPassport does not use required format");
    }
    this.jsonPassport = jsonPassport;

    // jsonVaccineData
    String regex2 = "^\\[\\{.*\\}\\]$";
    if (!Pattern.matches(regex2, jsonVaccineData)) {
        throw new IllegalArgumentException("jsonVaccineData does not use required format");
    }
    this.jsonVaccineData = jsonVaccineData;

    // jsonCovidTest
    if (!jsonCovidTest.isEmpty()) {
        if (!Pattern.matches(regex, jsonCovidTest)) {
            throw new IllegalArgumentException("jsonCovidTest does not use required format");
        }
        this.jsonCovidTest = jsonCovidTest;
    } else {
        this.jsonCovidTest = "";
    }
  }

  public String getFullSerialization() {
    DateTimeFormatter isoInstantWithMilliseconds = new DateTimeFormatterBuilder()
        .appendInstant(3)
        .toFormatter();

    return String.join("|", new String[]{
      network.namespacePrefix + namespaceSuffix,
      patientName,
      patientPassport,
      procedure.internationalName,
      result.internationalName,
      serial,
      isoInstantWithMilliseconds.format(time),
      permitteeId + "",
      jsonPassport,
      jsonVaccineData,
      jsonCovidTest
    });
  }

  public String getTightSerialization() {
    return String.join("|", new String[]{
      patientName,
      patientPassport,
      procedure.code,
      result.code,
      serial,
      time.toEpochMilli() + "",
      permitteeId + "",
      jsonPassport,
      jsonVaccineData,
      jsonCovidTest
    });  
  }  

  public byte[] getClaim() {
    return Sign.getEthereumMessageHash(getFullSerialization().getBytes(StandardCharsets.UTF_8));
//    byte[] message = getFullSerialization().getBytes(StandardCharsets.UTF_8);
//    return Hash.sha3(message);
  }
}
