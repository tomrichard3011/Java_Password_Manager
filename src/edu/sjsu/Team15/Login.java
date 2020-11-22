package edu.sjsu.Team15;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.*;

public class Login {

    public Login(){
        //variables for text field and label
        JLabel username, password;
        JTextField un, pass;
        //making a frame
        JFrame f=new JFrame("Password Manager");
        //creating buttons
        JButton login = new JButton("Login");
        JButton newAcc = new JButton("Create Account");
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

        login.addActionListener(e -> new LoginDomain());
        newAcc.addActionListener(e -> new LoginDomain());

    }
}
