package edu.sjsu.Team15;

import edu.sjsu.Team15.controller.LoginController;
import edu.sjsu.Team15.controller.MainController;
import edu.sjsu.Team15.model.DomainInfo;
import edu.sjsu.Team15.utility.Message;
import edu.sjsu.Team15.model.User;
import edu.sjsu.Team15.view.DomainInfoListView;
import edu.sjsu.Team15.view.DomainInfoView;
import edu.sjsu.Team15.view.LoginView;
import edu.sjsu.Team15.view.MainView;
import io.github.novacrypto.SecureCharBuffer;

import java.util.concurrent.LinkedBlockingQueue;

public class EntryPoint {
    public static void run() {
        // Thread safe queue
        LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>();

        // login view setup
        LoginView loginView = new LoginView(queue);
        LoginController loginController = new LoginController(queue, loginView);
        User user = loginController.run();

        // Main view linking and setup
        DomainInfo firstDomain;
        if (user.getDomainInfoArray().size() == 0) firstDomain = new DomainInfo("", "", new SecureCharBuffer());
        else firstDomain = user.getDomainInfoArray().get(0);

        DomainInfoView domainInfoView = new DomainInfoView(firstDomain, queue);
        DomainInfoListView domainInfoListView = new DomainInfoListView(user.getDomainInfoArray(), domainInfoView);

        MainView frame = new MainView(domainInfoListView, domainInfoView, queue);
        MainController mainController = new MainController(user, frame, queue);
        mainController.run();
    }
}
