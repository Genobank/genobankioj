package io.genobank;

import java.time.Instant;

import org.web3j.utils.Numeric;

/**
 * Implementation for permittee to certify laboratory results
 * 
 * @author William Entriken
 */
public class Main {
  public static void main(String[] args) throws IllegalArgumentException {    
    if (args.length != 8) {
      showHelp();
      return;
    }
    
    System.out.println("Blockchain Lab Results Certification");
    System.out.println("Java Certification Example, version 1.0");
    System.out.println("(c) GenoBank.io 🧬");
    System.out.println();

    Network network;
    switch (args[0]) {
      case "--test":
        System.out.println(ConsoleColors.GREEN + "USING TEST NETWORK ⭐" + ConsoleColors.RESET);
        System.out.println();
        network = Network.TEST;
        break;
        case "--production":
        System.out.println(ConsoleColors.RED + "USING PRODUCTION NETWORK NETWORK FOR BILLABLE TRANSACTION 💰" + ConsoleColors.RESET);
        System.out.println();
        network = Network.PRODUCTION;
        break;
      default:
        throw new IllegalArgumentException("You must specify --test or --production network");
    }

    PermitteeSigner signer = new PermitteeSigner(args[1]);
    System.out.println("Loading account from TWELVE_WORD_PHRASE");
    System.out.println("Account:     " + signer.credentials.getAddress());
    System.out.println();
    
    PermitteeRepresentations representations = new PermitteeRepresentations(
      network,
      args[2], // Patient name
      args[3], // Patient passport
      LaboratoryProcedure.procedureWithCode(args[4]), // Laboratory procedure
      LaboratoryProcedure.procedureWithCode(args[4]).resultWithCode(args[5]),
      args[6], // Serial
      Instant.ofEpochSecond(Integer.parseInt(args[7])) // Time
      );
    System.out.println("Preparing representations");
    System.out.println("Tight:       " + representations.getTightSerialization());
    System.out.println("Full:        " + representations.getFullSerialization());
    System.out.println();
    
    byte[] signature = signer.sign(representations);
    System.out.println("Signing");
    System.out.println("Signature:   " + Numeric.toHexString(signature));
    System.out.println();
    
    Platform platform = new Platform(network);
    NotarizedCertificate certificate = platform.notarize(representations, signature);
    System.out.println("Certificate URL");
    System.out.println(certificate.toURL());
  }
      
  public static void showHelp() {
    System.out.println("Blockchain Lab Results Certification");
    System.out.println("Java Certification Example, version 1.0");
    System.out.println("(c) GenoBank.io");
    System.out.println();
    System.out.println("SYNOPSIS");
    System.out.println("    certificates --test TWELVE_WORD_PHRASE PATIENT_NAME PATIENT_PASSPORT PROCEDURE_CODE RESULT_CODE SERIAL TIMESTAMP");
    System.out.println("    certificates --production PATIENT_NAME PATIENT_PASSPORT PROCEDURE_CODE RESULT_CODE SERIAL TIMESTAMP");
    System.out.println();
    System.out.println("DESCRIPTION");
    System.out.println("    This notarizes a laboratory result using the GenoBank.io platform.");
    System.out.println("    Running on the production network is billable per your laboratory agreement.");
    System.out.println();
    System.out.println("    TWELVE_WORD_PHRASE a space-separated string of your twelve word phrase");
    System.out.println("    PATIENT_NAME       must match [A-Za-z0-9 .-]+");
    System.out.println("    PATIENT_PASSPORT   must match [A-Z0-9 -]+");
    System.out.println("    PROCEDURE_CODE     must be a procedure key in the Laboratory Procedure Taxonomy");
    System.out.println("    RESULT_CODE        must be a result key in the Laboratory Procedure Taxonomy");
    System.out.println("    SERIAL             must match [A-Z0-9 -]*");
    System.out.println("    TIMESTAMP          must match [0-9]+ representing the procedure/sample collection time");
    System.out.println();
    System.out.println("OUTPUT");
    System.out.println("    A complete URL for the certificate is printed to standard output.");
    System.out.println("    Please note: you should keep a copy of this output because you paid for it");
    System.out.println("    and nobody else has a copy or can recreate it for you.");
    System.out.println();
    System.out.println("REFERENCES");
    System.out.println("    Laboratory Procedure Taxonomy (test):");
    System.out.println("    https://genobank.io/certificates/laboratoryProcedureTaxonomy.json");
    System.out.println();
    System.out.println("    Laboratory Procedure Taxonomy (production):");
    System.out.println("    https://genobank.io/test/certificates/laboratoryProcedureTaxonomy.json");
  }
}
