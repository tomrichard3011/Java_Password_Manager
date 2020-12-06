package edu.sjsu.Team15.view;

import edu.sjsu.Team15.utility.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Main window GUI
 */
public class MainView extends JFrame {
	/** Left Side Panel: List View */
    public final DomainInfoListView domainInfoListView;
    /** Right Side Panel: Domain Information */
    public final DomainInfoView domainInfoView;
    /** Add button */
    private final JButton addDomainButton = new JButton("Add Domain");
    /** Settings button */
    private final JButton settingsButton = new JButton("Settings");
    /** Action Listener */
    private final ButtonPress buttonPressListener = new ButtonPress();
    /** Message queue for the whole application */
    private final LinkedBlockingQueue<Message> queue;

    /**
     * Constructor
     * @param domainInfoListView Sub component that displays all domain infos for user
     * @param domainInfoView sub component that display first domain info
     * @param queue thread safe queue to send commands to
     */
    public MainView(DomainInfoListView domainInfoListView, DomainInfoView domainInfoView, LinkedBlockingQueue<Message> queue) {
        this.domainInfoListView = domainInfoListView;
        this.domainInfoView = domainInfoView;
        this.queue = queue;

        // create buttons
        addDomainButton.addActionListener(buttonPressListener);
        settingsButton.addActionListener(buttonPressListener);

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(this.domainInfoListView);
        this.add(this.domainInfoView);
        this.add(addDomainButton);
        this.add(settingsButton);
        this.SpringLayoutSetup(layout);

        this.setPreferredSize(new Dimension(500, 500));
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    /**
     * Spring layout setup
     * @param layout layout for this frame
     */
    private void SpringLayoutSetup(SpringLayout layout) {
        // Domain Info List
        layout.putConstraint(SpringLayout.NORTH, domainInfoListView, 0,
                SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.WEST, domainInfoListView, 10,
                SpringLayout.WEST, this);

        // Domain info view
        layout.putConstraint(SpringLayout.NORTH, domainInfoView, 0,
                SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.EAST, domainInfoView, 0,
                SpringLayout.EAST, this);
        
        // Add Domain Button
        layout.putConstraint(SpringLayout.NORTH, addDomainButton, 10,
                SpringLayout.SOUTH, domainInfoListView);
        layout.putConstraint(SpringLayout.WEST, addDomainButton, 10,
                SpringLayout.WEST, this);

        // Settings button
        layout.putConstraint(SpringLayout.NORTH, settingsButton, 10,
                SpringLayout.SOUTH, addDomainButton);
        layout.putConstraint(SpringLayout.WEST, settingsButton, 10,
                SpringLayout.WEST, this);
    }

    @Override
    public void dispose() {
        Message message = new Message();
        message.action = Message.Action.EXIT;
        queue.add(message);
    }

    private class ButtonPress implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Object source = actionEvent.getSource();
            Message message = new Message();

            if (source == addDomainButton) {
                message.action = Message.Action.CREATE_DOMAININFO_MENU;
                queue.add(message);
            }
            if (source == settingsButton) {
                message.action = Message.Action.SETTINGS_MENU;
                queue.add(message);
            }
        }
    }
}
