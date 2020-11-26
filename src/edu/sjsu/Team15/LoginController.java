package edu.sjsu.Team15;

import java.util.concurrent.LinkedBlockingQueue;

//TODO
public class LoginController {
    LinkedBlockingQueue<Message> queue;

    public void run() {
        while (true) {
            Message message = null;
            try{
                message = queue.take(); // thread waits for input
            }
            catch (InterruptedException e) {
                System.exit(1); // TODO error handling
            }

            switch (message.action) {
                case LOGIN:
                    break;
                case NEW_USER:
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + message.action);
            }
        }
    }
}
