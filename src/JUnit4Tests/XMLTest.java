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

/**
 * Notes:
 * For every test case, a new user file is generated, to prevent issues in other tests.
 * Considering moving to @before, but some test cases could be designed around that, so 
 * it's left open. Just be careful when adding test cases.
 * 
 * @author Nicolas Guerrero
 */
public class XMLTest {
	// Basic File Necessities
	String fileSep;
	String directoryPath;
	String userFilePath;
	
	// Sample Users
	User user1;
	User user2;
	User user3;
	
	// Corresponding sample domain lists (may or may not use)
	ArrayList<DomainInfo> domain1;
	ArrayList<DomainInfo> domain2;
	ArrayList<DomainInfo> domain3;
	
	@Before
    public void initialize() throws Exception
    {
		// File Management System
		fileSep = System.getProperty("file.separator");
		directoryPath = System.getProperty("user.dir") + fileSep + "xmlTests" + fileSep;
		userFilePath = directoryPath + "user.xyt";
		
		// User 1 Information
		String salt1 = "user1";
		SecureCharBuffer pass1 = new SecureCharBuffer();
		pass1.append("mainpassword1");
		SecureCharBuffer subpass1 = new SecureCharBuffer();
		subpass1.append("subpassword1");
		user1 = new User(salt1, pass1, 0, directoryPath + salt1 + "domains.xyt");
		domain1 = new ArrayList<DomainInfo>();
		domain1.add(new DomainInfo("google", "usey1", subpass1));
		domain1.add(new DomainInfo("bing", "usey1", subpass1));
		domain1.add(new DomainInfo("quizlet", "usey1", subpass1));
		
		// User 2 Information
		String salt2 = "user2";
		SecureCharBuffer pass2 = new SecureCharBuffer();
		pass2.append("mainpassword2");
		SecureCharBuffer subpass2 = new SecureCharBuffer();
		subpass2.append("subpassword2");
		user2 = new User(salt2, pass2, 0, directoryPath + salt2 + "domains.xyt");
		domain2 = new ArrayList<DomainInfo>();
		domain2.add(new DomainInfo("google", "useh2", subpass2));
		
		// User 3 Information
		String salt3 = "user3";
		SecureCharBuffer pass3 = new SecureCharBuffer();
		pass3.append("mainpassword3");
		SecureCharBuffer subpass3 = new SecureCharBuffer();
		subpass3.append("subpassword3");
		user3 = new User(salt3, pass3, 0, directoryPath + salt3 + "domains.xyt");
		domain3 = new ArrayList<DomainInfo>();
		domain3.add(new DomainInfo("google", "useq3", subpass3));
		domain3.add(new DomainInfo("canvas", "useq3", subpass3));
		domain3.add(new DomainInfo("facebook", "useq3", subpass3));
		domain3.add(new DomainInfo("imgur", "useq3", subpass3));
		domain3.add(new DomainInfo("duckduckgo", "useq3", subpass3));
		domain3.add(new DomainInfo("evc", "useq3", subpass3));
		domain3.add(new DomainInfo("youtube", "useq3", subpass3));
		domain3.add(new DomainInfo("codepath", "useq3", subpass3));
		domain3.add(new DomainInfo("jstor", "useq3", subpass3));
		domain3.add(new DomainInfo("history", "useq3", subpass3));
		domain3.add(new DomainInfo("steam", "useq3", subpass3));
    }
	
	// Testing the handlers raw
	
	@Test
	public void createEmptyFiles() {
		try {
			// Empty User File
			File userFile = UserHandler.createNewUserFile(new File(userFilePath));
			UserHandler uHandler = new UserHandler(userFile.getAbsolutePath());
			assertEquals(false, uHandler.checkUser(null));
			
			// Empty Domain File
			File sampleDomainFile = DomainHandler.createNewDomainFile(user1.getUsername(), user1.getMasterKey(), user1.getFileLocation().getAbsoluteFile());
			DomainHandler dHandler = new DomainHandler(sampleDomainFile.getAbsolutePath(), user1.getMasterKey(), user1.getUsername());
			assertEquals(0, dHandler.getDomains().size());
		} catch (Exception e) {
			e.printStackTrace();
			assertEquals(true, false);
		}
	}
	
