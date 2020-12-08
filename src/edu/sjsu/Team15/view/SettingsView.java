package edu.sjsu.Team15.view;

import edu.sjsu.Team15.utility.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * GUI for when user needs to change settings
 */
public class SettingsView extends JFrame {
	/** The message queue for the entire application */
    private final LinkedBlockingQueue<Message> queue;
    /** The window */
    private final JPanel contentPane;
    /** Text input */
    private final JTextField clear;
    /** Button for setting the clear time */
    private final JButton btnClearTime;
    /** Action Listener */
    private SettingButtonListener settingButtonListener = new SettingButtonListener();
    /** The frame */
    private final JFrame frame = this;

    /**
     * Constructor
     * @param queue thread safe queue to send commands
     */
    public SettingsView(LinkedBlockingQueue<Message> queue) {
        this.queue = queue;

        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(new SpringLayout());

        clear = new JTextField();
        clear.setColumns(10);
        contentPane.add(clear);

        // CLEAR TIME BUTTON
        btnClearTime = new JButton("Clear Time: ");
        btnClearTime.addActionListener(settingButtonListener);
        contentPane.add(btnClearTime);

        SpringLayoutSetup((SpringLayout) contentPane.getLayout());
        // frame setups
        this.setPreferredSize(new Dimension(350, 150));
        this.setResizable(false);
        this.setTitle("Account Settings");
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);
    }

    /**
     * Spring layout setup
     * @param layout layout for this frame
     */
    private void SpringLayoutSetup(SpringLayout layout) {
        // clear time button
        layout.putConstraint(SpringLayout.SOUTH, btnClearTime, 0,
                SpringLayout.VERTICAL_CENTER, this);
        layout.putConstraint(SpringLayout.EAST, btnClearTime, -10,
                SpringLayout.WEST, clear);
        // clear time label
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, clear, 0,
                SpringLayout.VERTICAL_CENTER, btnClearTime);
        layout.putConstraint(SpringLayout.WEST, clear, 10,
                SpringLayout.HORIZONTAL_CENTER, this);
    }

    /**
     * Action button listener for all buttons
     */
    class SettingButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Object source = actionEvent.getSource();
            Message message = new Message();

            if (source == btnClearTime) {
                message.action = Message.Action.SET_CLEARTIME;
                message.setClearTime(Integer.parseInt(clear.getText()));
                queue.add(message);
            }

            frame.dispose();
        }
    }
}
