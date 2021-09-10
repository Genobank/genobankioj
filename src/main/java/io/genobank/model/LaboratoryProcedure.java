package io.genobank;

/// @see https://genobank.io/certificates/laboratoryProcedureTaxonomy.json
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
      case "2":
        return new LaboratoryProcedure("2", "COVID-19-ANTIGEN");
      case "3":
        return new LaboratoryProcedure("3", "COVID-19-LAMP");
      case "4":
        return new LaboratoryProcedure("4", "COVID-19-VACCINE");
      case "5":
        return new LaboratoryProcedure("5", "SARS-CoV-2-IgG");
      default:
        throw new IllegalArgumentException("Only laboratory procedure 1=COVID-19-PCR, 2=COVID-19-ANTIGEN, 3=COVID-19-LAMP 4=COVID-19-VACCINE, 5=SARS-CoV-2-IgG are supported in this version");
    }
  }

  public LaboratoryProcedureResult resultWithCode(String code) throws IllegalArgumentException {
    switch (code) {
      case "N":
        return new LaboratoryProcedureResult("N", "NEGATIVE");
      case "P":
        return new LaboratoryProcedureResult("P", "POSITIVE");
      case "C":
        return new LaboratoryProcedureResult("C", "COMPLETE");
      default:
        throw new IllegalArgumentException("Only laboratory result N=NEGATIVE, P=POSITIVE and C=COMPLETE are supported in this version");
    }
  }


}