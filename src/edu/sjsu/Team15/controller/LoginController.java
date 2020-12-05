package edu.sjsu.Team15.controller;


import edu.sjsu.Team15.Message;
import edu.sjsu.Team15.utility.DatabaseFunctions;
import edu.sjsu.Team15.model.User;
import edu.sjsu.Team15.view.LoginView;
import io.github.novacrypto.SecureCharBuffer;

import java.util.concurrent.LinkedBlockingQueue;

//TODO
public class LoginController {
    LinkedBlockingQueue<Message> queue;
    LoginView loginView;

    public LoginController(LinkedBlockingQueue<Message> queue, LoginView loginView){
        this.queue = queue;
        this.loginView = loginView;
    }

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
                    System.out.println("login");
                    User user = validateUser(message);
                    if (user != null) {
                        loginView.dispose();
                        return user;
                    }
                    break;
                case NEW_USER:
                    System.out.println("new user");
                    createUser(message);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + message.action);
            }
        }
    }

    private User validateUser(Message message) { // TODO
        String username = message.getUsername();
        SecureCharBuffer charBuffer = message.getPassword();

        User user = DatabaseFunctions.verifyUser(username, charBuffer);
        if (user == null) loginView.invalidCredsAlert();
        return user;
    }

    private void createUser(Message message) { // TODO
        String username = message.getUsername();
        SecureCharBuffer charBuffer = message.getPassword();

        User user = DatabaseFunctions.addNewUser(username, charBuffer);
        if (user == null) loginView.invalidCreationAlert();
        else loginView.enterCredsAlert();
    }
}
