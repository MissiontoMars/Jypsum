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
    public String hostServer;
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
            from = myFrom.trim();
        }
        to = myTo.trim();
        cc = myCc.trim();

        headers = "From: " + from + CRLF;
        headers += "To: " + to + CRLF;
        headers += "Cc: " + cc + CRLF;
        headers += "Subject: " + subject.trim() + CRLF;

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'");
        String dateString = dateFormat.format(new Date());
        headers += "Date: " + dateString + CRLF;
        body = text;
    }
    
    public String getHeaders() { return headers; }
    public String getBody() { return body; }
    public String getFrom() { return "<" + from + ">"; }
    public String getTo() { return "<" + to + ">"; }
    public String getCc() { return cc; }
    
    //TODO Replace with regex validator
    public boolean isValid() {
        int fromAt = from.indexOf('@');
        int toAt = to.indexOf('@');
        if (fromAt < 1 || (from.length() - fromAt) <= 1) {
            System.out.println("Sender address is invalid");
            return false;
        }
        if (toAt < 1 || (to.length() - toAt) <= 1) {
            System.out.println("Recipient address is invalid");
            return false;
        }
        //FAIL TO FAIL CC
        //now checks each cc as individual mail
        /*if (!cc.isEmpty() && (ccAt < 1 || (cc.length() - ccAt) <= 1)) {
            System.out.println("CC: adidress is invalid");
            System.out.println("Case1: cc is " + cc);
            return false;
        }*/
        
        // Not more than one @
        if (fromAt != from.lastIndexOf('@')) {
            System.out.println("Sender address is invalid");
            return false;
        }
        if (toAt != to.lastIndexOf('@')) {
            System.out.println("Recipient address is invalid");
            return false;
        }	
        /*if ((!cc.isEmpty()) && ccAt != cc.lastIndexOf('@')) {
            System.out.println("CC: addr ess is invalid");
            System.out.println("Case2: cc is " + cc);
            return false;
        }*/
        return true;
    }
    
    public String toString() {
        String res;
        res = headers + CRLF;
        res += body;
        res += CRLF + "." + CRLF;
        res = headers + CRLF;
        res += body;
        return res;
    }
}
