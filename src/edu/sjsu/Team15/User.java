package edu.sjsu.Team15;

import io.github.novacrypto.SecureCharBuffer;

import java.util.ArrayList;

public class User {
    private int ClipboardClearTime;
    private ClipboardManager clipboardManager;
    private ArrayList<DomainInfo> domainInfoArray;
    private String Username;
    private SecureCharBuffer MaterKey;

    public User(int clipboardClearTime, ClipboardManager clipboardManager, ArrayList<DomainInfo> domainInfoArray, String username, SecureCharBuffer materKey) {
        ClipboardClearTime = clipboardClearTime;
        this.clipboardManager = clipboardManager;
        this.domainInfoArray = domainInfoArray;
        Username = username;
        MaterKey = materKey;
    }

    public int getClipboardClearTime() {
        return ClipboardClearTime;
    }

    public ClipboardManager getClipboardManager() {
        return clipboardManager;
    }

    public ArrayList<DomainInfo> getDomainInfoArray() {
        return domainInfoArray;
    }

    public String getUsername() {
        return Username;
    }

    public SecureCharBuffer getMaterKey() {
        return MaterKey;
    }
}
