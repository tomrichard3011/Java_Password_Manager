package JUnit4Tests;

import edu.sjsu.Team15.utility.PasswordChecker;
import edu.sjsu.Team15.utility.PasswordGenerator;
import io.github.novacrypto.SecureCharBuffer;
import org.junit.*;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests that password generation checks are working
 */
public class PasswordCheckerTest {
    @Test
    public void badPasswordLength(){
        SecureCharBuffer charBuffer = new SecureCharBuffer();
        charBuffer.append("aA1!");
        assertFalse(PasswordChecker.checkPass(charBuffer, new Date()));
    }

    @Test
    public void badPasswordCharacters(){
        SecureCharBuffer charBuffer = new SecureCharBuffer();
        charBuffer.append("thisisalongpassword");
        assertFalse(PasswordChecker.checkPass(charBuffer, new Date()));
    }

    @Test
    public void badPasswordDate(){
        SecureCharBuffer charBuffer = PasswordGenerator.generatePassword();
        assertFalse(PasswordChecker.checkPass(charBuffer, new Date(Calendar.YEAR-1)));
    }

    @Test
    public void validRandomPassword() {
        SecureCharBuffer charBuffer = PasswordGenerator.generatePassword();
        assertTrue(PasswordChecker.checkPass(charBuffer, new Date()));
    }

}
