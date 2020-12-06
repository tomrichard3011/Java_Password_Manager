package edu.sjsu.Team15.view;

import edu.sjsu.Team15.model.DomainInfo;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class DomainInfoListView extends JPanel {
    final JList<DomainInfo> list;
    final ArrayList<DomainInfo> domainInfoArrayList; // TODO ADD NEW DOMAIN

    /**
     * Constructor
     * @param domainInfoArrayList arraylist to print to view
     * @param domainInfoView view to print to
     */
    public DomainInfoListView(ArrayList<DomainInfo> domainInfoArrayList, DomainInfoView domainInfoView){
        this.domainInfoArrayList = domainInfoArrayList;

        list = new JList<>();
        list.setListData(domainInfoArrayList.toArray(new DomainInfo[0]));
        list.setFixedCellHeight(20);
        list.setFixedCellWidth(120);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(18);
        list.setSelectedIndex(0);
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 1) {
                    int index = list.locationToIndex(me.getPoint());
                    if (index >= 0) { // TODO MOVE FUNCTIONALITY TO CONTROLLER
                        DomainInfo item = list.getModel().getElementAt(index);
                        domainInfoView.updateDomainInfo(item);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(list);
        this.add(scrollPane);
    }

    /**
     * function to update list data
     */
    public void updateList() {
        list.setListData(domainInfoArrayList.toArray(new DomainInfo[0]));
        this.revalidate();
    }
}