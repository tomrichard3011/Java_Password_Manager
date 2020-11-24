package edu.sjsu.Team15;

import io.github.novacrypto.SecureCharBuffer;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class ClipboardManager {
    private int clearTime;
    private boolean linux_system = true;

    public ClipboardManager(int clearTime) {
        this.clearTime = clearTime;
        if (System.getProperty("os.name").startsWith("Windows")) linux_system = false; // Ubuntu potentially loses functionality, so os specific programming yay;
    }

    public void copyToClip(SecureCharBuffer charBuffer) {
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < charBuffer.length(); i++) {
            password.append(charBuffer.get(i));
        }
        
        if (linux_system) {
            linuxCopyToClip(password.toString());
        } else {
            windowsCopyToClip(password.toString());
        }
    }

    protected void setClearTime(int clearTime) {
        this.clearTime = clearTime;
    }

    private void windowsCopyToClip(String password) {
        try {
            StringSelection ss = new StringSelection(password);
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            cb.setContents(ss, null);

            Thread.sleep(clearTime * 1000);

            ss = new StringSelection("");
            cb.setContents(ss, null);
        }
        catch (Exception e) {
            System.exit(-1);
        }
    }

    private void linuxCopyToClip(String password) {
        try {
            Runtime run = Runtime.getRuntime();
            run.exec(new String[]{"sh", "-c", "echo " + password + " | xclip -selection clipboard"});
            Thread.sleep(clearTime * 1000);
            run.exec(new String[]{"sh", "-c", "echo " + "" + " | xclip -selection clipboard"});
        }
        catch (Exception e)
        {
            System.exit(-1);
        }
    }
}