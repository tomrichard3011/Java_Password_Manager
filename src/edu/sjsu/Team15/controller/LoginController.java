package edu.sjsu.Team15.controller;


import edu.sjsu.Team15.Message;
import edu.sjsu.Team15.model.User;
import edu.sjsu.Team15.model.UserHandler;
import edu.sjsu.Team15.view.LoginView;
import io.github.novacrypto.SecureCharBuffer;

import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;

//TODO
public class LoginController {
    LinkedBlockingQueue<Message> queue;
    LoginView loginView;

    public void run() {
        Message message = null;
        try{
            message = queue.take(); // thread waits for input
        }
        catch (InterruptedException e) {
            System.exit(1); // TODO error handling
        }

        switch (message.action) {
            case LOGIN:
                validateUser(message);
            case NEW_USER:
                createUser(message);
            default:
                throw new IllegalStateException("Unexpected value: " + message.action);
        }
    }

    private void validateUser(Message message) { // TODO
        String username = message.getUsername();
        SecureCharBuffer charBuffer = message.getPassword();
        UserHandler userHandler = null;
        try {
            userHandler = new UserHandler(System.getProperty("user.dir") + "\\data\\user.xyt", // TODO SET DIRECTORIES
                    charBuffer,
                    username);
        }
        catch (Exception e) {
            System.exit(1); // error
        }

        User user = userHandler.verifyUser(username, charBuffer);
    }

    private void createUser(Message message) { // TODO
        String username = message.getUsername();
        SecureCharBuffer charBuffer = message.getPassword();
        UserHandler userHandler = null;
        try {
            userHandler = new UserHandler(System.getProperty("user.dir") + "\\data\\user.xyt", // TODO
                    charBuffer,
                    username);
        }
        catch (Exception e) {
            System.exit(1); // error
        }

        User user = userHandler.addUser(message.getUsername(),
                message.getPassword(),
                5,
                new File(System.getProperty("user.dir") + "\\data\\domain.xyt"));

//        if (userHandler.verifyUser()) {
//            loginView.dispose(); // destroy login view
//            // then open next window
//        }
    }
}
