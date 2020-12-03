package edu.sjsu.Team15.model;

import edu.sjsu.Team15.model.DomainInfo;
import io.github.novacrypto.SecureCharBuffer;
import java.io.File;
import java.util.ArrayList;

public class User {
    private int clipboardClearTime;
    private ArrayList<DomainInfo> domainInfoArray;
    private String username;
    private SecureCharBuffer masterKey;
    private File secureFile;


    public User(int clipboardClearTime, ArrayList<DomainInfo> domainInfoArray, String username, SecureCharBuffer masterKey) {
        this.clipboardClearTime = clipboardClearTime;
        this.domainInfoArray = domainInfoArray;
        this.username = username;
        this.masterKey = masterKey;
    }
    
    // Added constructor to allow DomainInfo to be retrieved later
    public User(String username, SecureCharBuffer password, int clipboardTime, String filepath) {
    	// Turn password into a SecureCharBuffer
    	this.username = username;
    	this.masterKey = password;
    	this.clipboardClearTime = clipboardTime;
    	this.secureFile = new File(filepath);
    	this.domainInfoArray = null;
    }

    public void setClipboardClearTime(int clipboardClearTime) {
        this.clipboardClearTime = clipboardClearTime;
    }

    public int getClipboardClearTime() {
        return clipboardClearTime;
    }

    public ArrayList<DomainInfo> getDomainInfoArray() {
        return domainInfoArray;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public SecureCharBuffer getMasterKey() {
        return masterKey;
    }

    public void setMasterKey(SecureCharBuffer masterKey) {
        this.masterKey = masterKey;
    }
    
    // Added method to allow DomainInfo to be retrieved later
    public void setDomainInfo(ArrayList<DomainInfo> list) {
    	this.domainInfoArray = list;
    }
    
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
