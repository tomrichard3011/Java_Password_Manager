package edu.sjsu.Team15;

import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import io.github.novacrypto.SecureCharBuffer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
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
	public boolean writeDomains(ArrayList<DomainInfo> domains) {
		// Prepare file to write
		Document doc;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
			doc = dbBuilder.parse(database);
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
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        DOMSource source = new DOMSource(doc);
	        
	        // I have no idea if this overwrites, I guess we'll find out the hard way
	        StreamResult file = new StreamResult(database);
	        transformer.transform(source, file);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		// Encrypt the file: Warning, this will destroy the original file
		if(encrypt(master, salt) == null) {
			return false;
		}
		return true;
	}
	
	// TODO: Create a new domain file
	// Static: Create a new domain file
	public static File createNewDomainFile() {
		
		return null;
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
