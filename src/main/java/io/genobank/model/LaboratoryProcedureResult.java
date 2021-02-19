package io.genobank;

public class LaboratoryProcedureResult {
  /** A succinct code used in the tight serialization */
  public final String code;

  /** The full name that is part of the signed message */
  public final String internationalName;

  public LaboratoryProcedureResult (
    String code,
    String internationalName
  ) {
    java.util.Objects.requireNonNull(code);
    java.util.Objects.requireNonNull(internationalName);

    this.code = code;
    this.internationalName = internationalName;
  }
}