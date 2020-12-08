package edu.sjsu.Team15.view;

import edu.sjsu.Team15.model.DomainInfo;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Creates left side window for list of domains
 */
public class DomainInfoListView extends JPanel {
	/** GUI List of Domains and their associated information */
    final JList<DomainInfo> list;
    /** The internal ArrayList of all the domains */
    final ArrayList<DomainInfo> domainInfoArrayList; // TODO ADD NEW DOMAIN
    /** List width */
    int columnWidth;

    /**
     * Constructor
     * @param domainInfoArrayList arraylist to print to view
     * @param domainInfoView view to print to
     */
    public DomainInfoListView(ArrayList<DomainInfo> domainInfoArrayList, DomainInfoView domainInfoView){
        this.domainInfoArrayList = domainInfoArrayList;
        this.columnWidth = 120;

        list = new JList<>();
        list.setListData(domainInfoArrayList.toArray(new DomainInfo[0]));
        list.setFixedCellHeight(-1); // 20
        list.setFixedCellWidth(columnWidth);
        list.setMinimumSize(new Dimension(120, 360));
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
    
    public void updateView(int i) {
    	if(i >= 120) {
    		list.setFixedCellWidth(i);
    		list.revalidate();
    		list.repaint();
    	}
    }
}