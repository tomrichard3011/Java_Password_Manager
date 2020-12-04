package JUnit4Tests;

import edu.sjsu.Team15.*;
import edu.sjsu.Team15.controller.LoginController;
import edu.sjsu.Team15.controller.MainController;
import edu.sjsu.Team15.model.DomainInfo;
import edu.sjsu.Team15.utility.PasswordGenerator;
import edu.sjsu.Team15.model.User;
import edu.sjsu.Team15.view.DomainInfoListView;
import edu.sjsu.Team15.view.DomainInfoView;
import edu.sjsu.Team15.view.LoginView;
import edu.sjsu.Team15.view.MainView;
import io.github.novacrypto.SecureCharBuffer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class ViewTest {
    public static void main(String[] args) {
        // stuff needed for initialization
        LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>();

        // login view setup
        LoginView loginView = new LoginView(queue);
        LoginController loginController = new LoginController(queue, loginView);
        User user = loginController.run();

        for (DomainInfo d : user.getDomainInfoArray()) {
            System.out.println();
        }

        // LINKING EVERYTHING TOGETHER
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
