package edu.sjsu.Team15.view;

import edu.sjsu.Team15.model.DomainInfo;
import io.github.novacrypto.SecureCharBuffer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Gui for when a user needs to edit a DomainInfo object
 */
public class EditDomainInfoView extends JFrame{
	/** The frame for holding edit window */
    private final JFrame frame = this;
    /** The edit button  */
    private final JButton editButton = new JButton("Edit");

    /** User Label */
    private final JLabel usernameLabel = new JLabel("Enter username:");
    /** Password Label */
    private final JLabel passwordLabel = new JLabel("Enter password:");

    /** User Input */
    private final JTextField usernameField = new JTextField();
    /** Password Input */
    private final JPasswordField passwordField = new JPasswordField();

    /** Layout Object */
    private final SpringLayout layout = new SpringLayout();

    /**
     * Constructor
     * @param domainInfo domain info to edit
     * @param domainInfoView domainInfo view to update
     */
    public EditDomainInfoView(DomainInfo domainInfo, DomainInfoView domainInfoView) {
        this.setTitle(domainInfo.getDomain());
        usernameField.setColumns(10);
        passwordField.setColumns(10);

        usernameField.setText(domainInfo.getUsername());

        this.add(usernameLabel);
        this.add(usernameField);
        this.add(passwordLabel);
        this.add(passwordField);
        this.add(editButton);
        this.setLayout(layout);

        editButton.addActionListener(actionEvent -> {
            String user = usernameField.getText();
            char[] pass = passwordField.getPassword();
            SecureCharBuffer charBuffer = new SecureCharBuffer();
            if (user.equals("")) user = domainInfo.getUsername();
            if (pass.length == 0) charBuffer = domainInfo.getPassword();

            for (char c : pass) {
                charBuffer.append(c);
            }

            domainInfo.setUsername(user);
            domainInfo.setPassword(charBuffer);

            domainInfoView.updateDomainInfo(domainInfo);
            frame.dispose();
        });

        springLayoutSetup();

        // frame setups
        this.setPreferredSize(new Dimension(350, 150));
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
    }

    /**
     * Spring layout setup for this frame
     */
    private void springLayoutSetup() {
        //Username labels and fields
        layout.putConstraint(SpringLayout.NORTH, usernameField, 10,
                SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.WEST, usernameField, 0,
                SpringLayout.HORIZONTAL_CENTER, this);

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, usernameLabel, 0,
                SpringLayout.VERTICAL_CENTER, usernameField);
        layout.putConstraint(SpringLayout.EAST, usernameLabel, -10,
                SpringLayout.WEST, usernameField);

        // password labels and fields
        layout.putConstraint(SpringLayout.NORTH, passwordField, 10,
                SpringLayout.SOUTH, usernameField);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, passwordField, 0,
                SpringLayout.HORIZONTAL_CENTER, usernameField);

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, passwordLabel, 0,
                SpringLayout.VERTICAL_CENTER, passwordField);
        layout.putConstraint(SpringLayout.EAST, passwordLabel, -10,
                SpringLayout.WEST, passwordField);

        // Edit button
        layout.putConstraint(SpringLayout.NORTH, editButton, 10,
                SpringLayout.SOUTH, passwordLabel);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, editButton, 0,
                SpringLayout.HORIZONTAL_CENTER, this);
    }
}
