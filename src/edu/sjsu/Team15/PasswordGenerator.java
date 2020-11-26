package edu.sjsu.Team15;

import io.github.novacrypto.SecureCharBuffer;
import net.codesup.utilities.basen.AnyBaseEncoder;

import java.math.BigInteger;
import java.security.SecureRandom;

public class PasswordGenerator {
    // generate secureCharBuffer password
    public static SecureCharBuffer generatePassword() {
        int length = 15;
        SecureCharBuffer secCharBuff = new SecureCharBuffer();
        char[] pass = AnyBaseEncoder.BASE_94.encode(
                new BigInteger(1,
                        CryptoUtil.hashByteOut(randomSaltGen(), randomNumberGen())
                )).toCharArray(); // big one liner to avoid allocating too much memory

        length = Math.min(length, pass.length);

        for (int i = 0; i < length; i++) {
            secCharBuff.append(pass[i]);
            pass[i] = '\0';
        }

        return secCharBuff;
    }

    // random number using CSPRNG
    private static byte[] randomNumberGen() {
        SecureRandom secRanGen = null;

        // prefered CSPRNG is system specifc, slightly odd for java
        if(System.getProperty("os.name").startsWith("Windows"))
        {
            try { secRanGen = SecureRandom.getInstance("Windows-PRNG"); }
            catch (java.security.NoSuchAlgorithmException e) { System.exit(1); }
        }
        else
        {
            secRanGen = new SecureRandom();
        }

        byte[] result = new byte[32];
        secRanGen.nextBytes(result);
        return result;
    }

    // random salt using CSPRNG
    private static String randomSaltGen() {
        byte[] salt = new byte[16];
        StringBuilder sb = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);
        for (byte b : salt) {
            sb.append((char) b);
        }
        return sb.toString();
    }
}
