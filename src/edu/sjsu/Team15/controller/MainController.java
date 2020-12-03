package edu.sjsu.Team15.controller;


import edu.sjsu.Team15.*;
import edu.sjsu.Team15.model.ClipboardManager;
import edu.sjsu.Team15.model.DomainInfo;
import edu.sjsu.Team15.model.User;
import edu.sjsu.Team15.view.*;
import io.github.novacrypto.SecureCharBuffer;

import java.util.concurrent.LinkedBlockingQueue;

public class MainController {
    User user;
    DomainInfoView domainInfoView;
    DomainInfoListView domainInfoListView;
    LinkedBlockingQueue<Message> queue;

    public MainController(User user, DomainInfoView domainInfoView, DomainInfoListView domainInfoListView, LinkedBlockingQueue<Message> queue)  {
        this.user = user;
        this.domainInfoView = domainInfoView;
        this.domainInfoListView = domainInfoListView;
        this.queue = queue;
    }

    public void run() {
        while (true) {
            Message message = null;
            try{
                message = queue.take(); // thread waits for input
            }
            catch (InterruptedException e) {
                System.exit(1); // TODO error handling
            }

            switch (message.action) {
                case EDIT_DOMAININFO: // TODO EDIT PAGE
                    edit(message);
                    break;
                case DELETE_DOMAININFO:
                    delete(message);
                    break;
                case COPY_PASSWORD:
                    copy(message);
                    break;
                case GENERATE_PASSWORD:
                    generate(message);
                    break;
                case SET_USERNAME: // TODO EDIT DATABASE ENTRIES
                    set_username(message);
                    break;
                case SET_PASSWORD:
                    set_password(message);
                    break;
                case SET_CLEARTIME:
                    set_clearTime(message);
                    break;
                case CREATE_DOMAININFO_MENU:
                    create_domaininfo_menu();
                    break;
                case SETTINGS_MENU:
                    settings_menu();
                    break;
                case ADD_DOMAININFO:
                    add_domainInfo(message);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + message.action);
            }
        }
    }

    // HELPER FUNCTIONS
    private void edit(Message message) {
        System.out.println("Edit");
        DomainInfo domainInfo = message.getDomainInfo();
        new EditDomainInfoView(domainInfo, domainInfoView);
    }

    private void delete(Message message) {
        System.out.println("Delete");
        user.getDomainInfoArray().remove(message.getDomainInfo());
        domainInfoListView.updateList();
    }

    private void copy(Message message) {
        System.out.println("Copy");

        SecureCharBuffer pass = message.getDomainInfo().getPassword();
        ClipboardManager cbManager = new ClipboardManager(pass, user.getClipboardClearTime());
        new Thread(cbManager).start();
        cbManager.setCharBuffer(new SecureCharBuffer());
    }

    private void generate(Message message) {
        System.out.println("Generate");
        message.getDomainInfo().genNewPassword();
    }

    private void set_username(Message message) { // TODO set credentials in Database
        user.setUsername(message.getUsername());
    }

    private void set_password(Message message) { // TODO set credentials in Database
        user.setMasterKey(message.getPassword());
    }

    private void set_clearTime(Message message) {
        int newClearTime = message.getClearTime();
        user.setClipboardClearTime(newClearTime);
    }

    private void create_domaininfo_menu() {
        new CreateDomainInfoView(queue);
        domainInfoListView.updateList();
    }

    private void settings_menu(){ // TODO
        new SettingsView(queue);
    }

    private void add_domainInfo(Message message) { // TODO MAYBE SAVE HERE
        user.getDomainInfoArray().add(message.getDomainInfo());
        domainInfoListView.updateList();
    }
}