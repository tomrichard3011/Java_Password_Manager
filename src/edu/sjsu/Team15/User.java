package edu.sjsu.Team15;

import io.github.novacrypto.SecureCharBuffer;

import java.util.ArrayList;

public class User {
    private int clipboardClearTime;
    private ClipboardManager clipboardManager;
    private ArrayList<DomainInfo> domainInfoArray;
    private String username;
    private SecureCharBuffer masterKey;


    public User(int clipboardClearTime, ArrayList<DomainInfo> domainInfoArray, String username, SecureCharBuffer masterKey) {
        this.clipboardClearTime = clipboardClearTime;
        this.clipboardManager = new ClipboardManager(clipboardClearTime);
        this.domainInfoArray = domainInfoArray;
        this.username = username;
        this.masterKey = masterKey;
    }

    public void setClipboardClearTime(int clipboardClearTime) {
        this.clipboardClearTime = clipboardClearTime;
        this.getClipboardManager().setClearTime(clipboardClearTime);
    }

    public ClipboardManager getClipboardManager() {
        return clipboardManager;
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
}
