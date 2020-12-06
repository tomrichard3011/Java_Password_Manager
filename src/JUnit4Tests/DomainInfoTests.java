package JUnit4Tests;

import edu.sjsu.Team15.model.DomainInfo;
import io.github.novacrypto.SecureCharBuffer;
import org.junit.*;

import static org.junit.Assert.assertEquals;

public class DomainInfoTests {
    DomainInfo domainInfo;
    String domain;
    String username;
    SecureCharBuffer password;


    @Before
    public void setup() {
        domain = "google";
        username = "user";
        password = new SecureCharBuffer();
        password.append("pass");
        domainInfo = new DomainInfo(domain, username, password);
    }

    @Test
    public void getLogoPath() {
        String fileSep = System.getProperty("file.separator");
        String filePath = System.getProperty("user.dir") + fileSep + "images" + fileSep;
        assertEquals(filePath + domain + ".png", domainInfo.getLogoPath());
    }

    @Test
    public void getDomain() {
        assertEquals("google", domainInfo.getDomain());
    }

    @Test
    public void getPassword() {
        assertEquals("pass", domainInfo.getPassword().toStringAble().toString());
    }

    @Test
    public void getUsername() {
        assertEquals("user", domainInfo.getUsername());
    }

    @Test
    public void setPassword() {
        SecureCharBuffer newPass = new SecureCharBuffer();
        newPass.append("newPass");
        domainInfo.setPassword(newPass);
        assertEquals("newPass", domainInfo.getPassword().toStringAble().toString());
    }

    @Test
    public void setUsername() {
        domainInfo.setUsername("newUser");
        assertEquals("newUser", domainInfo.getUsername());
    }

    @Test
    public void toStringAble() {
        assertEquals(domainInfo.getDomain(), domainInfo.toString());
    }
}
