package io.genobank;

public enum Network {
  TEST(
    "io.genobank.test", 
    "https://genobank.io/test/certificates/verify-certificate-v1#"
    ),
  PRODUCTION(
    "io.genobank",
    "https://genobank.io/certificates/verify-certificate-v1#"
  );

  public final String namespacePrefix;
  public final String certificateUrlBase;

  private Network(String namespacePrefix, String certificateUrlBase) {
    this.namespacePrefix = namespacePrefix;
    this.certificateUrlBase = certificateUrlBase;
  }
}