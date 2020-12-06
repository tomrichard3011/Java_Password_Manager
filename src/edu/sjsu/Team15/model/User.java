package edu.sjsu.Team15.model;

import io.github.novacrypto.SecureCharBuffer;
import java.io.File;
import java.util.ArrayList;

/**
 * Model for user, contains main username, main password, and all domains for a user
 */
public class User {
	/** The amount of time that a password will stay on the computer's clipboard */
    private int clipboardClearTime;
    /** The list of domains that the user saved */
    private ArrayList<DomainInfo> domainInfoArray;
    /** The username and the salt used for the file encryption */
    private String username;
    /** The master password and the key used for encryption */
    private SecureCharBuffer masterKey;
    /** The file location of the secure file of all domain information */
    private File secureFile;

    /**
     * Create user object, with a list of domains pre-made
     * @param clipboardClearTime The time a password will stay on the computer clipboard
     * @param domainInfoArray The list of domains
     * @param username The username and salt for encryption
     * @param masterKey The password and key for encryption
     */
    public User(int clipboardClearTime, ArrayList<DomainInfo> domainInfoArray, String username, SecureCharBuffer masterKey) {
        this.clipboardClearTime = clipboardClearTime;
        this.domainInfoArray = domainInfoArray;
        this.username = username;
        this.masterKey = masterKey;
    }
    
    /**
     * Create user object, without the list of domains
     * @param username The username and salt for encryption
     * @param password The password and key for encryption
     * @param clipboardTime The time a password will stay on the computer clipboard
     * @param filepath file path for user's domains
     */
    public User(String username, SecureCharBuffer password, int clipboardTime, String filepath) {
    	this.username = username;
    	this.masterKey = password;
    	this.clipboardClearTime = clipboardTime;
    	this.secureFile = new File(filepath);
    	this.domainInfoArray = null;
    }

    /**
     * Set the clipboard clear time
     * @param clipboardClearTime The value it will change to
     */
    public void setClipboardClearTime(int clipboardClearTime) {
        this.clipboardClearTime = clipboardClearTime;
    }

    /**
     * Get the clipboard clear time
     * @return int The time in the user object
     */
    public int getClipboardClearTime() {
        return clipboardClearTime;
    }

    /**
     * Get the list of domains stored in the user object
     * Note: If it returns null, then either the file didn't finish reading the domain file,
     * or there was an error in fetching the domains (Applies mostly to the database handlers)
     * @return ArrayList<DomainInfo> The list
     */
    public ArrayList<DomainInfo> getDomainInfoArray() {
        return domainInfoArray;
    }

    /**
     * Get the username stored in the object
     * @return String The username
     */
    public String getUsername() {
        return username;
    }

    /** Set the username stored in the object */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the master key, in a SecureCharBuffer
     * @return SecureCharBuffer The password
     */
    public SecureCharBuffer getMasterKey() {
        return masterKey;
    }

    /**
     * Set the master key, in a SecureCharBuffer
     * @param masterKey The password
     */
    public void setMasterKey(SecureCharBuffer masterKey) {
        this.masterKey = masterKey;
    }
    
    /**
     * Set the domain information list
     * Works in tandem with the second constructor to create the user, then fetch the domains
     * @param list The list of DomainInfo objects
     */
    public void setDomainInfo(ArrayList<DomainInfo> list) {
    	this.domainInfoArray = list;
    }
    
    /**
     * Set a new secure file location
     * @param f The file location of the new file
     */
    public void setFileLocation(File f) {
    	this.secureFile = f;
    }
    
    /**
     * Get the secure file location.
     * Used primarily to get and save to the database file
     * @return The file path
     */
    public File getFileLocation() {
    	return secureFile;
    }
}
