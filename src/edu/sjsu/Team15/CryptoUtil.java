package edu.sjsu.Team15;

import io.github.novacrypto.SecureCharBuffer;
import net.codesup.utilities.basen.AnyBaseEncoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class CryptoUtil {
    // uses sha3-256
    public static byte[] hashByteOut(String salt, byte[] input) {
        MessageDigest digest = null;
        try { digest = MessageDigest.getInstance("SHA3-256"); }
        catch (java.security.NoSuchAlgorithmException e) { System.exit(1); }
        digest.update(salt.getBytes());
        digest.update(input);
        return digest.digest();
    }

    // https://howtodoinjava.com/java/java-security/aes-256-encryption-decryption/
    public static byte[] encrypt(SecureCharBuffer secureKey, String username, byte[] input) {
        try
        {
            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            SecretKeySpec secretKey = AESHelper(secureKey, username);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            return cipher.doFinal(input);
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    // https://howtodoinjava.com/java/java-security/aes-256-encryption-decryption/
    public static byte[] decrypt(SecureCharBuffer secureKey, String username, byte[] input) {
        try
        {
            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            SecretKeySpec secretKey = AESHelper(secureKey, username);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");

            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            return cipher.doFinal(input);
        }
        catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }

    public static byte[] universalDecrypt(byte[] input) {
//        byte[] hardwareAddress;
//        try {
//            InetAddress localHost = InetAddress.getLocalHost();
//            NetworkInterface ni = NetworkInterface.getByInetAddress(localHost);
//            hardwareAddress = ni.getHardwareAddress();
//        }
//        catch (Exception e) {
//            hardwareAddress = new byte[32];
//        }

        SecureCharBuffer charBuffer = new SecureCharBuffer();
        // TODO TESTING
//        for (byte b : hardwareAddress) {
//            charBuffer.append((char) b);
//        }

        return encrypt(charBuffer, "", input);
    }

    // https://github.com/mklemm/base-n-codec-java
    public static byte[] base94encode(byte[] input) {
        return AnyBaseEncoder.BASE_94.encode(input).getBytes();
    }

    // SHA3-256 with character output
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

    // SecureCharBuffer to
    private static char[] parseMasterKey(String salt, SecureCharBuffer secureKey) {
        byte[] byteMasterKey = new byte[secureKey.capacity()];
        for (int i = 0; i < secureKey.length(); i++)
            byteMasterKey[i] = (byte) secureKey.charAt(i);
        return hashCharOut(salt, byteMasterKey);
    }

    // repeated code for encrypt and decrypt AES
    private static SecretKeySpec AESHelper(SecureCharBuffer secureKey, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        char[] masterKey = parseMasterKey(salt, secureKey);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(masterKey, // secret key
                salt.getBytes(), // salt
                65536, // iteration count
                256); // key length
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }
}
