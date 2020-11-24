package edu.sjsu.Team15;


import java.util.concurrent.LinkedBlockingQueue;

public class MainController {
    User user;
    DomainInfoView domainInfoView;
    LinkedBlockingQueue<ButtonEnum> queue;

    public MainController(User user, DomainInfoView domainInfoView, LinkedBlockingQueue<ButtonEnum> queue)  {
        this.user = user;
        this.domainInfoView = domainInfoView;
        this.queue = queue;
    }

    public void run() {
        while (true) {
            ButtonEnum buttonSource = null;
            try{
                buttonSource = queue.take(); // thread waits for input
            }
            catch (InterruptedException e) {
                System.exit(1); // TODO error handling
            }

            switch (buttonSource) {
                case EDIT_DOMAININFO:
                    break;
                case DELETE_DOMAININFO:
                    break;
                case COPY_PASSWORD:
                    break;
                case GENERATE_PASSWORD:
                    break;
                case LOGIN:
                    break;
                case NEW_USER:
                    break;
                case SET_USERNAME:
                    break;
                case SET_PASSWORD:
                    break;
                case SET_CLEARTIME:
                    break;
            }
        }
    }

    public void updateDomainInfo(DomainInfo domainInfo) {
        domainInfoView.updateDomainInfo(domainInfo);
    }


    private void edit() {
        System.out.println("Edit");
    }

    private void delete() {
        System.out.println("Delete");
    }

    private void copy() {
        System.out.println("Copy");
    }

    private void generate() {
        System.out.println("Generate");
    }
}