    @Test
    public void addAUser() {
    	try {
    		// Create user file
    		File userFile = UserHandler.createNewUserFile(new File(userFilePath));
    		UserHandler uHandler = new UserHandler(userFile.getAbsolutePath());
    		
    		// Add a user and check that it exists
    		assertEquals(false, uHandler.checkUser(user1.getUsername()));
    		uHandler.addUser(user1.getUsername(), user1.getMasterKey(), user1.getClipboardClearTime(), user1.getFileLocation());
    		assertEquals(true, uHandler.checkUser(user1.getUsername()));
    		
    		// Make sure that the domain file is empty as well
    		User sample = uHandler.verifyUser(user1.getUsername(), user1.getMasterKey());
    		DomainHandler dHandler = new DomainHandler(sample.getFileLocation().getAbsolutePath(), sample.getMasterKey(), sample.getUsername());
    		assertEquals(0, dHandler.getDomains().size());
    	} catch (Exception e) {
    		e.printStackTrace();
    		assertEquals(true, false);
    	}
    }
    
    @Test
    public void addMultipleUsers() {
    	try {
    		// Create user file
    		File userFile = UserHandler.createNewUserFile(new File(userFilePath));
    		UserHandler uHandler = new UserHandler(userFile.getAbsolutePath());
    		
    		// Add multiple users
    		uHandler.addUser(user1.getUsername(), user1.getMasterKey(), user1.getClipboardClearTime(), user1.getFileLocation());
    		uHandler.addUser(user2.getUsername(), user2.getMasterKey(), user2.getClipboardClearTime(), user2.getFileLocation());
    		uHandler.addUser(user3.getUsername(), user3.getMasterKey(), user3.getClipboardClearTime(), user3.getFileLocation());
    		
    		assertEquals(true, uHandler.checkUser(user1.getUsername()));
    		assertEquals(true, uHandler.checkUser(user2.getUsername()));
    		assertEquals(true, uHandler.checkUser(user3.getUsername()));
    	} catch (Exception e) {
    		e.printStackTrace();
    		assertEquals(true, false);
    	}
    }
    
