package edu.sjsu.Team15.view;

import edu.sjsu.Team15.Message;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.LinkedBlockingQueue;

public class LoginView { //TODO
    JButton login, newAcc;
    LoginButtonPress loginButtonListener;
    LinkedBlockingQueue<Message> queue;

    public LoginView(LinkedBlockingQueue<Message> queue){
        //variables for text field and label
        JLabel username, password;
        JTextField un, pass;

        //making a frame
        JFrame f = new JFrame("Password Manager");

        //creating buttons
        login = new JButton("Login");
        newAcc = new JButton("Create Account");

        // creating listener
        loginButtonListener = new LoginButtonPress();

        //creating labels
        username = new JLabel("Username: ");
        username.setBounds(50,50, 100,30);
        password = new JLabel("Password: ");
        password.setBounds(50,100, 100,30);

        //creating text fields
        un = new JTextField(10);
        un.setBounds(150,50, 120,40);
        pass = new JTextField(10);
        pass.setBounds(150,100, 120,40);

        //setting buttons
        login.setBounds(75, 160, 90, 40);
        newAcc.setBounds(170, 160, 120, 40);

        //add label
        f.add(username);
        f.add(password);

        //add text field
        f.add(un);
        f.add(pass);

        //add buttons
        f.add(login);
        f.add(newAcc);

        //set the frame
        f.setSize(350, 270);
        f.setLayout(null);
        f.setVisible(true);

        login.addActionListener(loginButtonListener);
        newAcc.addActionListener(loginButtonListener);
    }

    private class LoginButtonPress implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Object source = actionEvent.getSource();
            if (source == login) { // TODO BUTTONS
                Message message = new Message();
                message.action = Message.Action.LOGIN;
                queue.add(message);
            }
            if (source == newAcc) {
                // TODO setup message
                Message message = new Message();
                message.action = Message.Action.NEW_USER;
                queue.add(message);
            }
        }
    }
}
