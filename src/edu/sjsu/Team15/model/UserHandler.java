package edu.sjsu.Team15.model;

import java.util.ArrayList;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import edu.sjsu.Team15.utility.HandlerConstants;
import edu.sjsu.Team15.utility.CryptoUtil;
import io.github.novacrypto.SecureCharBuffer;
import javax.xml.transform.Transformer;
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
 * File Handler for user files
 * 
 * Class is designed to edit to existing user file, which can be created by a static method
 * available in this class.
 * 
 * Note: Previous iterations had master and salt variables set, before universal encryption
 * and decryption methods were available. Both are now unused, but could be re-implemented
 * quickly if there's a use. For now, leave both as null and just use the 1-arg constructor.
 * 
 * 
 * @author Nicolas Guerrero
 * Sources: File Read: https://mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
 * XPath Parser: https://www.baeldung.com/java-xpath
 */
public class UserHandler extends DatabaseHandler {
	/** The file location of the decrypted file */
	private File database;
	/** The buffer used for encryption (Unused, soon to be deprecated) */
	private SecureCharBuffer master;
	/** The salt used for encryption (Unused, soon to be deprecated) */
	private String salt;
	
	// Constructors ------------------------------------------------------------	
	/**
	 * Create a handler using the default universal encryption/decryption
	 * @param filepath The file location for the encrypted file
	 * @throws IOException
	 */
	public UserHandler(String filepath) throws IOException {
		super(new File(filepath), File.createTempFile("sjsu", ".xml"));
		this.master = null;
		this.salt = null;
		database = null;
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
		try {
			// Decrypt the file and parse the file
			database = decrypt(master, salt);
			Document userXML = buildDoc(database);
			XPath xpath = XPathFactory.newInstance().newXPath();
			
			// Check for the correct username and password combination
			CharSequence loosePass = password.toStringAble();
			String query = "/Users/user[name='" + username + "' and pass='" + loosePass + "']";
			Node node = (Node) xpath.compile(query).evaluate(userXML, XPathConstants.NODE);
			
			if(node != null && node.hasChildNodes()) {
				// Build the user object
				NodeList elements = node.getChildNodes();
				String fileLoc = elements.item(HandlerConstants.XMLPATH).getTextContent();
				User currentUser = new User(username, password, Integer.parseInt(elements.item(HandlerConstants.XMLCLIP).getTextContent()), fileLoc);
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
		} finally {
			if(database != null && database.exists()) {
				database.delete();
			}
		}
	}
	
	/**
	 * Checks if the username is already present in the user database file
	 * Used to ensure no duplicates on the database file.
	 * NOTE: If somehow username is null, then it will check if anything exists
	 * in the file at all. Returns true if something is found, false if there's
	 * nothing. Used primarily in test cases, but can work as an additional tool,
	 * since null should never be run through here normally.
	 * @param username The username, must be exact
	 * @return Whether the username is taken or not
	 */
	public boolean checkUser(String username) {
		try {
			// Decrypt the file
			database = decrypt(null, null);
			Document userXML = buildDoc(database);
			XPath xpath = XPathFactory.newInstance().newXPath();
			
			// Evaulate the query
			String query;
			if(username != null) {
				// Search for the username and return if it's found or not
				query = "/Users/user[name='" + username + "']";
			} else {
				// Check if there's anything in the file
				query = "/Users/user[0]";
			}
			Boolean found = (Boolean) xpath.evaluate(query, userXML, XPathConstants.BOOLEAN);
			return found.booleanValue();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if(database != null && database.exists()) {
				database.delete();
			}
		}
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
			Document userXML = buildDoc(database);
			XPath xpath = XPathFactory.newInstance().newXPath();
			
			// Retrieve the root node and append DEFAULT_CLIPBOARD_TIME
			String query = "/Users";
			CharSequence loosePass = password.toStringAble();
			Node root = (Node) xpath.evaluate(query, userXML, XPathConstants.NODE);
			Node newUser = createUserNode(userXML, username, loosePass.toString(), clipboardTime, secureFile.getAbsolutePath());
			root.appendChild(newUser);
			
			// Rewrite a new user file
	        Transformer transformer = buildTransformer();
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
				return currentUser;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(database != null && database.exists()) {
				database.delete();
			}
		}
	}

