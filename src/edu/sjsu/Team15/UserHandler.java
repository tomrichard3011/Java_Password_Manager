package edu.sjsu.Team15;

import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import io.github.novacrypto.SecureCharBuffer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

// Sources: File Read: https://mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
// XPath Parser: https://www.baeldung.com/java-xpath

public class UserHandler extends DatabaseHandler {
	private File database;
	private SecureCharBuffer master;
	private String salt;
	
	// Constructor ------------------------------------------------------------
	public UserHandler(String filepath, SecureCharBuffer key, String salt) throws IOException {
		super(new File(filepath), File.createTempFile("sjsu", ".xml"));
		database = decrypt(key, salt);
	}
	
	// Check for username and password
	public User verifyUser(String username, SecureCharBuffer password) throws XPathExpressionException {
		// Prepare the document to be parsed
		FileInputStream stream = new FileInputStream(database);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
		Document userXML = dbBuilder.parse(stream);
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		// Check for the correct username and password combination
		CharSequence loosePass = password.toStringAble();
		String query = "/Users/User[@name='" + username + "' and pass='" + loosePass + "']";
		Node node = (Node) xpath.compile(query).evaluate(userXML, XPathConstants.NODE);
		stream.close();
		
		if(node != null && node.hasChildNodes()) {
			// Username is attribute of user, pass is 0, clipboard is 1, filepath is 2
			NodeList elements = node.getChildNodes();
			String fileLoc = elements.item(2).getNodeValue();
			User currentUser = new User(username, password, Integer.parseInt(elements.item(1).getNodeValue()));
			// DatabaseHandler dbHandler = new DatabaseHandler(new File(filepath), File.createTempFile("sjsu", ".xml"), password, username);
			// return dbHandler.getDomains();
		} else {
			// Node not found
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
		FileInputStream stream = new FileInputStream(database);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
		Document userXML = dbBuilder.parse(stream);
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		// Search for the username and return if it's found or not
		String query = "/Users/User[@name='" + username + "']";
		Boolean found = (Boolean) xpath.evaluate(query, userXML, XPathConstants.BOOLEAN);
		stream.close();
		return found.booleanValue();
	}
	
	/**
	 * Attempt to get the user and create it from the file
	 * Note: Several exceptions are being thrown, use function with caution
	 * @return The user object, if found, null otherwise
	 */
	public ArrayList<User> getUsers(String salt) throws Exception {
		// Set up structures to get users
		ArrayList<User> users = new ArrayList<>();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(database);
		// Get users
		NodeList userList = doc.getElementsByTagName("user");
		for(int i = 0; i < userList.getLength(); i++) {
			Node current = userList.item(i);
			if(current.getNodeType() == Node.ELEMENT_NODE) {
				Element ele = (Element) current;
				users.add(new User(ele.getAttribute("name"),
						ele.getAttribute("pass"),
						Integer.parseInt(ele.getAttribute("clip")),
						ele.getAttribute("fp")));
			}
		}
		return users;
	}
	
	public Boolean updateUsers(ArrayList<User> users) throws Exception {
		// Build a new file from the provided user ArrayList
		DocumentBuilderFactory userFactory;
		DocumentBuilder dBuilder;
		Document doc;
		Element root;
		
		// Check if file writing is possible
		try {
			userFactory = DocumentBuilderFactory.newInstance();
			dBuilder = userFactory.newDocumentBuilder();
			doc = dBuilder.newDocument();
			root = doc.createElementNS("WeCouldAddAHashHereThatVerifiesFile", "Users");
			doc.appendChild(root);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		// TODO: Add entries
		for(int i = 0; i < users.size(); i++) {
			root.appendChild(null);
		}
		
		return true;
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
	
	private void getSecurePassword() {
		
	}
}
