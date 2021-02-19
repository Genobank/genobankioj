package io.genobank;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.URI;
import java.time.Instant;

public class Platform {
  private Network network;

  Platform(Network network) {
    java.util.Objects.requireNonNull(network);
    this.network = network;
  }

  public NotarizedCertificate notarize(
    PermitteeRepresentations representations, 
    byte[] signature
  ) {
    HttpClient client = HttpClient.newHttpClient();

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://httpbin.org/post"))
        .header("Content-Type", "application/json")
        .POST(BodyPublishers.ofString("request string"))
        .build();
    try {
      HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
      System.out.println(response.body());
    } catch(java.io.IOException exception) {
      System.out.println(exception);
    } catch(java.lang.InterruptedException exception) {
      System.out.println(exception);
    }

    return new NotarizedCertificate(
      representations,
      signature,
      Instant.ofEpochSecond(9999999999L), // TODO: use real time
      signature                            // TODO: use from GenoBank respnose
    );
  }
}