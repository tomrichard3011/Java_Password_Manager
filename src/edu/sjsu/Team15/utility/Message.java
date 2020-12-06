package edu.sjsu.Team15.utility;

import edu.sjsu.Team15.model.DomainInfo;
import io.github.novacrypto.SecureCharBuffer;

/**
 * Stores data to be transferred between Model View and Controller
 */
public class Message {
    /** command to be processed */
    public Action action;
    /** used to identify a domain info to edit, or to delete */
    private DomainInfo domainInfo;
    /** username field used for new domain info creation */
    private String username;
    /** password field used for new domain info creation */
    private SecureCharBuffer password;
    /** new clipboard time to set */
    private int clearTime;

    /**
     * Domain info getter
     * @return get domain info
     */
    public DomainInfo getDomainInfo() {
        return domainInfo;
    }

    /**
     * Domain info setter
     * @param domainInfo to set
     */
    public void setDomainInfo(DomainInfo domainInfo) {
        this.domainInfo = domainInfo;
    }

    /**
     * username getter
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * uername setter
     * @param username username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * password getter
     * @return password
     */
    public SecureCharBuffer getPassword() {
        return password;
    }

    /**
     * password setter
     * @param password password to transfer in message
     */
    public void setPassword(SecureCharBuffer password) {
        this.password = password;
    }

    /**
     * get clear time
     * @return clear time to set
     */
    public int getClearTime() {
        return clearTime;
    }

    /**
     * clear time setter
     * @param clearTime time to set for user
     */
    public void setClearTime(int clearTime) {
        this.clearTime = clearTime;
    }

    /**
     * Enum defining actions to be parsed
     */
    public enum Action {
        EDIT_DOMAININFO,
        DELETE_DOMAININFO,
        COPY_PASSWORD,
        GENERATE_PASSWORD,
        SET_CLEARTIME,
        CREATE_DOMAININFO_MENU,
        SETTINGS_MENU,
        ADD_DOMAININFO,
        LOGIN,
        NEW_USER,
        EXIT
    }
}
