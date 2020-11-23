package edu.sjsu.Team15;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class LoginDomain extends JComponent {
    public LoginDomain(){


        JList list;
        //creating the list of websites
        String[] domain = {"www.google.com", "www.facebook.com", "www.youtube.com", "www.gmail.com",
                "one.sjsu.edu", "www.slack.com", "www.github.com", "www.linkedin.com", "www.outlook.com"};
        JFrame f1 = new JFrame(); //creating frame
        list = new JList(domain); //setting list to the websites listed in domain lise
        list.setFixedCellHeight(20);
        list.setFixedCellWidth(120);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setVisibleRowCount(5);
        list.setSelectedIndex(0);
        f1.add(new JScrollPane(list));
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 1) {
                    JList target = (JList) me.getSource();
                    int index = target.locationToIndex(me.getPoint());
                    if (index >= 0) {
                        Object item = target.getModel().getElementAt(index);
                        System.out.println("You selected: " + item.toString());
                    }
                }
            }
        });
        //setting the frame
        f1.setLayout(new FlowLayout());
        f1.setSize(350,150);
        f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f1.setVisible(true);

    }

}
