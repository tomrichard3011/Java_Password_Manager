package edu.sjsu.Team15.utility;

import edu.sjsu.Team15.HandlerConstants;
import edu.sjsu.Team15.model.DomainHandler;
import edu.sjsu.Team15.model.User;
import edu.sjsu.Team15.model.UserHandler;
import io.github.novacrypto.SecureCharBuffer;

import java.io.File;

public class DatabaseFunctions {
    private final static String fileSep = System.getProperty("file.separator");
    private final static File ALL_USERS_FILE = new File(System.getProperty("user.dir") + fileSep + "data" + fileSep + "users.xyt");

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

    public static User verifyUser(String username, SecureCharBuffer password) {
        UserHandler userHandler = createUserHandler();
        return userHandler.verifyUser(username, password);
    }

    public static void saveDomains(User user) {
        DomainHandler dh = createDomainHandler(user);
        dh.writeDomains(user.getDomainInfoArray());
    }

    public static void editUserName(String username, SecureCharBuffer charBuffer, String newName) {
        UserHandler userHandler = createUserHandler();
        userHandler.editUser(username, charBuffer, HandlerConstants.XMLUSER, newName);
    }

    public static void editUserPass(String username, SecureCharBuffer charBuffer, SecureCharBuffer newPass) {
        UserHandler userHandler = createUserHandler();
        userHandler.editUser(username, charBuffer, HandlerConstants.XMLPASS, (String) newPass.toStringAble());
    }

    public static void editUserClipTime(String username, SecureCharBuffer charBuffer, int clipTime) {
        UserHandler userHandler = createUserHandler();
        userHandler.editUser(username, charBuffer, HandlerConstants.XMLCLIP, Integer.toString(clipTime));
    }

    private static void createUserFile() {
        if (!createDirectory()) System.out.println("Error");
        if (!ALL_USERS_FILE.exists()) {
            System.out.println();
            try {
                UserHandler.createNewUserFile(ALL_USERS_FILE);
            }
            catch (Exception e) {
                System.exit(1);
            }
        }
    }

    private static File createUserDomainFile(String username, SecureCharBuffer password) {
        File userDomainFile = new File(System.getProperty("user.dir") + fileSep + "data" + fileSep + username + ".xyt");
        if (!userDomainFile.exists()) {
            DomainHandler.createNewDomainFile(username, password, userDomainFile);
        }

        return userDomainFile;
    }

    private static boolean createDirectory() {
        File directory = new File(System.getProperty("user.dir") + fileSep + "data");
        if (!directory.exists()) {
            return directory.mkdir();
        }
        return directory.exists();
    }

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
