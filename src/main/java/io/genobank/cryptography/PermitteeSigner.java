package io.genobank;

import java.io.ByteArrayOutputStream;

import org.web3j.crypto.MnemonicUtils;
import org.web3j.utils.Numeric;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.utils.Numeric;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.crypto.Bip32ECKeyPair;
import org.web3j.utils.Numeric;
import static org.web3j.crypto.Bip32ECKeyPair.HARDENED_BIT;


//TODO:  SEE notes on signing, padding, etc. https://github.com/web3j/web3j/issues/208

public class PermitteeSigner {
  public Credentials credentials;

  public byte[] signature;

  // Load an Ethereum wallet: https://gitter.im/web3j/web3j?at=5cb437ba31aec969e8aaed08
  // Test https://www.trufflesuite.com/docs/truffle/getting-started/using-truffle-develop-and-the-console
  // See also https://github.com/web3j/web3j/issues/1369
  public PermitteeSigner(String twelveWordPassphrase) {
    String mnemonicPassword = null;
    Bip32ECKeyPair masterKeyPair = Bip32ECKeyPair.generateKeyPair(MnemonicUtils.generateSeed(twelveWordPassphrase, mnemonicPassword));
    int[] path = {44 | HARDENED_BIT, 60 | HARDENED_BIT, 0 | HARDENED_BIT, 0, 0};
    Bip32ECKeyPair keyPair = Bip32ECKeyPair.deriveKeyPair(masterKeyPair, path);
    this.credentials = Credentials.create(keyPair);
  }

  public byte[] sign(PermitteeRepresentations representations) {

    String password = null;
    String mnemonic = "candy maple cake sugar pudding cream honey rich smooth crumble sweet treat";
    String message = "a message";

    // How to load an Ethereum wallet explained at https://gitter.im/web3j/web3j?at=5cb437ba31aec969e8aaed08
    // Test case https://www.trufflesuite.com/docs/truffle/getting-started/using-truffle-develop-and-the-console
    Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(MnemonicUtils.generateSeed(mnemonic, password));
    int[] path = {44 | HARDENED_BIT, 60 | HARDENED_BIT, 0 | HARDENED_BIT, 0,0};
    Bip32ECKeyPair x = Bip32ECKeyPair.deriveKeyPair(masterKeypair, path);
    Credentials credentials = Credentials.create(x);
    System.out.println("Address:     " + credentials.getAddress());

    // Sign
    Sign.SignatureData signature = Sign.signPrefixedMessage(message.getBytes(), credentials.getEcKeyPair());
    System.out.println("R:           " + Numeric.toHexString(signature.getR()));
    System.out.println("S:           " + Numeric.toHexString(signature.getS()));
    System.out.println("V:           " + Numeric.toHexString(signature.getV()));

/*    
    String claim = Hash.sha3(representations.getTightSerialization());
    System.out.println("Preparing permittee claim to sign");
    System.out.println(claim);
    System.out.println();

    System.out.println("Signing");
    Sign.SignatureData signature = Sign.signPrefixedMessage(claim.getBytes(), credentials.getEcKeyPair());
    System.out.println("R: " + Numeric.toHexString(signature.getR()));
    System.out.println("S: " + Numeric.toHexString(signature.getS()));
    System.out.println("V: " + Numeric.toHexString(signature.getV()));
*/





    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    try {
      outputStream.write(signature.getR());
      outputStream.write(signature.getS());
      outputStream.write(signature.getV());
    } catch (java.io.IOException exception) {
      throw new RuntimeException("ByteArrayOutputStream should not be this difficult");
    }
    return outputStream.toByteArray();
  }
}