    @Test
    public void testBasicDomains() {
    	try {
    		// Create user file
    		File userFile = UserHandler.createNewUserFile(new File(userFilePath));
    		UserHandler uHandler = new UserHandler(userFile.getAbsolutePath());
    		
    		// Add the user and its domains
    		User sample = uHandler.addUser(user1.getUsername(), user1.getMasterKey(), user1.getClipboardClearTime(), user1.getFileLocation());
    		DomainHandler dHandler = new DomainHandler(sample.getFileLocation().getAbsolutePath(), sample.getMasterKey(), sample.getUsername());
    		dHandler.writeDomains(domain1);
    		
    		// Check that the domains can be fetched correctly
    		User check = uHandler.verifyUser(user1.getUsername(), user1.getMasterKey());
    		DomainHandler chkHandler = new DomainHandler(check.getFileLocation().getAbsolutePath(), check.getMasterKey(), check.getUsername());
    		ArrayList<DomainInfo> fthDomain = chkHandler.getDomains();
    		
    		assertEquals(domain1.size(), fthDomain.size());
    		for(int i = 0; i < domain1.size(); i++) {
    			assertEquals(domain1.get(i).getDomain(), fthDomain.get(i).getDomain());
    			assertEquals(domain1.get(i).getUsername(), fthDomain.get(i).getUsername());
    			assertEquals(domain1.get(i).getLogoPath(), fthDomain.get(i).getLogoPath());
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    		assertEquals(true, false);
    	}
    }
    
    @Test
	public void deleteAUser() {
		try {
			// Empty User File
			File userFile = UserHandler.createNewUserFile(new File(userFilePath));
			UserHandler uHandler = new UserHandler(userFile.getAbsolutePath());
			
			// Add the user and its domains
    		User sample = uHandler.addUser(user1.getUsername(), user1.getMasterKey(), user1.getClipboardClearTime(), user1.getFileLocation());
    		DomainHandler dHandler = new DomainHandler(sample.getFileLocation().getAbsolutePath(), sample.getMasterKey(), sample.getUsername());
    		dHandler.writeDomains(domain1);
    		
    		// Delete the user and check if it has been removed from the file
    		uHandler.deleteUser(user1.getUsername(), user1.getMasterKey());
    		assertEquals(false, uHandler.checkUser(user1.getUsername()));
    		
    		// Check if the domain file has been removed
    		assertEquals(false, user1.getFileLocation().exists());
    		
		} catch (Exception e) {
			e.printStackTrace();
			assertEquals(true, false);
		}
	}
    
    @Test
	public void editAUserUsername() {
		try {
			// Empty User File
			File userFile = UserHandler.createNewUserFile(new File(userFilePath));
			UserHandler uHandler = new UserHandler(userFile.getAbsolutePath());
			
			// Add the user and its domains
    		User sample = uHandler.addUser(user1.getUsername(), user1.getMasterKey(), user1.getClipboardClearTime(), user1.getFileLocation());
    		DomainHandler dHandler = new DomainHandler(sample.getFileLocation().getAbsolutePath(), sample.getMasterKey(), sample.getUsername());
    		dHandler.writeDomains(domain1);
    		
    		// Edit the user: Username ----------------------------------------------------------------------------------------------------------
    		uHandler.editUser(user1.getUsername(), user1.getMasterKey(), HandlerConstants.XMLUSER, "notuser1");
    		assertEquals(false, uHandler.checkUser(user1.getUsername()));
    		assertEquals(true, uHandler.checkUser("notuser1"));
    		
    		// Check the new domain file
    		User edit1 = uHandler.verifyUser("notuser1", user1.getMasterKey());
    		DomainHandler editHandler1 = new DomainHandler(edit1.getFileLocation().getAbsolutePath(), edit1.getMasterKey(), edit1.getUsername());
    		ArrayList<DomainInfo> editDomain1 = editHandler1.getDomains();
    		assertEquals(domain1.size(), editDomain1.size());
    		for(int i = 0; i < domain1.size(); i++) {
    			assertEquals(domain1.get(i).getDomain(), editDomain1.get(i).getDomain());
    			assertEquals(domain1.get(i).getUsername(), editDomain1.get(i).getUsername());
    			assertEquals(domain1.get(i).getLogoPath(), editDomain1.get(i).getLogoPath());
    		}
    		
    		// Edit the user: Password ----------------------------------------------------------------------------------------------------------
    		SecureCharBuffer replaceKey = new SecureCharBuffer();
    		replaceKey.append("newpassword");
    		uHandler.editUser("notuser1", user1.getMasterKey(), HandlerConstants.XMLPASS, "newpassword");
    		assertNull(uHandler.verifyUser("notuser1", user1.getMasterKey()));
    		assertNotNull(uHandler.verifyUser("notuser1", replaceKey));
    		
    		// Check the new domain file
    		User edit2 = uHandler.verifyUser("notuser1", replaceKey);
    		DomainHandler editHandler2 = new DomainHandler(edit2.getFileLocation().getAbsolutePath(), edit2.getMasterKey(), edit2.getUsername());
    		ArrayList<DomainInfo> editDomain2 = editHandler2.getDomains();
    		assertEquals(domain1.size(), editDomain2.size());
    		for(int i = 0; i < domain1.size(); i++) {
    			assertEquals(domain1.get(i).getDomain(), editDomain2.get(i).getDomain());
    			assertEquals(domain1.get(i).getUsername(), editDomain2.get(i).getUsername());
    			assertEquals(domain1.get(i).getLogoPath(), editDomain2.get(i).getLogoPath());
    		}
    		
    		// Edit the user: Filepath ----------------------------------------------------------------------------------------------------------
    		// Note: Since clipboard isn't used in encryption/decryption, we don't test it, since if the others work, it works as well
    		uHandler.editUser("notuser1", replaceKey, HandlerConstants.XMLPATH, directoryPath + "remainDomain.xyt");
    		User edit3 = uHandler.verifyUser("notuser1", replaceKey);
    		DomainHandler editHandler3 = new DomainHandler(edit3.getFileLocation().getAbsolutePath(), edit3.getMasterKey(), edit3.getUsername());
    		ArrayList<DomainInfo> editDomain3 = editHandler3.getDomains();
    		assertEquals(domain1.size(), editDomain3.size());
    		for(int i = 0; i < domain1.size(); i++) {
    			assertEquals(domain1.get(i).getDomain(), editDomain3.get(i).getDomain());
    			assertEquals(domain1.get(i).getUsername(), editDomain3.get(i).getUsername());
    			assertEquals(domain1.get(i).getLogoPath(), editDomain3.get(i).getLogoPath());
    		}
    		
		} catch (Exception e) {
			e.printStackTrace();
			assertEquals(true, false);
		}
	}
}
