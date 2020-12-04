package edu.sjsu.Team15;

import java.util.ArrayList;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import io.github.novacrypto.SecureCharBuffer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class DomainHandler extends DatabaseHandler {
	private File database;
	private SecureCharBuffer master;
	private String salt;
	
	// Constructor --------------------------------------------------------
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
		// Prepare the xml file
		database = decrypt(master, salt);
		Document doc;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
			doc = dbBuilder.parse(database);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		// Build the list of domains
		ArrayList<DomainInfo> allDomains = new ArrayList<DomainInfo>();
		NodeList domainList = doc.getElementsByTagName("domain");
		NodeList fields;
		for(int i = 0; i < domainList.getLength(); i++) {
			Node current = domainList.item(i);
			fields = current.getChildNodes();
			allDomains.add(new DomainInfo(
					fields.item(0).getTextContent(),
					fields.item(1).getTextContent(),
					convertToBuffer(fields.item(2).getTextContent())));
		}
		return allDomains;
	}
	
	// Write all domains
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
		// Prepare file to write
		Document doc = null;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
			//doc = dbBuilder.parse(database);
			doc = dbBuilder.newDocument();
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		// Prepare DOM object to replace
		Element root = doc.createElementNS("WeCouldAddAHashHereThatVerifiesFile", "domains");
		doc.appendChild(root);
		for(int i = 0; i < domains.size(); i++) {
			root.appendChild(domainToNode(doc, domains.get(i)));
		}
		
		// Write to file location
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        transformer.setOutputProperty(OutputKeys.INDENT, "no");
	        transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
	        DOMSource source = new DOMSource(doc);
	        
	        database = File.createTempFile("sjsu", ".xml");
	        StreamResult file = new StreamResult(database);
	        transformer.transform(source, file);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		// Encrypt the file: Warning, this will destroy the original file
		// Set active file, then encrypt that file to the right location
		setActiveFile(database.getAbsolutePath());
		if(encrypt(master, salt) == null) {
			return false;
		}
		return true;
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
		// Prepare file to write
		Document doc;
		try {
			DocumentBuilderFactory domainFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder domainBuilder = domainFactory.newDocumentBuilder();
			doc = domainBuilder.newDocument();
			Element root = doc.createElementNS("WeCouldAddAHashHereThatVerifiesFile", "domains");
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
	
	private SecureCharBuffer convertToBuffer(String hash) {
		SecureCharBuffer buffer = new SecureCharBuffer();
		buffer.append(hash);
		return buffer;
	}
	
	private String convertToString(SecureCharBuffer buffer) {
		CharSequence loosePass = buffer.toStringAble();
		return loosePass.toString();
	}
	
	private Node domainToNode(Document doc, DomainInfo domain) {
		Element current = doc.createElement("domain");
		current.appendChild(formElement(doc, "site", domain.getDomain()));
		current.appendChild(formElement(doc, "user", domain.getUsername()));
		current.appendChild(formElement(doc, "pass", convertToString(domain.getPassword())));
		return current;
	}
	
	private Node formElement(Document doc, String name, String value) {
		Element node = doc.createElement(name);
		node.appendChild(doc.createTextNode(value));
		return node;
	}

}
