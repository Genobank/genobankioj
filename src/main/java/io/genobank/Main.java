package io.genobank;

import java.time.Instant;

/**
 * Example implementation certifying laboratory results.
 * 
 * @author William Entriken
 */
public class Main {
  public static void main(String[] args) throws IllegalArgumentException {    
    if (args.length != 7) {
      showHelp();
      return;
    }
    
    Network network;
    switch (args[0]) {
      case "--test":
        network = Network.TEST;
        break;
      case "--production":
        network = Network.PRODUCTION;
        break;
      default:
        throw new IllegalArgumentException("You must specify --test or --production network");
    }

    PermitteeRepresentations representations = new PermitteeRepresentations(
      network,
      args[1], // Patient name
      args[2], // Patient passport
      LaboratoryProcedure.procedureWithCode(args[3]), // Laboratory procedure
      LaboratoryProcedure.procedureWithCode(args[3]).resultWithCode(args[4]),
      args[5], // Serial
      Instant.ofEpochSecond(Integer.parseInt(args[6])) // Time
    );

    System.out.println("permitteeRepresentations.getTightSerialization()");
    System.out.println(representations.getTightSerialization());
    System.out.println("");
    System.out.println("permitteeRepresentations.getFullSerialization()");
    System.out.println(representations.getFullSerialization());
    System.out.println("");
    
    PermitteeSigner signer = new PermitteeSigner("wrong outside clever wagon father insane boy junk punch duck drift cupboard");
    byte[] signature = signer.sign(representations);
    
    Platform platform = new Platform(network);
    NotarizedCertificate certificate = platform.notarize(representations, signature);
    System.out.println("Certificate URL");
    System.out.println(certificate.toURL());
  }
      
  public static void showHelp() {
    System.out.println("Blockchain Lab Results Certification");
    System.out.println("Java Certification Example, version 1.0");
    System.out.println("(c) GenoBank.io");
    System.out.println("");
    System.out.println("SYNOPSIS");
    System.out.println("    certificates --test PATIENT_NAME PATIENT_PASSPORT PROCEDURE_CODE RESULT_CODE SERIAL TIMESTAMP");
    System.out.println("    certificates --production PATIENT_NAME PATIENT_PASSPORT PROCEDURE_CODE RESULT_CODE SERIAL TIMESTAMP");
    System.out.println("");
    System.out.println("DESCRIPTION");
    System.out.println("    This notarizes a laboratory result using the GenoBank.io platform.");
    System.out.println("    Running on the production network is billable per your laboratory agreement.");
    System.out.println("");
    System.out.println("    PATIENT_NAME     must match [A-Za-z0-9 .-]+");
    System.out.println("    PATIENT_PASSPORT must match [A-Z0-9 -]+");
    System.out.println("    PROCEDURE_CODE   must be a procedure key in the Laboratory Procedure Taxonomy");
    System.out.println("    RESULT_CODE      must be a result key in the Laboratory Procedure Taxonomy");
    System.out.println("    SERIAL           must match [A-Z0-9 -]*");
    System.out.println("    TIMESTAMP        must match [0-9]+ representing the procedure/sample collection time");
    System.out.println("");
    System.out.println("OUTPUT");
    System.out.println("    A complete URL for the certificate is printed to standard output.");
    System.out.println("    Please note: you should keep a copy of this output because you paid for it");
    System.out.println("    and nobody else has a copy or can recreate it for you.");
    System.out.println("");
    System.out.println("REFERENCES");
    System.out.println("    Laboratory Procedure Taxonomy (test):");
    System.out.println("    https://genobank.io/certificates/laboratoryProcedureTaxonomy.json");
    System.out.println("");
    System.out.println("    Laboratory Procedure Taxonomy (production):");
    System.out.println("    https://genobank.io/test/certificates/laboratoryProcedureTaxonomy.json");
  }
}
