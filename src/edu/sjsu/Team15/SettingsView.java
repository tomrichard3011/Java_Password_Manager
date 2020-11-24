package edu.sjsu.Team15;
// TODO

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.LinkedBlockingQueue;

public class SettingsView extends JFrame {
    private LinkedBlockingQueue<ButtonEnum> queue;


    private JPanel contentPane;
    private JTextField userName;
    private JTextField clear;
    private JPasswordField pass;

    private JButton btnSaveName;
    private JButton btnSavePassword;
    private JButton btnClearTime;

    private SettingButtonListener settingButtonListener;

    /**
     * Create the frame.
     */
    public SettingsView(LinkedBlockingQueue<ButtonEnum> queue) {
        this.queue = queue;

        setBounds(100, 100, 450, 200);

        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(new SpringLayout());

        userName = new JTextField("UserName");
        contentPane.add(userName);
        userName.setColumns(10);

        clear = new JTextField();
        clear.setColumns(10);
        contentPane.add(clear);

        pass = new JPasswordField();
        pass.setColumns(10);
        contentPane.add(pass);

        // SAVE NAME BUTTON
        btnSaveName = new JButton("Change Name: ");
        btnSaveName.addActionListener(settingButtonListener);
        contentPane.add(btnSaveName);

        // SAVE PASSWORD BUTTON
        btnSavePassword = new JButton("Change Password: ");
        btnSavePassword.addActionListener(settingButtonListener);
        contentPane.add(btnSavePassword);

        // CEAR TIME BUTTON
        btnClearTime = new JButton("Clear Time: ");
        btnClearTime.addActionListener(settingButtonListener);
        contentPane.add(btnClearTime);




        SpringLayoutSetup((SpringLayout) contentPane.getLayout());
    }

    private void SpringLayoutSetup(SpringLayout layout) {
        // USERNAME LAYOUT
        layout.putConstraint(SpringLayout.NORTH, btnSaveName, 20,
                SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, btnSaveName, 20,
                SpringLayout.WEST, contentPane);

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, userName, 0,
                SpringLayout.VERTICAL_CENTER, btnSaveName);
        layout.putConstraint(SpringLayout.WEST, userName, 20,
                SpringLayout.EAST, btnSaveName);

        // PASSWORD LAYOUT
        layout.putConstraint(SpringLayout.NORTH, btnSavePassword, 20,
                SpringLayout.SOUTH, btnSaveName);
        layout.putConstraint(SpringLayout.WEST, btnSavePassword, 20,
                SpringLayout.WEST, contentPane);

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, pass, 0,
                SpringLayout.VERTICAL_CENTER, btnSavePassword);
        layout.putConstraint(SpringLayout.WEST, pass, 20,
                SpringLayout.EAST, btnSavePassword);

        // CLEAR TIME BUTTON
        layout.putConstraint(SpringLayout.NORTH, btnClearTime, 20,
                SpringLayout.SOUTH, btnSavePassword);
        layout.putConstraint(SpringLayout.WEST, btnClearTime, 20,
                SpringLayout.WEST, contentPane);

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, clear, 0,
                SpringLayout.VERTICAL_CENTER, btnClearTime);
        layout.putConstraint(SpringLayout.WEST, clear, 20,
                SpringLayout.EAST, btnClearTime);
    }

    class SettingButtonListener implements ActionListener { //TODO
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Object source = actionEvent.getSource();

            if (source == btnSaveName) {
                queue.add(ButtonEnum.SET_USERNAME);
            }

            if (source == btnSavePassword) {
                queue.add(ButtonEnum.SET_PASSWORD);
            }

            if (source == btnClearTime) {
                queue.add(ButtonEnum.SET_CLEARTIME);
            }
        }
    }
}
