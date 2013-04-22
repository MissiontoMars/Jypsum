package net.mua.jypsum.mail;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * SMTP envelope for one mail message.
 * @author Justin Reeves
 * @author Jussi Kangasharju
 */
public class Envelope {
    /* SMTP-sender of the message (in this case, contents of From-header. */
    public String sender;

    /* SMTP-recipient, or contents of To-header. */
    public String recipient;
    public String cc; /* Person to cc: to */
    public String destHost; /* Target MX-host */
    public InetAddress destAddr; /* Destination Server address */

    /* The actual message */
    public Msg message;

    /* Create the envelope. */
    public Envelope(Msg myMessage, String myServer) throws UnknownHostException {
        /* Get sender, recipient, and Cc (if there). */
	    sender = myMessage.getFrom();
    	recipient = myMessage.getTo();
        cc = myMessage.getCc();
        /* Get message. We must escape the message to make sure that 
        there are no single periods on a line.
        This would mess up sending the mail. */
        message = escapeMessage(myMessage); 
        /* Take the name of the local mailserver and map it into an
        * InetAddress */
        destHost = myServer;
        try {
            destAddr = InetAddress.getByName(destHost);
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: " + destHost);
            System.out.println(e);
            throw e;
        }
        return;
    }
    //Returns for DATA transmission Header and Body w/ escape sequence
    public String dataString(Msg myMsg) {
        String out = myMsg.getHeader() + CLRF;
        out += myMsg.getBody();
        out += CLRF + "." CLRF;
        return out;
    }

    /* Escape the message by doubling all periods at the beginning of
       a line. */
    private Msg escapeMessage(Msg myMessage) {
        String escapedBody = "";
        String token;
        StringTokenizer parser = new StringTokenizer(myMessage.myBody, "\n", true);

        while(parser.hasMoreTokens()) {
            token = parser.nextToken();
            if(token.startsWith(".")) {
                token = "." + token;
            }
            escapedBody += token;
        }
        myMessage.myBody = escapedBody;
        return myMessage;
    }

    /* For printing the envelope. Only for debug. */
    public String toString() {
        String res = "Sender: " + sender + '\n';
        res += "Recipient: " + recipient + '\n';
        res += "MX-host: " + destHost + ", address: " + destAddr + '\n';
        res += "Message:" + '\n';
        res += message.toString();
	
        return res;
    }
}
