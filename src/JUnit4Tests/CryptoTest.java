package JUnit4Tests;

import edu.sjsu.Team15.utility.CryptoUtil;
import io.github.novacrypto.SecureCharBuffer;

import org.junit.*;

import static org.junit.Assert.*;


public class CryptoTest {
    byte[] password;
    SecureCharBuffer key;
    String salt;

    @Before
    public void init() {
        try {
            password = "password".getBytes();
        }
        catch (Exception e) {
            return;
        }
        key = new SecureCharBuffer();
        salt = "salt";

        key.append('b'); // key
    }

    @Test
    public void SHA256Test() {
        byte[] cipher = CryptoUtil.hashByteOut(salt, password);

        //String ciphertext = new BigInteger(1, cipher).toString(16);

        //System.out.println("SHA3-256: " + ciphertext);
        assertEquals(32, cipher.length);
    }

    @Test
    public void base94() {
        byte[] encoded = CryptoUtil.base94encode(password); //
        String s = new String(encoded);

        assertEquals("y-0[$uL>+Qk8jNb,'TwF", s);
    }

    @Test
    public void secretKeyencryption_Decryption() {
        // encryption
        byte[] encryptedBytes = CryptoUtil.encrypt(key, salt, password); // encrypt the password with a masterkey, key, and a password

        // decryption
        byte[] decryptedBytes = CryptoUtil.decrypt(key, salt, encryptedBytes); // decrypt password with same salt and key

        String decodedStr = new String(decryptedBytes);
                assertEquals("password", decodedStr);
    }

    @Test
    public void universalEncryption_Decryption() {
        byte[] encryptedBytes = CryptoUtil.universalEncrypt(password);
        byte[] decryptedBytes = CryptoUtil.universalDecrypt(encryptedBytes);

        String decodedStr = new String(decryptedBytes);
        assertEquals("password", decodedStr);
    }
}
