package edu.sjsu.Team15;

import io.github.novacrypto.SecureCharBuffer;

public class Message {
    Action action;
    private DomainInfo domainInfo;
    private String username;
    private SecureCharBuffer password;
    private int clearTime;

    public DomainInfo getDomainInfo() {
        return domainInfo;
    }

    public void setDomainInfo(DomainInfo domainInfo) {
        this.domainInfo = domainInfo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public SecureCharBuffer getPassword() {
        return password;
    }

    public void setPassword(SecureCharBuffer password) {
        this.password = password;
    }

    public int getClearTime() {
        return clearTime;
    }

    public void setClearTime(int clearTime) {
        this.clearTime = clearTime;
    }

    enum Action {
        EDIT_DOMAININFO,
        DELETE_DOMAININFO,
        COPY_PASSWORD,
        GENERATE_PASSWORD,
        LOGIN,
        NEW_USER,
        SET_USERNAME,
        SET_PASSWORD,
        SET_CLEARTIME,
        CREATE_DOMAININFO_MENU,
        SETTINGS_MENU,
        ADD_DOMAININFO,
    }
}