	/**
	 * Edit the user on the user file, and fix the domain file from the change in user credentials
	 * @param username The salt used in encryption
	 * @param password The char buffer used in encryption
	 * @param name The HandlerConstant of the element that needs to change
	 * @param value The value that will be replacing the original file content
	 * @return Whether the edit was successful or not
	 */
	public Boolean editUser(String username, SecureCharBuffer password, int name, String value) {
		try {
			// Decrypt the file
			database = decrypt(master, salt);			
			Document userXML = buildDoc(database);
			XPath xpath = XPathFactory.newInstance().newXPath();
			
			// Check for the correct username and password combination
			CharSequence loosePass = password.toStringAble();
			String query = "/Users/user[name='" + username + "' and pass='" + loosePass + "']";
			Node node = (Node) xpath.compile(query).evaluate(userXML, XPathConstants.NODE);
			
			if(node != null && node.hasChildNodes()) {
				// Prepare to fix the domain file (Change user, pass, or file location)
				NodeList elements = node.getChildNodes();
				SecureCharBuffer sec = new SecureCharBuffer();
				sec.append(elements.item(HandlerConstants.XMLPASS).getTextContent());
				DomainHandler originDomain = new DomainHandler(elements.item(HandlerConstants.XMLPATH).getTextContent(),
						sec, elements.item(HandlerConstants.XMLUSER).getTextContent());
				ArrayList<DomainInfo> tempList = originDomain.getDomains();
				
				// Replace the element
				elements.item(name).setTextContent(value);
				
				// Rewrite the domain file
				sec = new SecureCharBuffer();
				sec.append(elements.item(HandlerConstants.XMLPASS).getTextContent());
				DomainHandler replaceDomain = new DomainHandler(elements.item(HandlerConstants.XMLPATH).getTextContent(),
						sec, elements.item(HandlerConstants.XMLUSER).getTextContent());
				replaceDomain.writeDomains(tempList);
				
				// Rewrite the xml to the temp directory
		        Transformer transformer = buildTransformer();
		        DOMSource source = new DOMSource(userXML);
		        StreamResult file = new StreamResult(database);
		        transformer.transform(source, file);
				
		        // Encrypt the file to the correct location
		        setActiveFile(database.getAbsolutePath());
		        if(encrypt(master, salt) == null) {
					return false;
				} else {
					return true;
				}    
			} else {
				// Node not found
				return false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(database != null && database.exists()) {
				database.delete();
			}
		}
	}
	
	/**
	 * Delete the user with the matching credentials
	 * @param username The username we're searching for
	 * @param password The password for verifying that the request came from the user 
	 * @return true if the user is properly deleted, false otherwise
	 */
	public Boolean deleteUser(String username, SecureCharBuffer password) {
		// Prepare the document to be parsed
		try {
			// Decrypt the file
			database = decrypt(master, salt);			
			Document userXML = buildDoc(database);
			XPath xpath = XPathFactory.newInstance().newXPath();
			
			// Check for the correct username and password combination
			CharSequence loosePass = password.toStringAble();
			String query = "/Users/user[name='" + username + "' and pass='" + loosePass + "']";
			Node node = (Node) xpath.compile(query).evaluate(userXML, XPathConstants.NODE);
			
			if(node != null && node.hasChildNodes()) {
				// Delete the user from the internal node list
				NodeList elements = node.getChildNodes();
				String originDomain = elements.item(HandlerConstants.XMLPATH).getTextContent();
				node.getParentNode().removeChild(node);
				
				// Delete the associated domain file
				File origin = new File(originDomain);
				origin.delete();
				
				// Rewrite the xml to the temp directory		        
		        Transformer transformer = buildTransformer();
		        DOMSource source = new DOMSource(userXML);
		        StreamResult file = new StreamResult(database);
		        transformer.transform(source, file);
				
		        // Encrypt the file to the correct location
		        setActiveFile(database.getAbsolutePath());
		        if(encrypt(master, salt) == null) {
					return false;
				} else {
					return true;
				}
			} else {
				// Node not found
				return false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(database != null && database.exists()) {
				database.delete();
			}
		}
	}

	/**
	 * Create a encrypted file for the database of users
	 * @param secureFileLocation The location where the encrypted file is saved
	 * @return The file location
	 */
	public static File createNewUserFile(File secureFileLocation) {
		File temporaryFile = null;
		try {
			// Prepare file to write
			Document doc = buildDoc(null);
			Element root = doc.createElementNS("WeCouldAddAHashHereThatVerifiesFile", "Users");
			doc.appendChild(root);
			
			// Write to the DOM object
			Transformer transformer = buildTransformer();
			DOMSource source = new DOMSource(doc);
			
	        // Write to temporary file
	        temporaryFile = File.createTempFile("sjsu", ".xml");
	        StreamResult file = new StreamResult(temporaryFile);
	        transformer.transform(source, file);
	        
	        // Encrypt to the provided location
	        byte[] fileBytes = Files.readAllBytes(temporaryFile.toPath());
	        byte[] encryptedBytes = CryptoUtil.universalEncrypt(fileBytes);
	        FileOutputStream stream = new FileOutputStream(secureFileLocation.getAbsoluteFile(), false);
			stream.write(encryptedBytes);
			stream.close();
			
			return secureFileLocation;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(temporaryFile != null && temporaryFile.exists()) {
				temporaryFile.delete();
			}
			
		}
	}
	
	/**
	 * Create a user node to append to the root node
	 * @param doc The document used to create the node
	 * @param username The username for the node
	 * @param passhash The password for the node
	 * @param clipTime The clipTime for the node
	 * @param filepath The filepath where the domain file is
	 * @return Node The Node object for storing the user info the user file
	 */
	private Node createUserNode(Document doc, String username, String passhash, int clipTime, String filepath) {
		Element user = doc.createElement("user");
		user.appendChild(createUserField(doc, "name", username));
		user.appendChild(createUserField(doc, "pass", passhash));
		user.appendChild(createUserField(doc, "clip", Integer.toString(clipTime)));
		user.appendChild(createUserField(doc, "fp", filepath));
		return user;
	}
	
	/**
	 * Create an element node to append to the user node
	 * @param doc The document object for creating the node
	 * @param name The field name
	 * @param value The field value
	 * @return
	 */
	private Node createUserField(Document doc, String name, String value) {
		Element field = doc.createElement(name);
		field.appendChild(doc.createTextNode(value));
		return field;
	}
}
