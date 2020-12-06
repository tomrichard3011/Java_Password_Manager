package edu.sjsu.Team15.controller;


import edu.sjsu.Team15.utility.Message;
import edu.sjsu.Team15.utility.DatabaseFunctions;
import edu.sjsu.Team15.model.User;
import edu.sjsu.Team15.view.LoginView;
import io.github.novacrypto.SecureCharBuffer;

import java.util.concurrent.LinkedBlockingQueue;

public class LoginController {
    /** Command queue that holds messages to be processed */
    LinkedBlockingQueue<Message> queue;
    /** View to be updated depending on what commands are processed */
    LoginView loginView;

    /**
     * Constructor
     * @param queue thread safe queue that holds all commands
     * @param loginView view to link to
     */
    public LoginController(LinkedBlockingQueue<Message> queue, LoginView loginView){
        this.queue = queue;
        this.loginView = loginView;
    }

    /**
     * Loop through commands until we get a valid user
     * @return Valid user found in database
     */
    public User run() {
        while (true) {
            Message message = null;
            try {
                message = queue.take(); // thread waits for input
            } catch (InterruptedException e) {
                System.exit(1); // TODO error handling
            }

            switch (message.action) {
                case LOGIN:
                    User user = validateUser(message);
                    if (user != null) {
                        loginView.dispose();
                        return user;
                    }
                    break;
                case NEW_USER:
                    createUser(message);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + message.action);
            }
        }
    }

    /**
     * Checks if user exists in database
     * @param message Message that contains extra data
     * @return Valid user
     */
    private User validateUser(Message message) { // TODO
        String username = message.getUsername();
        SecureCharBuffer charBuffer = message.getPassword();

        User user = DatabaseFunctions.verifyUser(username, charBuffer);
        if (user == null) loginView.invalidCredsAlert();
        return user;
    }

    /**
     * Creates a user in the database
     * @param message Message that contains extra data
     */
    private void createUser(Message message) { // TODO
        String username = message.getUsername();
        SecureCharBuffer charBuffer = message.getPassword();

        User user = DatabaseFunctions.addNewUser(username, charBuffer);
        if (user == null) loginView.invalidCreationAlert();
        else loginView.enterCredsAlert();
    }
}
