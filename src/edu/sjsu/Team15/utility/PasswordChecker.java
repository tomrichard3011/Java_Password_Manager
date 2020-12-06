package edu.sjsu.Team15.utility;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import io.github.novacrypto.SecureCharBuffer;

/**
 * checks for valid passwords taking a SecureCharBuffer as an input
 */
public class PasswordChecker {
    /** regex pattern for a string that contains at least 8 characters, upper and lower case, digit, and symbol */
    private static final String regexPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

    /**
     * Checks if password contains numbers, lowercase uppercase letters, and symbols and isn't older than 3 months
     * @param pass password to check
     * @param passAge how old is the password
     * @return true if password is valid
     */
    public static boolean checkPass(SecureCharBuffer pass, Date passAge) {
        return checkPassRegex(pass) && checkPassAge(passAge);
    }

    /**
     * Checks if password contains numbers, lowercase uppercase letters, and symbols
     * @param pass password to check
     * @return true if it is valid
     */
    private static boolean checkPassRegex(SecureCharBuffer pass) {
        return Pattern.matches(regexPattern, pass);
    }

    /**
     * Checks if password isn't older than 3 months
     * @param passAge how old is the password
     * @return true if password is valid
     */
    private static boolean checkPassAge(Date passAge) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(passAge);
        cal.add(Calendar.MONTH, 3);

        Date maxAge = cal.getTime();
        Date today = new Date();
        return today.before(maxAge);
    }


}