package io.genobank;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import org.web3j.crypto.Bip32ECKeyPair;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;
import static org.web3j.crypto.Bip32ECKeyPair.HARDENED_BIT;

public class PermitteeSigner {
  public final Credentials credentials;
  public final Integer permitteeId;

  public byte[] signature;

  // Load an Ethereum wallet: https://gitter.im/web3j/web3j?at=5cb437ba31aec969e8aaed08
  // Test https://www.trufflesuite.com/docs/truffle/getting-started/using-truffle-develop-and-the-console
  // See also https://github.com/web3j/web3j/issues/1369
  public PermitteeSigner(String twelveWordPassphrase, Integer permitteeId) {
    String mnemonicPassword = null;
    Bip32ECKeyPair masterKeyPair = Bip32ECKeyPair.generateKeyPair(MnemonicUtils.generateSeed(twelveWordPassphrase, mnemonicPassword));
    int[] path = {44 | HARDENED_BIT, 60 | HARDENED_BIT, 0 | HARDENED_BIT, 0, 0};
    Bip32ECKeyPair keyPair = Bip32ECKeyPair.deriveKeyPair(masterKeyPair, path);
    credentials = Credentials.create(keyPair);
    this.permitteeId = permitteeId;
  }

  public byte[] sign(PermitteeRepresentations representations) {
    byte[] message = representations.getFullSerialization().getBytes(StandardCharsets.UTF_8);
    Sign.SignatureData signature = Sign.signPrefixedMessage(message, credentials.getEcKeyPair());

    // Match the signature output format as Ethers.js v5.0.31
    // https://github.com/ethers-io/ethers.js/blob/v5.0.31/packages/bytes/src.ts/index.ts#L444-L448
    byte[] retval = new byte[65];
    System.arraycopy(signature.getR(), 0, retval, 0, 32);
    System.arraycopy(signature.getS(), 0, retval, 32, 32);
    System.arraycopy(signature.getV(), 0, retval, 64, 1);
    return retval;
  }
}
