package edu.sjsu.Team15;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;

public class DomainInfoView extends JPanel{
    // Actual Object temporary
    private DomainInfo domainInfo;
    private final LinkedBlockingQueue<Message> queue;

    // JComponents
    private final JLabel domain;
    private final JLabel logo;
    private final JLabel username;

    private final JLabel upleadAttribution;

    private final JButton editButton;
    private final JButton deleteButton;
    private final JButton copyPassButton;
    private final JButton genNewPassButton;

    ButtonPress buttonListener = new ButtonPress();

    public DomainInfoView(DomainInfo domainInfo, LinkedBlockingQueue<Message> queue) {
        this.domainInfo = domainInfo;
        this.queue = queue;

        domain = new JLabel(this.domainInfo.getDomain());
        logo = new JLabel();
        logo.setIcon(getImageIcon());
        username = new JLabel("Username: " + this.domainInfo.getUsername());

        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        copyPassButton = new JButton("Copy Pass");
        genNewPassButton = new JButton("Create New Pass");
        editButton.addActionListener(buttonListener);
        deleteButton.addActionListener(buttonListener);
        copyPassButton.addActionListener(buttonListener);
        genNewPassButton.addActionListener(buttonListener);

        upleadAttribution = new JLabel("Logos provided by UpLead");

        this.setLayout(new SpringLayout());

        this.add(domain);
        this.add(logo);
        this.add(username);
        this.add(editButton);
        this.add(deleteButton);
        this.add(copyPassButton);
        this.add(genNewPassButton);
        this.add(upleadAttribution);

        this.setPreferredSize(new Dimension(350, 400));

        springLayoutSetup((SpringLayout) this.getLayout());
    }

    public void updateDomainInfo(DomainInfo domainInfo) {
        this.domainInfo = domainInfo; // set new domain info

        // set
        this.domain.setText(this.domainInfo.getDomain());
        this.logo.setIcon(getImageIcon());
        this.logo.repaint();
        this.username.setText("Username: " + this.domainInfo.getUsername());

        this.revalidate();
    }

    private void springLayoutSetup(SpringLayout layout) {

        // Domain name
        layout.putConstraint(SpringLayout.NORTH, domain, 20,
                SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, domain, 0,
                SpringLayout.HORIZONTAL_CENTER, this);

        // Domain Logo
        layout.putConstraint(SpringLayout.NORTH, logo, 20,
                SpringLayout.SOUTH, domain);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, logo, 0,
                SpringLayout.HORIZONTAL_CENTER, this);

        // edit button
        layout.putConstraint(SpringLayout.NORTH, editButton, 20,
                SpringLayout.SOUTH, logo);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, editButton, -40,
                SpringLayout.HORIZONTAL_CENTER, this);

        // delete button
        layout.putConstraint(SpringLayout.NORTH, deleteButton, 20,
                SpringLayout.SOUTH, logo);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, deleteButton, 40,
                SpringLayout.HORIZONTAL_CENTER, this);

        // user name
        layout.putConstraint(SpringLayout.NORTH, username, 20,
                SpringLayout.SOUTH, editButton);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, username, 0,
                SpringLayout.HORIZONTAL_CENTER, this);

        // Copy Pass
        layout.putConstraint(SpringLayout.NORTH, copyPassButton, 20,
                SpringLayout.SOUTH, username);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, copyPassButton, 0,
                SpringLayout.HORIZONTAL_CENTER, this);

        // generate new pass
        layout.putConstraint(SpringLayout.NORTH, genNewPassButton, 20,
                SpringLayout.SOUTH, copyPassButton);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, genNewPassButton, 0,
                SpringLayout.HORIZONTAL_CENTER, this);

        // UpLead attribution
        layout.putConstraint(SpringLayout.NORTH, upleadAttribution, 20,
                SpringLayout.SOUTH, genNewPassButton);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, upleadAttribution, 0,
                SpringLayout.HORIZONTAL_CENTER, this);

    }

    private ImageIcon getImageIcon() {
        BufferedImage image;
        try {
            image = ImageIO.read(new File(domainInfo.getLogoPath()));
        }
        catch (Exception e){
            image = new BufferedImage(128, 128, BufferedImage.TYPE_4BYTE_ABGR);
        }

        return new ImageIcon(image);
    }

    private class ButtonPress implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Object source = actionEvent.getSource();
            Message message = new Message();

            if (source == editButton) {
                message.action = Message.Action.EDIT_DOMAININFO;
                message.setDomainInfo(domainInfo);
                queue.add(message);
            }
            if (source == deleteButton) {
                message.action = Message.Action.DELETE_DOMAININFO;
                message.setDomainInfo(domainInfo);
                queue.add(message);
            }
            if (source == copyPassButton) {
                message.action = Message.Action.COPY_PASSWORD;
                message.setDomainInfo(domainInfo);
                queue.add(message);
            }
            if (source == genNewPassButton) {
                message.action = Message.Action.GENERATE_PASSWORD;
                message.setDomainInfo(domainInfo);
                queue.add(message);
            }
        }
    }
}