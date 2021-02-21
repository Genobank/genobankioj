package io.genobank;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.URI;
import java.time.Instant;

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
      "\"claim\":\"" + new String(representations.getClaim()) + "\"," +
      "\"signature\":\"" + Numeric.toHexString(signature) + "\"," +
      "\"permitteeSerial\":" + signer.permitteeId +
      "}";

    System.out.println("Preparing request to API server");
    System.out.println("URL:         " + targetUrl);
    System.out.println("Body:        " + requestBody);
    
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
    .uri(targetUrl)
    .header("Content-Type", "application/json")
    .POST(BodyPublishers.ofString(requestBody))
    .build();
    try {
      HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
      System.out.println("Response:    " + response.body());
//todo: validate if this is an error      
    } catch(java.io.IOException exception) {
      System.out.println(exception);
    } catch(java.lang.InterruptedException exception) {
      System.out.println(exception);
    }

    return new NotarizedCertificate(
      representations,
      signature,
      Instant.ofEpochSecond(9999999999L), // TODO: use real time FROM RESPONSE
      signature                            // TODO: use from GenoBank respnose FROM RESPONSE
    );
  }
}