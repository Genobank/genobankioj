package io.genobank;

public class LaboratoryProcedure {
  /** A succinct code used in the tight serialization */
  public final String code;

  /** The full name that is part of the signed message */
  public final String internationalName;

  public LaboratoryProcedure (
    String code,
    String internationalName
  ) {
    java.util.Objects.requireNonNull(code);
    java.util.Objects.requireNonNull(internationalName);

    this.code = code;
    this.internationalName = internationalName;
  }

  public static LaboratoryProcedure procedureWithCode(String code) throws IllegalArgumentException {
    switch (code) {
      case "1":
        return new LaboratoryProcedure("1", "COVID-19-PCR");
      default:
        throw new IllegalArgumentException("Only laboratory procedure 1=COVID-19-PCR is supported in this version");
    }
  }

  public LaboratoryProcedureResult resultWithCode(String code) throws IllegalArgumentException {
    switch (code) {
      case "N":
        return new LaboratoryProcedureResult("N", "NEGATIVE");
      default:
        throw new IllegalArgumentException("Only laboratory result N=NEGATIVE is supported in this version");
    }
  }


}