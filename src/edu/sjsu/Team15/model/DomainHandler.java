package edu.sjsu.Team15.model;

import java.util.ArrayList;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import edu.sjsu.Team15.utility.CryptoUtil;
import edu.sjsu.Team15.utility.HandlerConstants;
import io.github.novacrypto.SecureCharBuffer;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

/**
 * File Handler for lists of domains
 * Class is designed around reading and writing full list of domains. Unlike
 * UserHandler, all edits must occur on the DomainInfo objects themselves, this
 * is only for getting and securely saving files.
 * 
 * Note: Be careful if you edit the user object, as another DomainHandler must be built
 * to work with the new file.
 * 
 * @author Nicolas Guerrero
 * Sources: Mostly derived from UserHandler
 */
public class DomainHandler extends DatabaseHandler {
	/** The file location of the decrypted file */
	private File database;
	/** The buffer used for encryption */
	private SecureCharBuffer master;
	/** The salt used for encryption */
	private String salt;

	/**
	 * Create a DomainHandler
	 * Note: If you ever edit the user in the user file, create a new DomainHandler. It won't read
	 * the correct credentials otherwise and will fail.
	 * @param filepath The secure file location
	 * @param key User's master key
	 * @param salt User's username
	 * @throws IOException
	 */
	public DomainHandler(String filepath, SecureCharBuffer key, String salt) throws IOException {
		super(new File(filepath), File.createTempFile("sjsu", ".xml"));
		this.master = key;
		this.salt = salt;
		database = null;
	}
	
	/**
	 * Get all the domains from the provided file 
	 * Note: If the file is empty, an empty ArrayList is returned. However, if an error occurs,
	 * null is returned. Check for null in case an unexpected error occurs.
	 * @return The ArrayList of all the domains
	 */
	public ArrayList<DomainInfo> getDomains() {
		try {
			// Prepare the xml file
			database = decrypt(master, salt);
			Document doc = buildDoc(database);
			
			// Build the list of domains
			ArrayList<DomainInfo> allDomains = new ArrayList<DomainInfo>();
			NodeList domainList = doc.getElementsByTagName("domain");
			NodeList fields;
			for(int i = 0; i < domainList.getLength(); i++) {
				Node current = domainList.item(i);
				fields = current.getChildNodes();
				allDomains.add(new DomainInfo(
						fields.item(HandlerConstants.DOMAINNAME).getTextContent(),
						fields.item(HandlerConstants.DOMAINUSER).getTextContent(),
						convertToBuffer(fields.item(HandlerConstants.DOMAINPASS).getTextContent())));
			}
			return allDomains;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(database != null && database.exists()) {
				database.delete();
			}
		}
	}
	
	/**
	 * Write the domain ArrayList to the domain file personal to the user
	 * The function creates a new Document, and translates the DOM object to the
	 * file. After saving the file to a temporary directory, it is then encrypted
	 * to the correct secure filepath location (provided in the constructor).
	 * DomainHandler requires username, password, and filepath, which is how the
	 * file is correctly written for the user.
	 * @param domains The ArrayList of domains
	 * @return Whether the process was successfully completed
	 */
	public boolean writeDomains(ArrayList<DomainInfo> domains) {
		try {
			// Prepare DOM object to replace
			Document doc = buildDoc(null);
			Element root = doc.createElementNS("WeCouldAddAHashHereThatVerifiesFile", "domains");
			doc.appendChild(root);
			for(int i = 0; i < domains.size(); i++) {
				root.appendChild(domainToNode(doc, domains.get(i)));
			}
			
			// Write to file location
	        DOMSource source = new DOMSource(doc);
	        database = File.createTempFile("sjsu", ".xml");
	        StreamResult file = new StreamResult(database);
	        Transformer transformer = buildTransformer();
	        transformer.transform(source, file);
	        
	        // Encrypt the file: Warning, this will destroy the original file
			// Set active file, then encrypt that file to the right location
			setActiveFile(database.getAbsolutePath());
			if(encrypt(master, salt) == null) {
				return false;
			}
			return true;
	        
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if(database != null && database.exists()) {
				database.delete();
			}
		}
	}
	
	/**
	 * Create a new domain database for a new user
	 * Generate one from scratch. Since the user doesn't exist, a static method needs to build one
	 * from scratch without the provided abstract methods. If there's time, I'll rewrite it to better
	 * implement the superclass.
	 * @param username The salt used in encryption
	 * @param password The buffer used to encrypt the file
	 * @param secureFileLocation The final location of the secure file location
	 * @return
	 */
	public static File createNewDomainFile(String username, SecureCharBuffer password, File secureFileLocation) {
		File temporaryFile = null;
		try {
			// Prepare file to write
			Document doc;
			doc = buildDoc(null);
			Element root = doc.createElementNS("WeCouldAddAHashHereThatVerifiesFile", "domains");
			doc.appendChild(root);
			
	        // Write to the temporary file
	        temporaryFile = File.createTempFile("sjsu", ".xml");
	        StreamResult file = new StreamResult(temporaryFile);
 			Transformer transformer = buildTransformer();
 	        DOMSource source = new DOMSource(doc);
	        transformer.transform(source, file);
			
	        // Encrypt to the provided location
	        byte[] fileBytes = Files.readAllBytes(temporaryFile.toPath());
	        byte[] encryptedBytes = CryptoUtil.encrypt(password, username, fileBytes);
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
	 * Converts a String to a SecureCharBuffer
	 * Used when fetching the password from a file
	 * @param hash The password to be converted
	 * @return SecureCharBuffer The buffer where the password is stored
	 */
	private SecureCharBuffer convertToBuffer(String hash) {
		SecureCharBuffer buffer = new SecureCharBuffer();
		buffer.append(hash);
		return buffer;
	}
	
	/**
	 * Converts a SecureCharBuffer to a String
	 * Used when storing a password in the xml file
	 * @param buffer The buffer where the password is stored
	 * @return String The string representation of the buffer
	 */
	private String convertToString(SecureCharBuffer buffer) {
		CharSequence loosePass = buffer.toStringAble();
		return loosePass.toString();
	}
	
	/**
	 * Converts a Domain object into a xml node that can append to the root node
	 * @param doc A document object that can create elements
	 * @param domain The domain being converted
	 * @return Node The Node representation of the domain
	 */
	private Node domainToNode(Document doc, DomainInfo domain) {
		Element current = doc.createElement("domain");
		current.appendChild(formElement(doc, "site", domain.getDomain()));
		current.appendChild(formElement(doc, "user", domain.getUsername()));
		current.appendChild(formElement(doc, "pass", convertToString(domain.getPassword())));
		return current;
	}
	
	/**
	 * Creates a Node containing the field from the domain object
	 * @param doc A document object that creates elements
	 * @param name The name of the tag (site, user, pass)
	 * @param value The value being stored in the domain
	 * @return Node The Node representation of a field in DomianInfo
	 */
	private Node formElement(Document doc, String name, String value) {
		Element node = doc.createElement(name);
		node.appendChild(doc.createTextNode(value));
		return node;
	}
}
