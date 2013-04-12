package net.mua.jypsum.mail;

import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;
public class Msg {
    public String myHeaders;
    public String myBody;
    
    private String myFrom;
    private String myTo;
    private String myCc;
    private String myHostServer;
    private String myUserName;
    private static final String CRLF = "\r\n";

    public Msg(String from, String to, String cc, String subject, String text, boolean state, String localsvr) throws IOException { 
        InetAddress inet = InetAddress.getByName(localsvr);
        myHostServer = inet.getHostName();
        myUserName = System.getProperty("user.name");
        if (state) {
            myFrom = myUserName + "@" + myHostServer;
        } else {
            myFrom = from.trim();
        }
        myTo = to.trim();
        myCc = cc.trim();

        myHeaders = "From:" + myFrom + CRLF;
        myHeaders += "To:" + myTo + CRLF;
        myHeaders += "Cc:" + myCc + CRLF;
        myHeaders += "Subject:" + subject.trim() + CRLF;

        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'");
        String dateString = format.format(new Date());
        myHeaders += "Date: " + dateString + CRLF;
        myBody = text;
    }
    public String getFrom() { return myFrom; }
    public String getTo() { return myTo; }
    public String getCc() { return myCc; }

    public boolean isValid() {
        int fromAt = myFrom.indexOf('@');
        int toAt = myTo.indexOf('@');
        int ccAt;
        if (myCc.isEmpty())
            ccAt = myCc.indexOf('@');
        else
            ccAt = 0;
        if (fromAt < 1 || (myFrom.length() - fromAt) <= 1) {
            System.out.println("Sender address is invalid");
            return false;
        }
        if (toAt < 1 || (myTo.length() - toAt) <= 1) {
            System.out.println("Recipient address is invalid");
            return false;
        }

        if (myCc.isEmpty() && (ccAt < 1 || (myCc.length() - ccAt) <= 1)) {
            System.out.println("CC: address is invalid");
            return false;
        }
        
        // Not more than one @
        if (fromAt != myFrom.lastIndexOf('@')) {
            System.out.println("Sender address is invalid");
            return false;
        }
        if (toAt != myTo.lastIndexOf('@')) {
            System.out.println("Recipient address is invalid");
            return false;
        }	
        if ((myCc.isEmpty()) && ccAt != myCc.lastIndexOf('@')) {
            System.out.println("CC: address is invalid");
            return false;
        }
        return true;
    }
    
    public String toString() {
        String res;
        res = myHeaders + CRLF;
        res += myBody;
        return res;
    }
}
