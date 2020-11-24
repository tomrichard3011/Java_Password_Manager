package edu.sjsu.Team15;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    DomainInfoList domainInfoList;
    DomainInfoView domainInfoView;

    public MainView(DomainInfoList domainInfoList, DomainInfoView domainInfoView) {
        this.domainInfoList = domainInfoList;
        this.domainInfoView = domainInfoView;


        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(this.domainInfoList);
        this.add(this.domainInfoView);
        this.SpringLayoutSetup(layout);

        this.setPreferredSize(new Dimension(500, 450));
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    private void SpringLayoutSetup(SpringLayout layout) {
        layout.putConstraint(SpringLayout.NORTH, domainInfoList, 0,
                SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.WEST, domainInfoList, 0,
                SpringLayout.WEST, this);


        layout.putConstraint(SpringLayout.NORTH, domainInfoView, 0,
                SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.EAST, domainInfoView, 0,
                SpringLayout.EAST, this);
    }
}
