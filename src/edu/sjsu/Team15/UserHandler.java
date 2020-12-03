package edu.sjsu.Team15;

import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.github.novacrypto.SecureCharBuffer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

/**
 * Currently not working: Do not attempt to run, finalized function declarations will be different
 * Look to DomainHandler for a more complete class
 * @author Nicolas Guerrero
 */

// Sources: File Read: https://mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
// XPath Parser: https://www.baeldung.com/java-xpath

public class UserHandler extends DatabaseHandler {
	// Constant: Perhaps move all constants to a single file to avoid losing track
	private File database;
	private SecureCharBuffer master;
	private String salt;
	
	// Constructor ------------------------------------------------------------
	public UserHandler(String filepath, SecureCharBuffer key, String salt) throws IOException {
		super(new File(filepath), File.createTempFile("sjsu", ".xml"));
		this.master = key;
		this.salt = salt;
		database = null;
		//database = decrypt(key, salt);
	}
	
	/**
	 * Check if the credentials match in the user file, return the user object if found
	 * Using the xml structure leads to security issues, but it uses XPath to allow
	 * for straightforward queries of finding elements. If found, it will then automatically
	 * prepare the DomainInfo ArrayList and add it to the User object. If errors occur, then
	 * null values will be passed, so always check if the value is null
	 * @param username The username for credential, and salt for file decryption
	 * @param password The password for credential, and buffer for file decryption
	 * @return The User object
	 */
	public User verifyUser(String username, SecureCharBuffer password) {
		// Prepare the document to be parsed
		try {
			database = decrypt(master, salt);
			
			// Debugging: Reading the file from temp directory
			Path path = Paths.get(database.getAbsolutePath());
			String template = Files.readString(path);
			System.out.println("------------------");
			System.out.println(template);
			System.out.println("------------------");
			
			FileInputStream stream = new FileInputStream(database);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
			Document userXML = dbBuilder.parse(stream);
			XPath xpath = XPathFactory.newInstance().newXPath();
			
			// Check for the correct username and password combination
			CharSequence loosePass = password.toStringAble();
			// String query = "/Users/User[@name='" + username + "' and pass='" + loosePass + "']";
			String query = "/Users/user[name='" + username + "' and pass='" + loosePass + "']";
			Node node = (Node) xpath.compile(query).evaluate(userXML, XPathConstants.NODE);
			stream.close();
			
			if(node != null && node.hasChildNodes()) {
				// Username is 0, pass is 1, clipboard is 2, filepath is 3 getNodeValue()
				NodeList elements = node.getChildNodes();
				
				// Read through the node list TODO
				System.out.println("----------------------");
				for(int i = 0; i < elements.getLength(); i++) {
					System.out.print(i);
					System.out.print(" :");
					System.out.print(elements.item(i).getNodeName());
					System.out.print(" :");
					System.out.print(elements.item(i).getNodeValue());
					System.out.print(" :");
					System.out.println(elements.item(i).getTextContent());
					
					if(elements.item(i).getTextContent().contains("\n")) {
						System.out.print("Unusual behavior, but understandable");
						System.out.println(elements.item(i).getTextContent().length());
					}
				}
				System.out.println("-------------------------");
				
				String fileLoc = elements.item(3).getTextContent();
				User currentUser = new User(username, password, Integer.parseInt(elements.item(2).getTextContent()), elements.item(3).getTextContent());
				DomainHandler domainHandler = new DomainHandler(fileLoc, currentUser.getMasterKey(), currentUser.getUsername());
				currentUser.setDomainInfo(domainHandler.getDomains());
				return currentUser;
			} else {
				// Node not found
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Checks if the username is already present in the user database file
	 * Used to ensure no duplicates on the database file
	 * @param username The username, must be exact
	 * @return Whether the username is taken or not
	 */
	public boolean checkUser(String username) {
		FileInputStream stream;
		Boolean found = false;
		try {
			database = decrypt(master, salt);
			stream = new FileInputStream(database);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
			Document userXML = dbBuilder.parse(stream);
			XPath xpath = XPathFactory.newInstance().newXPath();
			
			// Search for the username and return if it's found or not
			String query = "/Users/user[name='" + username + "']";
			found = (Boolean) xpath.evaluate(query, userXML, XPathConstants.BOOLEAN);
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return found.booleanValue();
	}
	
	/**
	 * Add a user to the user database file
	 * You MUST check if the user is in the file, this function doesn't check and won't check.
	 * @param username The username for the new user
	 * @param password The password being written (MUST be hashed beforehand)
	 * @param clipboardTime The time for the clipboard
	 * @param secureFile Where the encrypted file will be saved
	 * @return The User object, if successfully added. null otherwise.
	 */
	public User addUser(String username, SecureCharBuffer password, int clipboardTime, File secureFile) {
		try {
			// Prepare the document to be parsed
			database = decrypt(master, salt);
			FileInputStream stream = new FileInputStream(database);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
			Document userXML = dbBuilder.parse(stream);
			XPath xpath = XPathFactory.newInstance().newXPath();
			
			// Retrieve the root node and append DEFAULT_CLIPBOARD_TIME
			String query = "/Users";
			CharSequence loosePass = password.toStringAble();
			Node root = (Node) xpath.evaluate(query, userXML, XPathConstants.NODE);
			Node newUser = createUserNode(userXML, username, loosePass.toString(), clipboardTime, secureFile.getAbsolutePath());
			root.appendChild(newUser);
			stream.close();
			
			// Rewrite a new user file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        //transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty(OutputKeys.INDENT, "no");
	        transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
	        DOMSource source = new DOMSource(userXML);
	        StreamResult file = new StreamResult(database);
	        transformer.transform(source, file);
			
	        // Encrypt the file to the correct location
	        setActiveFile(database.getAbsolutePath());
	        
			if(encrypt(master, salt) == null) {
				return null;
			} else {
				// Build the user object
				User currentUser = new User(clipboardTime, new ArrayList<DomainInfo>(), username, password);
				secureFile = DomainHandler.createNewDomainFile(username, password, secureFile);
				currentUser.setFileLocation(secureFile);
				// Build domain file
				
				return currentUser;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// Delete user TODO: If requested

	/**
	 * Create a encrypted file for the database of users
	 * @param username The salt used to encrypt the file
	 * @param password The buffer used to encrypt the file
	 * @param secureFileLocation The location where the encrypted file is saved
	 * @return The file location
	 */
	public static File createNewUserFile(String username, SecureCharBuffer password, File secureFileLocation) {
		// Prepare file to write
		Document doc;
		try {
			DocumentBuilderFactory domainFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder domainBuilder = domainFactory.newDocumentBuilder();
			doc = domainBuilder.newDocument();
			Element root = doc.createElementNS("WeCouldAddAHashHereThatVerifiesFile", "Users");
			doc.appendChild(root);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		// Write to the DOM object
		Transformer transformer;
		DOMSource source;
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        transformer = transformerFactory.newTransformer();
	        //transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty(OutputKeys.INDENT, "no");
	        transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
	        source = new DOMSource(doc);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
        
        // Write to temporary file
		File temporaryFile;
		try {
	        temporaryFile = File.createTempFile("sjsu", ".xml");
	        StreamResult file = new StreamResult(temporaryFile);
	        transformer.transform(source, file);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
        // Encrypt to the provided location
		try {
	        byte[] fileBytes = Files.readAllBytes(temporaryFile.toPath());
	        byte[] encryptedBytes = CryptoUtil.encrypt(password, username, fileBytes);
	        FileOutputStream stream = new FileOutputStream(secureFileLocation.getAbsoluteFile(), false);
			stream.write(encryptedBytes);
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return secureFileLocation;
	}
	
	private Node createUserNode(Document doc, String username, String passhash, int clipTime, String filepath) {
		Element user = doc.createElement("user");
		user.appendChild(createUserField(doc, "name", username));
		user.appendChild(createUserField(doc, "pass", passhash));
		user.appendChild(createUserField(doc, "clip", Integer.toString(clipTime)));
		user.appendChild(createUserField(doc, "fp", filepath));
		return user;
	}
	
	private Node createUserField(Document doc, String name, String value) {
		Element field = doc.createElement(name);
		field.appendChild(doc.createTextNode(value));
		return field;
	}
}
