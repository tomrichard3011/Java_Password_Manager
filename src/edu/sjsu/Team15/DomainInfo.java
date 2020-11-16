package edu.sjsu.Team15;

import io.github.novacrypto.SecureCharBuffer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;


public class DomainInfo {
    private final String domain;
    private final String logoPath;
    private Date timeStamp;
    private String username;
    private SecureCharBuffer password;

    public DomainInfo(String domain, String username, SecureCharBuffer password) {
        this.domain = domain;
        this.logoPath = System.getProperty("username.dir") + "/images/" + domain + ".png";
        this.timeStamp = new Date();
        this.username = username;
        // TODO generate password
        if (PasswordChecker.checkPass(password, timeStamp)) {
            this.password = password;
        }

        checkImageDirectory();
        downloadLogo(domain);
    }

    // TODO manage getting and setting
    public String getDomain() {
        return domain;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public SecureCharBuffer getPassword() {
        return password;
    }

    public void setPassword(SecureCharBuffer password) {
        this.password = password;
    }

    // NEW STUFF
    // returns imageIcon ready for JLabel, used as an example
    public ImageIcon getImageIcon() throws IOException {
        BufferedImage image = ImageIO.read(new File(logoPath));
        return new ImageIcon(image);
    }

    // UpLead.com -- need to attribute; 128x128 pixels
    private void downloadLogo(String domainName) {
        BufferedImage image;

        try {
            URL url = new URL("https://logo.uplead.com/" + domainName + ".com"); // define url
            image = ImageIO.read(url); // check if image exists
        }
        catch (Exception e) {
            image = new BufferedImage(128, 128, BufferedImage.TYPE_4BYTE_ABGR);
        }

        try {
            ImageIO.write(image, "png", new File(logoPath)); // download and save image
        }
        catch (Exception e) {
            System.exit(1); //TODO ERROR HANDLING
        }
    }

    // checks for existence of directory, if it doesnt exist, make a new directory
    private static void checkImageDirectory(){
        File file = new File(System.getProperty("username.dir") + "/images");
        if (!file.exists()) {
            if (!file.mkdir()) System.exit(1); //TODO ERROR HANDLING
        }
    }
}
