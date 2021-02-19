package io.genobank;

public enum Network {
  TEST("io.genobank.test"),
  PRODUCTION("io.genobank");

  public final String namespacePrefix;

  private Network(String namespacePrefix) {
    this.namespacePrefix = namespacePrefix;
  }
}