package io.genobank;

import org.web3j.crypto.MnemonicUtils;
import org.web3j.utils.Numeric;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;

//notes on signing, padding, etc. https://github.com/web3j/web3j/issues/208

public class PermitteeSigner {
  private Credentials credentials;

  public byte[] signature;

  public PermitteeSigner(String twelveWordPassphrase) {
    System.out.println("Loading account from mnemonic");
    byte[] seed = MnemonicUtils.generateSeed(twelveWordPassphrase, null);
    credentials = Credentials.create(bytesToHex(seed));
    System.out.println("Address: " + credentials.getAddress());
    System.out.println("Private key: " + credentials.getEcKeyPair().getPrivateKey().toString(16)); // TODO take this out
    System.out.println("");
  }

  public byte[] sign(PermitteeRepresentations representations) {
    String claim = Hash.sha3(representations.getTightSerialization());
    System.out.println("Preparing permittee claim to sign");
    System.out.println(claim);
    System.out.println("");

    System.out.println("Signing");
    // Signing currently crashes, todo see https://github.com/web3j/web3j/issues/1369
    /*
    Sign.SignatureData signature = Sign.signPrefixedMessage(claim.getBytes(), credentials.getEcKeyPair());
    System.out.println("R: " + Numeric.toHexString(signature.getR()));
    System.out.println("S: " + Numeric.toHexString(signature.getS()));
    System.out.println("V: " + Numeric.toHexString(signature.getV()));
    */

    return "hi".getBytes(); // TODO get real value
  }

  // TODO web3j has a method for this probably
  private String bytesToHex(byte[] bytes) {
    final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

    char[] chars = new char[2 * bytes.length];
    for (int i = 0; i < bytes.length; ++i) {
        chars[2 * i] = HEX_CHARS[(bytes[i] & 0xF0) >>> 4];
        chars[2 * i + 1] = HEX_CHARS[bytes[i] & 0x0F];
    }
    return new String(chars);
  }
}
