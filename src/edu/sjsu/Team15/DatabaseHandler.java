package edu.sjsu.Team15;

/**
 * Sources:
 * https://mkyong.com/java/how-to-convert-file-into-an-array-of-bytes/
 * https://docs.oracle.com/javase/7/docs/api/java/io/File.html
 * https://stackoverflow.com/questions/4350084/byte-to-file-in-java
 */

import io.github.novacrypto.SecureCharBuffer;
import java.nio.file.Files;
import java.io.*;

public abstract class DatabaseHandler {
	private File secureFile;
	private File activeFile;
	
	/**
	 * Create a DatabaseHandler Instance.
	 * Both file locations must be provided and are used for encrypted and
	 * decrypted file locations.
	 * 
	 * @param pathname The database file location
	 * @param tempFile The file location for the decrypted file
	 */
	public DatabaseHandler(File dataFile, File tempFile) {
		secureFile = dataFile;
		activeFile = tempFile;
		activeFile.deleteOnExit();
	}
	
	public File encrypt(SecureCharBuffer sk, String username) {
		try {
			// Transform decrypted file to encrypted byte array
			byte[] fileBytes = Files.readAllBytes(activeFile.toPath());
			byte[] encryptedBytes = CryptoUtil.encrypt(sk, username, fileBytes);
			// Save the encrypted file
			FileOutputStream stream = new FileOutputStream(secureFile.getAbsoluteFile(), false);
			stream.write(encryptedBytes);
			stream.close();
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
		return secureFile;
	}
	
	public File decrypt(SecureCharBuffer sk, String username) {
		try {
			// Transform encrypted file to unencrypted byte array
			byte[] fileBytes = Files.readAllBytes(secureFile.toPath());
			byte[] unencryptedBytes = CryptoUtil.decrypt(sk, username, fileBytes);
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
			System.out.println("WARNING: Decrypted file cannot be removed due to permissions! Please delete the file manually or resolve permissions.");
		}
		return activeFile;
	}
	
	/**
	 * Destroy the decrypted file.
	 * Destroys the decrypted file for safety. Call this whenever the file is no
	 * longer needed. File location stays, however, so if another tmp file needs
	 * to be made, it can be made with a decrypt() call.
	 * @return
	 */
	public File destroyActiveFile() {
		try {
			activeFile.delete();
		} catch (SecurityException e) {
			System.out.println("WARNING: Decrypted file cannot be removed due to permissions! Please delete the file manually or resolve permissions.");
		}
		return activeFile;
	}
}
