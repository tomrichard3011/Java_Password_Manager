package JUnit4Tests;
import java.io.File;
import java.util.ArrayList;

import edu.sjsu.Team15.HandlerConstants;
import edu.sjsu.Team15.model.DomainHandler;
import edu.sjsu.Team15.model.DomainInfo;
import edu.sjsu.Team15.model.User;
import edu.sjsu.Team15.model.UserHandler;
import io.github.novacrypto.SecureCharBuffer;

// JUnit 4 Test Suite
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class XMLTest {
	
	/**
	@Before
    public void setUp() throws Exception
    {
		//
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void getMaxOfAnEmptyHeap() {
	    while(true) {
	 	   heap.extractMax();
	    }
    }
    **/
	
	public static void main(String[] args) {
		/**
		//File actile = new File("\\xmlTests\\test.xml");
		File actile = new File("C:\\Users\\Guerr\\eclipse-workspace\\PassMan\\xmlTests\\test.xml");
		//File secile = new File("\\xmlTests\\secTest.a");
		File secile = new File("C:\\Users\\Guerr\\eclipse-workspace\\PassMan\\xmlTests\\secTest.a");
		String username = "username";
		SecureCharBuffer password = new SecureCharBuffer();
		password.append("password");
		**/
		
		
		// XML File Constants: User File
		String fileSep = System.getProperty("file.separator");

		String userFilepath = System.getProperty("user.dir") + fileSep + "xmlTests" + fileSep + "user.xyt";
		String userSalt = "user";
		SecureCharBuffer internalKey = new SecureCharBuffer();
		internalKey.append("special");
		
		// XML File Constants: Domain File
		String filepath = System.getProperty("user.dir") + fileSep + "xmlTests" + fileSep + userSalt + "domains.xyt";
		String salt = "username";
		SecureCharBuffer key = new SecureCharBuffer();
		key.append("password");
		
		// Create an xml file
		File encryptedUserFile = UserHandler.createNewUserFile(new File(userFilepath));
		
		try {
			// Add a user (UserHandler)
			UserHandler uHandler = new UserHandler(encryptedUserFile.getAbsolutePath());
			// Test 2: Check for a user that doesn't exist in the file (Done above)
			System.out.print("This should be false: ");
			System.out.println(uHandler.checkUser(salt));
			User person = uHandler.addUser(salt, key, 200, new File(filepath)); // add user
			
			// Check that the user is there
			System.out.println("The line below me should be true:");
			System.out.println(uHandler.checkUser(salt));
			
			// By adding the user, we get a user object. Now, let's try to pull that from the file
			UserHandler anotherHandler = new UserHandler(encryptedUserFile.getAbsolutePath());
			User samePerson = anotherHandler.verifyUser(salt, key);
			
			// Test 1: Confirm it was added to the file
			System.out.println("Comparing user against itself");
			// Compare that both names are the same
			System.out.println(person.getUsername());
			System.out.println(samePerson.getUsername());
			// Compare that both passwords are the same
			System.out.println(person.getMasterKey().toStringAble());
			System.out.println(samePerson.getMasterKey().toStringAble());
			// Compare that both DomainInfo arrays are empty
			System.out.println(person.getDomainInfoArray().size());
			System.out.println(samePerson.getDomainInfoArray().size());
			// Compare that both file paths are the same
			System.out.println(person.getFileLocation());
			System.out.println(samePerson.getFileLocation());
			
			// Test 3: Edit the existing user
			anotherHandler.editUser(salt, key, HandlerConstants.XMLUSER, "notusername");
			salt = "notusername";
			if(anotherHandler.checkUser("notusername")) {
				System.out.println("Editing is working!");
			} else {
				throw new Exception("Editing is not working.");
			}
			
			// Test 4: Add a user & remove the original user
			anotherHandler.addUser("delete me", internalKey, 100, new File(System.getProperty("user.dir") + fileSep + "xmlTests" + fileSep + "merry.xyt"));
			if(anotherHandler.checkUser("delete me")) {
				System.out.println("User added");
			}
			anotherHandler.deleteUser("delete me", internalKey);
			if(!anotherHandler.checkUser("delete me")) {
				System.out.println("User deleted");
			} else {
				throw new Exception("Delete failed.");
			}
			
			// Test DomainHandler
			User person1 = anotherHandler.verifyUser(salt, key);
			ArrayList<DomainInfo> list = person1.getDomainInfoArray();
			SecureCharBuffer pass = new SecureCharBuffer();
			pass.append("somehashedpassword");
			list.add(new DomainInfo("google", "generic.user@gmail.com", pass));
			list.add(new DomainInfo("yahoo", "generic.user@yahoo.com", pass));
			list.add(new DomainInfo("canvas", "canvas", pass));
			DomainHandler domHandler = new DomainHandler(person1.getFileLocation().toString(), key, salt);
			domHandler.writeDomains(list);
			
			// Check that the domains are being written correctly
			ArrayList<DomainInfo> checking = domHandler.getDomains();
			for(int i = 0; i < checking.size(); i++) {
				System.out.println(checking.get(i).getDomain());
				System.out.println(checking.get(i).getUsername());
				System.out.println(checking.get(i).getPassword().toStringAble());
			}
			
			// public DomainInfo(String domain, String username, SecureCharBuffer password)
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		// UserHandler(String filepath)
		// UserHandler(String filepath, SecureCharBuffer key, String salt)
		// DomainHandler(String filepath, SecureCharBuffer key, String salt)
		
		// public static File createNewUserFile(String username, SecureCharBuffer password, File secureFileLocation)
		// public static File createNewDomainFile(String username, SecureCharBuffer password, File secureFileLocation)
		
		// User files
		// public User verifyUser(String username, SecureCharBuffer password)
		// public boolean checkUser(String username)
		// public User addUser(String username, SecureCharBuffer password, int clipboardTime, File secureFile)
		
		// Domain files
		// public ArrayList<DomainInfo> getDomains()
		// public boolean writeDomains(ArrayList<DomainInfo> domains)
		
		// Write to the xml
		
		// Read from the xml
		
		// Other query stuff
	}
}
