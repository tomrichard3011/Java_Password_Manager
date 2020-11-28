package edu.sjsu.Team15;

import io.github.novacrypto.SecureCharBuffer;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class ClipboardManager implements Runnable { // TODO multithread this
    private int clearTime;
    private SecureCharBuffer charBuffer;

    public ClipboardManager(SecureCharBuffer charBuffer, int clearTime) {
        this.clearTime = clearTime;
        this.charBuffer = charBuffer;
    }

    public static void copyToClip(SecureCharBuffer charBuffer, int clearTime) {
        StringBuilder password = new StringBuilder();
        password.append(charBuffer.toStringAble());

        switch (getOSType()) {
            case LINUX:
                linuxCopyToClip(password.toString(), clearTime);
            case WINDOWS:
                windowsCopyToClip(password.toString(), clearTime);
            case MAC:
                macCopyToClip(password.toString(), clearTime);

        }
    }

    private static OS getOSType(){
        if (System.getProperty("os.name").startsWith("Linux")) return OS.LINUX;
        else if (System.getProperty("os.name").startsWith("Windows")) return OS.WINDOWS;
        else return OS.MAC;
    }

    public void setCharBuffer(SecureCharBuffer charBuffer) {
        this.charBuffer = charBuffer;
    }

    public void setClearTime(int clearTime) {
        this.clearTime = clearTime;
    }

    private static void windowsCopyToClip(String password, int clearTime) {
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

    private static void linuxCopyToClip(String password, int clearTime) {
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

    private static void macCopyToClip(String password, int clearTime) { // UNTESTED
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

    @Override
    public void run() {
        copyToClip(this.charBuffer, this.clearTime);
    }

    private enum OS {
        LINUX,
        WINDOWS,
        MAC
    }
}