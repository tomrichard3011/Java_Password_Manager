package edu.sjsu.Team15.controller;


import edu.sjsu.Team15.utility.DatabaseFunctions;
import edu.sjsu.Team15.utility.ClipboardManager;
import edu.sjsu.Team15.model.DomainInfo;
import edu.sjsu.Team15.model.User;
import edu.sjsu.Team15.utility.Message;
import edu.sjsu.Team15.view.*;
import io.github.novacrypto.SecureCharBuffer;

import java.util.concurrent.LinkedBlockingQueue;

public class MainController {
    User user;
    MainView mainView;
    DomainInfoView domainInfoView;
    DomainInfoListView domainInfoListView;
    LinkedBlockingQueue<Message> queue;

    /**
     * Constructor
     * @param user current user
     * @param mainView main view to link
     * @param queue Thread safe queue
     */
    public MainController(User user, MainView mainView, LinkedBlockingQueue<Message> queue)  {
        this.user = user;
        this.mainView = mainView;
        this.domainInfoView = mainView.domainInfoView;
        this.domainInfoListView = mainView.domainInfoListView;
        this.queue = queue;
    }

    /**
     * Main loop for program
     * Processes data from the queue
     */
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
//                case SET_USERNAME: // TODO EDIT DATABASE ENTRIES
//                    set_username(message);
//                    break;
//                case SET_PASSWORD:
//                    set_password(message);
//                    break;
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
                case EXIT:
                    exit();
                default:
                    throw new IllegalStateException("Unexpected value: " + message.action);
            }
        }
    }

    // HELPER FUNCTIONS

    /**
     * Edits a domain info
     * @param message message with extra data
     */
    private void edit(Message message) {
        DomainInfo domainInfo = message.getDomainInfo();
        new EditDomainInfoView(domainInfo, domainInfoView);
    }

    /**
     * Deletes a domain info
     * @param message message with extra data
     */
    private void delete(Message message) {
        user.getDomainInfoArray().remove(message.getDomainInfo());
        domainInfoListView.updateList();
        DatabaseFunctions.saveDomains(user);

    }

    /**
     * Copies password to clipboard
     * @param message message with extra data
     */
    private void copy(Message message) {
        SecureCharBuffer pass = message.getDomainInfo().getPassword();
        ClipboardManager cbManager = new ClipboardManager(pass, user.getClipboardClearTime());
        new Thread(cbManager).start();
        cbManager.setCharBuffer(new SecureCharBuffer());
    }

    /**
     * Generate a random password for a user
     * @param message message with extra data
     */
    private void generate(Message message) {
        message.getDomainInfo().genNewPassword();
    }

//    private void set_username(Message message) { // TODO set credentials in Database
//        DatabaseFunctions.editUserName(user.getUsername(), user.getMasterKey(), message.getUsername());
//    }
//
//    private void set_password(Message message) { // TODO set credentials in Database
//        DatabaseFunctions.editUserPass(user.getUsername(), user.getMasterKey(), message.getPassword());
//    }

    /**
     * Changes cleartime for a user
     * @param message message with extra data
     */
    private void set_clearTime(Message message) {
        int newClearTime = message.getClearTime();
        user.setClipboardClearTime(newClearTime);
        DatabaseFunctions.editUserClipTime(user.getUsername(), user.getMasterKey(), newClearTime);
    }

    /**
     * Domain Info creation menu
     */
    private void create_domaininfo_menu() {
        new CreateDomainInfoView(queue);
        domainInfoListView.updateList();
    }

    /**
     * bring up settings for a user
     */
    private void settings_menu(){
        new SettingsView(queue);
    }

    /**
     * Add a domain info
     * @param message message with extra data
     */
    private void add_domainInfo(Message message) {
        user.getDomainInfoArray().add(message.getDomainInfo());
        domainInfoListView.updateList();
        DatabaseFunctions.saveDomains(user);
    }

    /**
     * Custom exit procedure
     */
    private void exit() {
        DatabaseFunctions.saveDomains(user);
        System.exit(0);
    }
}