package edu.sjsu.Team15.model;

import edu.sjsu.Team15.utility.CryptoUtil;
import io.github.novacrypto.SecureCharBuffer;
import java.nio.file.Files;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

import org.w3c.dom.Document;

import java.io.*;

/**
 * Standard File Handler
 * Offers standard methods for encrypting and decrypting, handling the decrypted file,
 * and having a few additional methods for working with java xml objects.
 * 
 * Notes: It can't determine if the file is safe or formatted correctly, so the function
 * relies on the program for it to be used safely and properly. With more time, a
 * verification function can be added, but for the time being, use these classes with
 * caution.
 * 
 * @author Nicolas Guerrero
 * Sources: https://mkyong.com/java/how-to-convert-file-into-an-array-of-bytes/
 * https://docs.oracle.com/javase/7/docs/api/java/io/File.html
 * https://stackoverflow.com/questions/4350084/byte-to-file-in-java
 */
public abstract class DatabaseHandler {
	/** The file location of the encrypted xml file. Usually ends with .xyt extension */
	private File secureFile;
	/** The file location of the decrypted xml file. Should point in the temporary directory */
	private File activeFile;
	
	/**
	 * Create a DatabaseHandler Instance.
	 * Both file locations must be provided and are used for encrypted and
	 * decrypted file locations.
	 * 
	 * @param dataFile The database file location
	 * @param tempFile The file location for the decrypted file
	 */
	public DatabaseHandler(File dataFile, File tempFile) {
		secureFile = dataFile;
		activeFile = tempFile;
		activeFile.deleteOnExit();
	}
	
	/**
	 * Encrypt the active file and save it to the secure file location
	 * @param sk The key used for encryption
	 * @param username The salt used for encryption
	 * @return File The location where the secure file was saved
	 */
	public File encrypt(SecureCharBuffer sk, String username) {
		try {
			
			// Transform decrypted file to encrypted byte array
			byte[] fileBytes = Files.readAllBytes(activeFile.toPath());
			byte[] encryptedBytes;
			if(sk != null && username != null) {
				encryptedBytes = CryptoUtil.encrypt(sk, username, fileBytes);
			} else {
				encryptedBytes = CryptoUtil.universalEncrypt(fileBytes);
			}
			// Save the encrypted file
			FileOutputStream stream = new FileOutputStream(secureFile.getAbsoluteFile(), false);
			stream.write(encryptedBytes);
			stream.close();
			// Delete the decrypted file
			activeFile.delete();
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if(activeFile.exists()) {
				activeFile.delete();
			}
		}
		return secureFile;
	}
	
	/**
	 * Decrypt the secure file and save it to the active file location
	 * @param sk The key used for decryption
	 * @param username The salt used for decryption
	 * @return File The location where the active file wa saved
	 */
	public File decrypt(SecureCharBuffer sk, String username) {
		try {
			// Transform encrypted file to unencrypted byte array
			byte[] fileBytes = Files.readAllBytes(secureFile.toPath());
			byte[] unencryptedBytes;
			if(sk != null && username != null) {
				unencryptedBytes = CryptoUtil.decrypt(sk, username, fileBytes);
			} else {
				unencryptedBytes = CryptoUtil.universalDecrypt(fileBytes);
			}
			
			// Create new unencrypted file
			FileOutputStream stream = new FileOutputStream(activeFile.getAbsoluteFile(), false);
			stream.write(unencryptedBytes);
			stream.close();
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
		return activeFile;
	}
	
	/**
	 * Return the temporary file location, where the decrypted file is
	 * @return File The temporary file location
	 */
	public File getActiveFile() {
		return activeFile;
	}
	
	/**
	 * Change the decrypted file location.
	 * Destroys the previous instance of the decrypted file and sets the
	 * new location at the specified spot. The only way to destroy tmp file
	 * without destroying the DatabaseHandler object.
	 * @param path The new tmp file location
	 * @return The location the tmp file
	 */
	public File setActiveFile(String path) {
		// Destroy original instance, if any data existed
		try {
			if(!activeFile.getAbsolutePath().equals(path)) {
				activeFile.delete();
				activeFile = new File(path);
				activeFile.deleteOnExit();
			}
		} catch (SecurityException e) {
			e.printStackTrace();
			return null;
		}
		return activeFile;
	}
	
	/**
	 * Destroy the decrypted file.
	 * Destroys the decrypted file for safety. Call this whenever the file is no
	 * longer needed. File location stays, however, so if another tmp file needs
	 * to be made, it can be made with a decrypt() call.
	 * @return returns file if failed
	 */
	public File destroyActiveFile() {
		try {
			if(activeFile.exists()) {
				activeFile.delete();
			}
		} catch (SecurityException e) {
			e.printStackTrace();
			return null;
		}
		return activeFile;
	}
	
	// General Functions for helping sub-handler processes
	
	/**
	 * Create a xml document for the java parsers to read
	 * @param current The file to be converted, or null for a blank file
	 * @return The xml object used for parsing
	 * @throws Exception
	 */
	public static Document buildDoc(File current) throws Exception {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		if(current == null) {
			return docBuilder.newDocument();
		} else {
			Document output = docBuilder.parse(current);
			current.delete();
			return output;
		}
	}
	
	/**
	 * Create a template transformer for correctly writing the xml files
	 * @return Transformer for writing the xml files correctly
	 * @throws Exception
	 */
	public static Transformer buildTransformer() throws Exception {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "no");
        transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        return transformer;
	}
}
