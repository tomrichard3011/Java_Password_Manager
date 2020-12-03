package edu.sjsu.Team15.controller;


import edu.sjsu.Team15.Message;
import edu.sjsu.Team15.model.User;
import io.github.novacrypto.SecureCharBuffer;

import java.util.concurrent.LinkedBlockingQueue;

//TODO
public class LoginController {
    LinkedBlockingQueue<Message> queue;

    public User initialLogin() {
        Message message = null;
        try{
            message = queue.take(); // thread waits for input
        }
        catch (InterruptedException e) {
            System.exit(1); // TODO error handling
        }

        switch (message.action) {
            case LOGIN:
                return validateUser();
            case NEW_USER:
                return createUser();
            default:
                throw new IllegalStateException("Unexpected value: " + message.action);
        }
    }

    private User validateUser() { // TODO
        return new User("user", new SecureCharBuffer(), 3, "");
    }

    private User createUser() {
        return new User("user", new SecureCharBuffer(), 3, "");
    }
}
