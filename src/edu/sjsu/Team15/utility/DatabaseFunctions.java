package edu.sjsu.Team15.utility;

import edu.sjsu.Team15.model.DomainHandler;
import edu.sjsu.Team15.model.User;
import edu.sjsu.Team15.model.UserHandler;
import io.github.novacrypto.SecureCharBuffer;

import java.io.File;

public class DatabaseFunctions {
    /** System specific file separator */
    private final static String fileSep = System.getProperty("file.separator");
    /** HardCoded file path for file that contains all user credentials */
    private final static File ALL_USERS_FILE = new File(System.getProperty("user.dir") + fileSep + "data" + fileSep + "ALL_USERS.enc");

    /**
     * returns null if cannot add user, else returns the new user
     * @param username username
     * @param password password
     * @return null | user added
     */
    public static User addNewUser(String username, SecureCharBuffer password){
        // create user file if it doesnt exists
        createUserFile();
        UserHandler userHandler = createUserHandler();

        // If user exists, exit early
        if (userHandler.checkUser(username)) return null;

        File domainFile = createUserDomainFile(username, password);

         return userHandler.addUser(
                username,
                password,
                5,
                domainFile);
    }

    /**
     * Returns the user if they can be verified in the database
     * @param username username
     * @param password password
     * @return null | user
     */
    public static User verifyUser(String username, SecureCharBuffer password) {
        UserHandler userHandler = createUserHandler();
        if (!ALL_USERS_FILE.exists()) return null;
        return userHandler.verifyUser(username, password);
    }

    /**
     * save domains for a particular user
     * @param user User to save domains for
     */
    public static void saveDomains(User user) {
        DomainHandler dh = createDomainHandler(user);
        dh.writeDomains(user.getDomainInfoArray());
    }

    /**
     * UNUSED: Needs further testing
     * @param username username
     * @param charBuffer password
     * @param newName new username to change to
     */
    public static void editUserName(String username, SecureCharBuffer charBuffer, String newName) {
        UserHandler userHandler = createUserHandler();
        userHandler.editUser(username, charBuffer, HandlerConstants.XMLUSER, newName);
    }

    /**
     * UNUSED: Needs further testing
     * @param username username
     * @param charBuffer password
     * @param newPass new password to change to
     */
    public static void editUserPass(String username, SecureCharBuffer charBuffer, SecureCharBuffer newPass) {
        UserHandler userHandler = createUserHandler();
        userHandler.editUser(username, charBuffer, HandlerConstants.XMLPASS, (String) newPass.toStringAble());
    }

    /**
     * Changes clipboard time in database
     * @param username username
     * @param charBuffer password
     * @param clipTime new clipboard clear time to change
     */
    public static void editUserClipTime(String username, SecureCharBuffer charBuffer, int clipTime) {
        UserHandler userHandler = createUserHandler();
        userHandler.editUser(username, charBuffer, HandlerConstants.XMLCLIP, Integer.toString(clipTime));
    }

    /**
     * creates a users file if it doesn't exist
     */
    private static void createUserFile() {
        if (!createDirectory()) System.exit(1);
        if (!ALL_USERS_FILE.exists()) {
            try {
                UserHandler.createNewUserFile(ALL_USERS_FILE);
            }
            catch (Exception e) {
                System.exit(1);
            }
        }
    }

    /**
     * Create a user domain file for first time
     * @param username username
     * @param password password
     * @return File object containing the file location
     */
    private static File createUserDomainFile(String username, SecureCharBuffer password) {
        File userDomainFile = new File(System.getProperty("user.dir") + fileSep + "data" + fileSep + username + ".enc");
        if (!userDomainFile.exists()) {
            DomainHandler.createNewDomainFile(username, password, userDomainFile);
        }

        return userDomainFile;
    }

    /**
     * creates a data directory if it doesnt exist
     * @return false if the program cannot create a directory
     */
    private static boolean createDirectory() {
        File directory = new File(System.getProperty("user.dir") + fileSep + "data");
        if (!directory.exists()) {
            return directory.mkdir();
        }
        return directory.exists();
    }

    /**
     * Safely create a user handler
     * @return valid user handler
     */
    private static UserHandler createUserHandler() {
        UserHandler userHandler = null;
        try {
            userHandler = new UserHandler(ALL_USERS_FILE.getAbsolutePath());
        }
        catch (Exception e) {
            System.exit(1);
        }
        return userHandler;
    }

    /**
     * Safely create a domain handler
     * @param user user's to look for
     * @return valid domain handler
     */
    private static DomainHandler createDomainHandler(User user) {
        DomainHandler dh = null;
        try {
            dh = new DomainHandler(user.getFileLocation().getAbsolutePath(), user.getMasterKey(), user.getUsername());
        }
        catch (Exception e) {
            System.exit(1);
        }
        return dh;
    }
}
