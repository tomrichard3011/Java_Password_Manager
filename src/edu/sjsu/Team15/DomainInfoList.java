package edu.sjsu.Team15;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class DomainInfoList extends JPanel {
    final JList<DomainInfo> list;
    final ArrayList<DomainInfo> domainInfoArrayList; // TODO ADD NEW DOMAIN

    public DomainInfoList(ArrayList<DomainInfo> domainInfoArrayList, DomainInfoView domainInfoView){
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
                    JList<DomainInfo> target = (JList<DomainInfo>) me.getSource();
                    int index = target.locationToIndex(me.getPoint());
                    if (index >= 0) {
                        DomainInfo item = target.getModel().getElementAt(index);
                        domainInfoView.updateDomainInfo(item);
                    }
                }
            }
        });


        JScrollPane scrollPane = new JScrollPane(list);
        this.add(scrollPane);
    }

    public void updateList() {
        list.setListData(domainInfoArrayList.toArray(new DomainInfo[0]));
        this.revalidate();
    }
}