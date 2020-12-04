package JUnit4Tests;

import edu.sjsu.Team15.*;
import edu.sjsu.Team15.controller.MainController;
import edu.sjsu.Team15.model.DomainInfo;
import edu.sjsu.Team15.model.PasswordGenerator;
import edu.sjsu.Team15.model.User;
import edu.sjsu.Team15.view.DomainInfoListView;
import edu.sjsu.Team15.view.DomainInfoView;
import edu.sjsu.Team15.view.MainView;
import io.github.novacrypto.SecureCharBuffer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class ViewTest {
    public static void main(String[] args) {
        SecureCharBuffer facebookpass = new SecureCharBuffer();
        facebookpass.append("Facebook password123!@#");

        DomainInfo facebook = new DomainInfo("facebook", "john", facebookpass);
        facebook.setPassword(facebookpass);
        DomainInfo instagram = new DomainInfo("instagram", "jill", PasswordGenerator.generatePassword());
        DomainInfo linkedin = new DomainInfo("linkedin", "jack", PasswordGenerator.generatePassword());

        ArrayList<DomainInfo> domainInfoArrayList = new ArrayList<>();
        domainInfoArrayList.add(facebook);
        domainInfoArrayList.add(instagram);
        domainInfoArrayList.add(linkedin);

        // stuff needed for initialization
        User user = new User(5, domainInfoArrayList, "Jake", new SecureCharBuffer());
        LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>();




        // LINKING EVERYTHING TOGETHER
        DomainInfoView domainInfoView = new DomainInfoView(user.getDomainInfoArray().get(0), queue);
        DomainInfoListView domainInfoListView = new DomainInfoListView(user.getDomainInfoArray(), domainInfoView); // link JList to domaininfoview


        JFrame frame = new MainView(domainInfoListView, domainInfoView, queue);
        MainController mainController = new MainController(user, domainInfoView, domainInfoListView, queue);
        mainController.run();
    }
}
