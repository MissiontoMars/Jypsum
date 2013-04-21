package net.mua.jypsum;

import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;
public class Msg {
    public String headers;
    public String body;    
    private String from;
    private String to;
    private String cc;
    private String hostServer;
    private String userName;
    private static final String CRLF = "\r\n";

    public Msg(String myFrom, String myTo, String myCc, String subject, String text, boolean state, String localsvr) throws IOException { 
        //Init Class Variables
        InetAddress inet = InetAddress.getByName(localsvr);
        hostServer = inet.getHostName();
        userName = System.getProperty("user.name");
        if (state) {
            from = userName + "@" + hostServer;
        } else {
            from = from.trim();
        }
        to = myTo.trim();
        cc = myCc.trim();

        headers = "From:" + from + CRLF;
        headers += "To:" + to + CRLF;
        headers += "Cc:" + cc + CRLF;
        headers += "Subject:" + subject.trim() + CRLF;

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'");
        String dateString = dateFormat.format(new Date());
        headers += "Date: " + dateString + CRLF;
        body = text;
    }
    
    public String getHeaders() { return headers; }
    public String getBody() { return body; }
    public String getFrom() { return from; }
    public String getTo() { return to; }
    public String getCc() { return cc; }
    
    public boolean isValid() {
        int fromAt = from.indexOf('@');
        int toAt = to.indexOf('@');
        int ccAt;
        if (cc.isEmpty())
            ccAt = cc.indexOf('@');
        else
            ccAt = 0;
        if (fromAt < 1 || (from.length() - fromAt) <= 1) {
            System.out.println("Sender address is invalid");
            return false;
        }
        if (toAt < 1 || (to.length() - toAt) <= 1) {
            System.out.println("Recipient address is invalid");
            return false;
        }

        if (cc.isEmpty() && (ccAt < 1 || (cc.length() - ccAt) <= 1)) {
            System.out.println("CC: address is invalid");
            return false;
        }
        
        // Not more than one @
        if (fromAt != from.lastIndexOf('@')) {
            System.out.println("Sender address is invalid");
            return false;
        }
        if (toAt != to.lastIndexOf('@')) {
            System.out.println("Recipient address is invalid");
            return false;
        }	
        if ((cc.isEmpty()) && ccAt != cc.lastIndexOf('@')) {
            System.out.println("CC: address is invalid");
            return false;
        }
        return true;
    }
    
    public String toString() {
        String res;
        res = headers + CRLF;
        res += body;
        return res;
    }
}
