package JUnit4Tests;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import edu.sjsu.Team15.model.CryptoUtil;
import edu.sjsu.Team15.model.DomainInfo;
import edu.sjsu.Team15.model.User;
import edu.sjsu.Team15.model.UserHandler;
import io.github.novacrypto.SecureCharBuffer;

public class XMLTest {
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
		String userFilepath = "C:\\Users\\Guerr\\eclipse-workspace\\PassMan\\xmlTests\\user.xyt";
		String userSalt = "user";
		SecureCharBuffer internalKey = new SecureCharBuffer();
		internalKey.append("special");
		
		// XML File Constants: Domain File
		String filepath = "C:\\Users\\Guerr\\eclipse-workspace\\PassMan\\xmlTests\\domain.xyt";
		String salt = "username";
		SecureCharBuffer key = new SecureCharBuffer();
		key.append("password");
		
		// Create an xml file
		File encryptedUserFile = UserHandler.createNewUserFile(userSalt, internalKey, new File(userFilepath));
		
		// Add a user
		try {
			UserHandler uHandler = new UserHandler(encryptedUserFile.getAbsolutePath(), internalKey, userSalt);
			System.out.print("This should be false: ");
			System.out.println(uHandler.checkUser(salt));
			User person = uHandler.addUser(salt, key, 200, new File(filepath));
			
			// Check that the user is there
			System.out.println("The line below me should be true:");
			System.out.println(uHandler.checkUser(salt));
			
			// By adding the user, we get a user object. Now, let's try to pull that from the file
			UserHandler anotherHandler = new UserHandler(encryptedUserFile.getAbsolutePath(), internalKey, userSalt);
			User samePerson = anotherHandler.verifyUser(salt, key);
			
			// Testing
			System.out.println("Comparing user against itself");
			System.out.println(person.getUsername());
			System.out.println(samePerson.getUsername());
			System.out.println(person.getDomainInfoArray().size());
			System.out.println(samePerson.getDomainInfoArray().size());
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
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
