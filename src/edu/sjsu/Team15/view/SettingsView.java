package edu.sjsu.Team15.view;
// TODO

import edu.sjsu.Team15.Message;
import io.github.novacrypto.SecureCharBuffer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.LinkedBlockingQueue;

public class SettingsView extends JFrame {
    private final LinkedBlockingQueue<Message> queue;

    private final JPanel contentPane;
//    private final JTextField userName;
    private final JTextField clear;
//    private final JPasswordField pass;
//
//    private final JButton btnSaveName;
//    private final JButton btnSavePassword;
    private final JButton btnClearTime;

    private SettingButtonListener settingButtonListener = new SettingButtonListener();

    private final JFrame frame = this;

    /**
     * Create the frame.
     */
    public SettingsView(LinkedBlockingQueue<Message> queue) {
        this.queue = queue;

        setBounds(100, 100, 450, 200);

        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(new SpringLayout());

//        userName = new JTextField("UserName");
//        contentPane.add(userName);
//        userName.setColumns(10);

        clear = new JTextField();
        clear.setColumns(10);
        contentPane.add(clear);

//        pass = new JPasswordField();
//        pass.setColumns(10);
//        contentPane.add(pass);

//        // SAVE NAME BUTTON
//        btnSaveName = new JButton("Change Name: ");
//        btnSaveName.addActionListener(settingButtonListener);
//        contentPane.add(btnSaveName);
//
//        // SAVE PASSWORD BUTTON
//        btnSavePassword = new JButton("Change Password: ");
//        btnSavePassword.addActionListener(settingButtonListener);
//        contentPane.add(btnSavePassword);

        // CEAR TIME BUTTON
        btnClearTime = new JButton("Clear Time: ");
        btnClearTime.addActionListener(settingButtonListener);
        contentPane.add(btnClearTime);

        SpringLayoutSetup((SpringLayout) contentPane.getLayout());
        // frame setups
        this.setPreferredSize(new Dimension(350, 150));
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
    }

    private void SpringLayoutSetup(SpringLayout layout) {
        // USERNAME LAYOUT
        // user button
//        layout.putConstraint(SpringLayout.NORTH, btnSaveName, 20,
//                SpringLayout.NORTH, contentPane);
//        layout.putConstraint(SpringLayout.EAST, btnSaveName, -10,
//                SpringLayout.WEST, userName);
//        //user label
//        layout.putConstraint(SpringLayout.VERTICAL_CENTER, userName, 0,
//                SpringLayout.VERTICAL_CENTER, btnSaveName);
//        layout.putConstraint(SpringLayout.WEST, userName, 20,
//                SpringLayout.HORIZONTAL_CENTER, this);

//        // PASSWORD LAYOUT
//        // password button
//        layout.putConstraint(SpringLayout.NORTH, btnSavePassword, 20,
//                SpringLayout.SOUTH, btnSaveName);
//        layout.putConstraint(SpringLayout.EAST, btnSavePassword, -10,
//                SpringLayout.WEST, pass);
//        // password label
//        layout.putConstraint(SpringLayout.VERTICAL_CENTER, pass, 0,
//                SpringLayout.VERTICAL_CENTER, btnSavePassword);
//        layout.putConstraint(SpringLayout.WEST, pass, 20,
//                SpringLayout.HORIZONTAL_CENTER, this);

        // CLEAR TIME BUTTON
        // clear time button
        layout.putConstraint(SpringLayout.NORTH, btnClearTime, 20,
                SpringLayout.VERTICAL_CENTER, this);
        layout.putConstraint(SpringLayout.EAST, btnClearTime, -10,
                SpringLayout.WEST, clear);
        // clear time label
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, clear, 0,
                SpringLayout.VERTICAL_CENTER, btnClearTime);
        layout.putConstraint(SpringLayout.WEST, clear, 20,
                SpringLayout.HORIZONTAL_CENTER, this);
    }

    class SettingButtonListener implements ActionListener { //TODO
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Object source = actionEvent.getSource();
            Message message = new Message();

//            if (source == btnSaveName) {
//                message.action = Message.Action.SET_USERNAME;
//                message.setUsername(userName.getText());
//                queue.add(message);
//            }
//
//            if (source == btnSavePassword) {
//                message.action = Message.Action.SET_PASSWORD;
//                SecureCharBuffer charBuffer = new SecureCharBuffer();
//                for (char c : pass.getPassword()) {
//                    charBuffer.append(c);
//                    c = '\0';
//                }
//                message.setPassword(charBuffer);
//                queue.add(message);
//            }

            if (source == btnClearTime) {
                message.action = Message.Action.SET_CLEARTIME;
                message.setClearTime(Integer.parseInt(clear.getText()));
                queue.add(message);
            }

            frame.dispose();
        }
    }
}
