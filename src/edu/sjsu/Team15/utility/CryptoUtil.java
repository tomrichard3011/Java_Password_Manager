package edu.sjsu.Team15.utility;

import io.github.novacrypto.SecureCharBuffer;
import net.codesup.utilities.basen.AnyBaseEncoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.MessageDigest;
import java.security.spec.KeySpec;

/**
 * Handles all functions dealing with cryptography, such as hashing, encryption, and some encoding
 */
public class CryptoUtil {
    /**
     * SHA3-256 hash function
     * @param salt salt used for hashing
     * @param input data to hash
     * @return always returns 32 byte hash
     */
    public static byte[] hashByteOut(String salt, byte[] input) {
        MessageDigest digest = null;
        try { digest = MessageDigest.getInstance("SHA3-256"); }
        catch (java.security.NoSuchAlgorithmException e) { System.exit(1); }
        digest.update(salt.getBytes());
        digest.update(input);
        return digest.digest();
    }

    // https://howtodoinjava.com/java/java-security/aes-256-encryption-decryption/
    /**
     * Symmetric key encryption using a secret key and username as salt
     * @param secureKey Master key to encrypt
     * @param salt salt used for encryption
     * @param input plain text data to encrypt
     * @return ciphertext as variable length byte array
     */
    public static byte[] encrypt(SecureCharBuffer secureKey, String salt, byte[] input) {
        try
        {
            SecretKeySpec secretKey = AESHelper(secureKey, salt);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    secretKey,
                    new IvParameterSpec(new byte[16])
            );
            return cipher.doFinal(input);
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    // https://howtodoinjava.com/java/java-security/aes-256-encryption-decryption/
    /**
     * Symmetric key decryption using secret key and username as salt
     * @param secureKey symmetric key for encryption
     * @param salt salt used for encryption
     * @param input cipher text to be decrypted
     * @return plaintext as byte[]
     */
    public static byte[] decrypt(SecureCharBuffer secureKey, String salt, byte[] input) {
        try
        {
            SecretKeySpec secretKey = AESHelper(secureKey, salt);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(
                    Cipher.DECRYPT_MODE,
                    secretKey,
                    new IvParameterSpec(new byte[16])
            );
            return cipher.doFinal(input);
        }
        catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }


    /**
     * Encryption using user specific data as encryption key
     * @param input plaintext data as byte array
     * @return encrypted data as plaintext
     */
    // TODO NOT SECURE, need a reliable way of creating machine specific keys
    public static byte[] universalEncrypt(byte[] input) {
        SecureCharBuffer charBuffer = new SecureCharBuffer();
        charBuffer.append(System.getProperty("user.dir"));

        return encrypt(charBuffer, System.getProperty("os.name"), input);
    }

    /**
     * Decryption using user specific data as encryption key
     * @param input encrypted data
     * @return decrypted data
     */
    // TODO NOT SECURE, need a reliable way of creating machine specific keys
    public static byte[] universalDecrypt(byte[] input) {
        SecureCharBuffer charBuffer = new SecureCharBuffer();
        charBuffer.append(System.getProperty("user.dir"));

        return decrypt(charBuffer, System.getProperty("os.name"), input);
    }

    // https://github.com/mklemm/base-n-codec-java

    /**
     * Encodes byte array to base94 to fit ascii safe characters
     * @param input data to be encoded
     * @return encoded data
     */
    public static byte[] base94encode(byte[] input) {
        return AnyBaseEncoder.BASE_94.encode(input).getBytes();
    }

    /**
     * Sha3-256 with char[] output used internally
     * @param salt salt used for hashing
     * @param input data to hash
     * @return always returns 32 char[]
     */
    private static char[] hashCharOut(String salt, byte[] input) {
        byte[] byteResult = hashByteOut(salt, input);
        char[] result = new char[byteResult.length];
        for (int i = 0; i < byteResult.length; i++)
        {
            result[i] = (char) byteResult[i];
            byteResult[i] = 0;
        }

        return result;
    }

    /**
     * Gets hash of of secureCharBuffer
     * @param salt salt used for hashing
     * @param secureKey key to transform into hash
     * @return char[] of masterkey
     */
    private static char[] parseMasterKey(String salt, SecureCharBuffer secureKey) {
        byte[] byteMasterKey = new byte[secureKey.capacity()];
        for (int i = 0; i < secureKey.length(); i++)
            byteMasterKey[i] = (byte) secureKey.charAt(i);
        return hashCharOut(salt, byteMasterKey);
    }

    /**
     * Helper method for AES
     * @param secureKey Master key for encryption
     * @param salt salt for encryption
     * @return SecretKey Spec used for AES encryption
     */
    private static SecretKeySpec AESHelper(SecureCharBuffer secureKey, String salt) {
        char[] masterKey = parseMasterKey(salt, secureKey);
        SecretKey tmp = null;
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(masterKey, // secret key
                    salt.getBytes(), // salt
                    65536, // iteration count
                    256); // key length
            tmp = factory.generateSecret(spec);
        }
        catch (Exception e) {
            System.exit(1);
        }
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }
}
