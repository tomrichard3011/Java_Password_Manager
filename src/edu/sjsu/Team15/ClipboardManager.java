package edu.sjsu.Team15;

import io.github.novacrypto.SecureCharBuffer;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class ClipboardManager { // TODO multithread me
    private int clearTime;
    private OS os;

    public ClipboardManager(int clearTime) {
        this.clearTime = clearTime;
        if (System.getProperty("os.name").startsWith("Linux")) os = OS.LINUX;
        else if (System.getProperty("os.name").startsWith("Windows")) os = OS.WINDOWS;
        else os = OS.MAC;
    }

    public void copyToClip(SecureCharBuffer charBuffer) {
        StringBuilder password = new StringBuilder();
        password.append(charBuffer.subSequence(0, charBuffer.length()));

        switch (os) {
            case LINUX:
                linuxCopyToClip(password.toString());
            case WINDOWS:
                windowsCopyToClip(password.toString());
            case MAC:
                macCopyToClip(password.toString());

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
            run.exec(new String[]{"sh", "-c", "echo '" + password + "' | xclip -selection clipboard"});
            Thread.sleep(clearTime * 1000);
            run.exec(new String[]{"sh", "-c", "echo " + "" + " | xclip -selection clipboard"});
        }
        catch (Exception e)
        {
            System.exit(1);
        }
    }

    private void macCopyToClip(String password) { // UNTESTED
        try {
            Runtime run = Runtime.getRuntime();
            run.exec(new String[]{"sh", "-c", "echo '" + password + "' | pbcopy"});
            Thread.sleep(clearTime * 1000);
            run.exec(new String[]{"sh", "-c", "echo " + "" + " | pbcopy"});
        }
        catch (Exception e)
        {
            System.exit(-1);
        }
    }

    private enum OS {
        LINUX,
        WINDOWS,
        MAC
    }
}