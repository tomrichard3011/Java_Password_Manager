package edu.sjsu.Team15;

import io.github.novacrypto.SecureCharBuffer;
import net.codesup.utilities.basen.AnyBaseEncoder;

import java.math.BigInteger;
import java.security.SecureRandom;

public class PasswordGenerator {
    public static SecureCharBuffer generatePassword(String salt) {
        SecureCharBuffer secCharBuff = new SecureCharBuffer();
        secCharBuff.append(
                AnyBaseEncoder.BASE_94.encode(
                        new BigInteger(1,
                                CryptoUtil.hashByteOut(salt, randomNumberGen())
                        ))); // big one liner to avoid allocating too much memory
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
}
