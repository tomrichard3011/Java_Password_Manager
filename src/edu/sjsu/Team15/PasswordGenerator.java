package edu.sjsu.Team15;

import io.github.novacrypto.SecureCharBuffer;
import net.codesup.utilities.basen.AnyBaseEncoder;

import java.math.BigInteger;

public class PasswordGenerator {
    public static SecureCharBuffer generatePassword(String salt) {
        SecureCharBuffer secCharBuff = new SecureCharBuffer();
        secCharBuff.append(
                AnyBaseEncoder.BASE_94.encode(
                        new BigInteger(1,
                                CryptoUtil.hashByteOut(salt, CryptoUtil.randomNumberGen())
                        ))); // big one liner to avoid storing password in memory for too long
        return secCharBuff;
    }
}
