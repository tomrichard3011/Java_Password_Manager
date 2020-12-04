package edu.sjsu.Team15.view;

import edu.sjsu.Team15.Message;
import io.github.novacrypto.SecureCharBuffer;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.LinkedBlockingQueue;

public class LoginView extends JFrame{ //TODO
    JButton login, newAcc;
    LoginButtonPress loginButtonListener;
    LinkedBlockingQueue<Message> queue;
    JLabel username, password;
    JTextField un;
    JPasswordField pass;
    JFrame frame = this;

    public LoginView(LinkedBlockingQueue<Message> queue){
        //variables for text field and label
        this.queue = queue;
        //making a frame

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
        pass = new JPasswordField(10);
        pass.setBounds(150,100, 120,40);

        //setting buttons
        login.setBounds(75, 160, 90, 40);
        newAcc.setBounds(170, 160, 120, 40);

        //set listener
        login.addActionListener(loginButtonListener);
        newAcc.addActionListener(loginButtonListener);

        //add label
        frame.add(username);
        frame.add(password);

        //add text field
        frame.add(un);
        frame.add(pass);

        //add buttons
        frame.add(login);
        frame.add(newAcc);

        //set the frame
        frame.setSize(350, 270);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    private class LoginButtonPress implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Message message = new Message();
            // get transfer password from char[] to secureCharBuffer
            SecureCharBuffer charBuffer = new SecureCharBuffer();
            for (char c : pass.getPassword()) {
                charBuffer.append(c);
                c = '\0'; // null password as we append
            }

            // do nothing if login is empty.
            if (charBuffer.length() == 0 || un.getText() == "") return;


            // put data into message
            message.setUsername(un.getText());
            message.setPassword(charBuffer);

            // clear text fields
            un.setText("");
            pass.setText("");


            Object source = actionEvent.getSource();
            if (source == login) {
                message.action = Message.Action.LOGIN;
                queue.add(message);
            }
            if (source == newAcc) {
                message.action = Message.Action.NEW_USER;
                queue.add(message);
            }
        }
    }
}
