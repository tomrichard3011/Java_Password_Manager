package edu.sjsu.Team15;

import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import io.github.novacrypto.SecureCharBuffer;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

// Sources: File Read: https://mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/

public class UserHandler extends DatabaseHandler {
	private SecureCharBuffer master;
	
	// Constructor ------------------------------------------------------------
	public UserHandler(String filepath, SecureCharBuffer key) throws IOException {
		super(new File(filepath), File.createTempFile("sjsu", ".xml"));
		master = key;
	}
	
	/**
	 * Attempt to get the user and create it from the file
	 * Note: Several exceptions are being thrown, use function with caution
	 * @return The user object, if found, null otherwise
	 */
	public ArrayList<User> getUsers(String salt) throws Exception {
		// Decrypt the file
		File active = decrypt(master, salt);
		// Set up structures to get users
		ArrayList<User> users = new ArrayList<>();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(active);
		// Get users
		NodeList userList = doc.getElementsByTagName("user");
		for(int i = 0; i < userList.getLength(); i++) {
			Node current = userList.item(i);
			if(current.getNodeType() == Node.ELEMENT_NODE) {
				Element ele = (Element) current;
				users.add(new User(ele.getAttribute("name"),
						ele.getAttribute("pass").toCharArray(),
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
}
