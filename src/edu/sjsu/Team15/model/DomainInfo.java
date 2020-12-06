package edu.sjsu.Team15.model;

import edu.sjsu.Team15.utility.PasswordGenerator;
import io.github.novacrypto.SecureCharBuffer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Date;


public class DomainInfo {
	/** The website's name (i.e. domain) */
    private final String domain;
    /** The file path to the image of the website's logo */
    private final String logoPath;
    /** The last time the password was changed (Unused?) */
    private Date timeStamp;
    /** The website username */
    private String username;
    /** The website password */
    private SecureCharBuffer password;
    /** The file separator, allows cross-platform usage */
    private String fileSep = System.getProperty("file.separator");

    /**
     * Create DomainInfo object
     * @param domain The website name
     * @param username The username for an account on the website
     * @param password The password for an account on the website
     */
    public DomainInfo(String domain, String username, SecureCharBuffer password) {
        this.domain = domain;
        this.logoPath = System.getProperty("user.dir") + fileSep + "images" + fileSep + domain + ".png";
        this.timeStamp = new Date();
        this.username = username;
        this.password = password; // TODO PASSWORD CHECK

        checkImageDirectory();
        downloadLogo(domain);
    }

    /** @return String The website's name */
    public String getDomain() {
        return domain;
    }

    /** @return String The path to the image */
    public String getLogoPath() {
        return logoPath;
    }

    /** (Currently unused) 
     * @return Date The last time the domain was updated */
    public Date getTimeStamp() {
        return timeStamp;
    }

    /**
     * Set the timestamp for the website credentials
     * @param timeStamp The lastest time
     */
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    /** @return String The username for the website */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username for a website
     * @param username The username for the website
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the password for the website account
     * @return SecureCharBuffer Object containing the password
     */
    public SecureCharBuffer getPassword() {
        return password;
    }

    /**
     * Set the password for the website account
     * @param password The password being set
     */
    public void setPassword(SecureCharBuffer password) {
        this.password = password;
    }

    /** Generate a new password and place it in the domain */
    public void genNewPassword() {
        this.password = PasswordGenerator.generatePassword();
    }

    // NEW STUFF
    // UpLead.com -- need to attribute; 128x128 pixels
    /**
     * Download a website's logo for use within the program
     * Connects to an external logo database for retrieving logos
     * and saving them in the application for further use. Not all
     * logos may be available, especially for smaller websites
     * @param domainName The website we're trying to connect to
     */
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
            File logoLocation = new File(logoPath);
            if (!logoLocation.exists()) { // check for location of logo, if it doesn't exist write it
                ImageIO.write(image, "png", logoLocation); // download and save image
            }
        }
        catch (Exception e) {
            System.exit(1); //TODO ERROR HANDLING
        }
    }

    /** Checks for existence of directory, if it doesn't exist, make a new directory */
    private static void checkImageDirectory(){
        File file = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "images");
        if (!file.exists()) {
            if (!file.mkdir()) {
                System.exit(1); //TODO ERROR FILE DIRECTORY COULD NOT BE CREATED
            }
        }
    }

    @Override
    public String toString() {
        return domain;
    }
}
