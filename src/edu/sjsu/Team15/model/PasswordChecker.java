package edu.sjsu.Team15.model;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import io.github.novacrypto.SecureCharBuffer;

public class PasswordChecker {
    private static final String regexPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

    public static boolean checkPass(SecureCharBuffer pass, Date passAge) {
        return checkPassRegex(pass) && checkPassAge(passAge);
    }

    private static boolean checkPassRegex(SecureCharBuffer pass) {
        return Pattern.matches(regexPattern, pass);
    }

    private static boolean checkPassAge(Date passAge) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(passAge);
        cal.add(Calendar.MONTH, 3);

        Date maxAge = cal.getTime();
        Date today = new Date();
        return today.before(maxAge);
    }


}