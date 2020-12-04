package edu.sjsu.Team15.model;

import io.github.novacrypto.SecureCharBuffer;

import java.io.File;

public class DatabaseFunctions {
    private final static String fileSep = System.getProperty("file.separator");
    private final static File ALL_USERS_FILE = new File(System.getProperty("user.dir") + fileSep + "data" + fileSep + "users.xyt");

    public static User addNewUser(String username, SecureCharBuffer password){
        UserHandler userHandler = null;

        // create user file if it doesnt exists
        createUserFile();

        // create user handler
        try {
            userHandler = new UserHandler(ALL_USERS_FILE.getPath());
        }
        catch (Exception e) {
            System.exit(1);
        }

        // If user exists, exit early
        if (userHandler.checkUser(username)) return null;

        File domainFile = createUserDomainFile(username, password);

        User user =  userHandler.addUser(
                username,
                password,
                5,
                domainFile);
        return user;
    }

    public static User verifyUser(String username, SecureCharBuffer password) {
        UserHandler userHandler = null;
        try {
            userHandler = new UserHandler(ALL_USERS_FILE.getAbsolutePath());
        }
        catch (Exception e) {
            return null;
        }

        return userHandler.verifyUser(username, password);
    }

    public static void saveDomains(User user) {
        DomainHandler dh = null;
        try {
            dh = new DomainHandler(System.getProperty("user.dir") + fileSep + "data" + fileSep + user.getUsername() + ".xyt", user.getMasterKey(), user.getUsername());
        }
        catch (Exception e) {
            System.exit(1);
        }
        dh.writeDomains(user.getDomainInfoArray());
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
}
