package JUnit4Tests;

import edu.sjsu.Team15.model.DomainInfo;
import edu.sjsu.Team15.model.User;
import io.github.novacrypto.SecureCharBuffer;
import org.junit.*;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserTest {
    User user;
    String username;
    ArrayList<DomainInfo> domainInfoArrayList;
    int clipboardClearTime;
    SecureCharBuffer password;


    @Before
    public void setup() {
        username = "Steve";
        domainInfoArrayList = new ArrayList<>();
        clipboardClearTime = 5;
        password = new SecureCharBuffer();
        password.append("password");
        user = new User(clipboardClearTime, domainInfoArrayList, username, password);
    }

    @Test
    public void regularConstructor() {
        user = new User(clipboardClearTime, domainInfoArrayList, username, password);
        assertNotNull(user);
    }

    @Test
    public void sansDomainInfoArrayConstructor() {
        // username, password, clipboard clear time, filepath
        user = new User(username, password, clipboardClearTime, System.getProperty("user.dir"));
        assertNotNull(user);
    }

    @Test
    public void getClipboardClearTime() {
        assertEquals(5, user.getClipboardClearTime());
    }

    @Test
    public void getUsername() {
        assertEquals("Steve", user.getUsername());
    }

    @Test
    public void getPassword() {
        assertEquals("password", user.getMasterKey().toStringAble().toString());
    }

    @Test
    public void setClipboardClearTime() {
        user.setClipboardClearTime(3);
        assertEquals(3, user.getClipboardClearTime());
    }

    @Test
    public void setUsername() {
        user.setUsername("new name");
        assertEquals("new name", user.getUsername());
    }

    @Test
    public void setPassword() {
        SecureCharBuffer newPass = new SecureCharBuffer();
        newPass.append("new password");
        user.setMasterKey(newPass);
        assertEquals("new password", user.getMasterKey().toStringAble().toString());
    }

    @Test
    public void setFileLocation() {
        user.setFileLocation(new File(System.getProperty("user.home")));
        assertEquals(System.getProperty("user.home"), user.getFileLocation().getAbsolutePath());
    }
}
