package io.genobank;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.URI;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.web3j.utils.Numeric;

public class Platform {
  private Network network;
  private PermitteeSigner signer;

  Platform(Network network, PermitteeSigner signer) {
    java.util.Objects.requireNonNull(network);
    this.network = network;
    this.signer = signer;
  }

  public NotarizedCertificate notarize(
    PermitteeRepresentations representations,
    byte[] signature
  ) {
    URI targetUrl = URI.create(network.apiUrlBase + "certificates");
    String requestBody = "{" +
      "\"claim\":\"" + Numeric.toHexString(representations.getClaim()) + "\"," +
      "\"signature\":\"" + Numeric.toHexString(signature) + "\"," +
      "\"permitteeSerial\":" + signer.permitteeId +
      "}";

    
      /*
    System.err.println("Preparing request to API server");
    System.err.println("URL:         " + targetUrl);*/
    System.err.println("Body:        " + requestBody);
    
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
      .uri(targetUrl)
      .header("Content-Type", "application/json")
      .POST(BodyPublishers.ofString(requestBody))
      .build();

    String txHash;
    String certificateTimestamp;
    String genoBankIoSignature;

    try {
      HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
      String responseString = (String) response.body();
      // System.err.println("Response:    " + responseString);

      // Choose you favorite JSON library for this task
      if (!responseString.contains("\"status\":200")) {
        throw new RuntimeException("Server error a");
      }

      String txHashPatternString = "\"txHash\":\\w*\"(0x[a-f0-9]{64})\"";
      Pattern txHashPattern = Pattern.compile(txHashPatternString);
      Matcher txHashMatcher = txHashPattern.matcher(responseString);
      if (txHashMatcher.find( )) {
        txHash = txHashMatcher.group(1);
      } else {
        throw new RuntimeException("Server error b");
      }

      String certificateTimestampPatternString = "\"timestamp\":\\w*\"([0-9TZ.:-]+)\"";
      Pattern certificateTimestampPattern = Pattern.compile(certificateTimestampPatternString);
      Matcher certificateTimestampMatcher = certificateTimestampPattern.matcher(responseString);
      if (certificateTimestampMatcher.find()) {
        certificateTimestamp = certificateTimestampMatcher.group(1);
      } else {
        throw new RuntimeException("Server error c");
      }

      String genoBankIoSignaturePatternString = "\"genobankSignature\":\\w*\"(0x[a-f0-9]{130})\"";
      Pattern genoBankIoSignaturePattern = Pattern.compile(genoBankIoSignaturePatternString);
      Matcher genoBankIoSignatureMatcher = genoBankIoSignaturePattern.matcher(responseString);
      if (genoBankIoSignatureMatcher.find()) {
        genoBankIoSignature = genoBankIoSignatureMatcher.group(1);
      } else {
        throw new RuntimeException("Server error d");
      }
    } catch(java.io.IOException exception) {
      throw new RuntimeException(exception.getMessage());
    } catch(java.lang.InterruptedException exception) {
      throw new RuntimeException(exception.getMessage());
    }

    return new NotarizedCertificate(
      representations,
      signature,
      Instant.parse(certificateTimestamp),
      Numeric.hexStringToByteArray(genoBankIoSignature),
      Numeric.hexStringToByteArray(txHash)
    );
  }
}
