package io.genobank;

import java.net.URI;

public enum Network {
  TEST(
    "io.genobank.test", 
    "https://genobank.io/test/certificates/verify-certificate-v1.1#",
    "https://api-test.genobank.io/",
    "0x795faFFc58648e435E3bD3196C4F75F8EFc4b306"
    ),
  LOCAL(
    "io.genobank.test", 
    "http://localhost:5501/test/certificates/verify-certificate-v1.1.html#",
    "https://api-test.genobank.io/",
    "0x795faFFc58648e435E3bD3196C4F75F8EFc4b306"
    ),
  PRODUCTION(
    "io.genobank",
    "https://genobank.io/certificates/verify-certificate-v1#",
    "https://api.genobank.io/",
    "0x633F5500A87C3DbB9c15f4D41eD5A33DacaF4184"
  );

  public final String namespacePrefix;
  public final URI certificateUrlBase;
  public final URI apiUrlBase;
  public final String genoBankIoAddress;

  private Network(
    String namespacePrefix,
    String certificateUrlBase,
    String apiUrlBase,
    String genoBankIoAddress) {
    try {
      this.namespacePrefix = namespacePrefix;
      this.certificateUrlBase = URI.create(certificateUrlBase);
      this.apiUrlBase = URI.create(apiUrlBase);
      this.genoBankIoAddress = genoBankIoAddress;
    } catch(Exception exception) {
      throw new RuntimeException("URI base error");
    }
  }
}
