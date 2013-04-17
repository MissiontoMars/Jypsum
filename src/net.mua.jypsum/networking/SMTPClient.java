package net.mua.jypsum.networking;
import java.net.*;
import java.io.*;
import java.util.*;

import net.mua.jypsum.mail.networking;

public class SMTPClient {
    private Socket clientSocket;
    private BufferedReader fromServer;
    private DataOutputStream toServer;
    private static final int SMTP_PORT = 25;
    private static final String CRLF = "\r\n";
    private boolean isConnected = false;
    /**
     * @param env
     */
    public SMTPClient(Envelope env) throws IOException {
        connection = new Socket(env.destHost, SMTP_PORT);
        fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        toServer = new DataOutputStream(clientSocket.getOutputStream());
        private String localhost = InetAddress.getLocalHost().toString();
        sendCommand("HELO", 354); //Handshake
        isConnected = true;
    }
    
    public void send(Envelope myEnv) throws IOException {
        sendCommand("MAIL FROM", myEnv.recipient, 250);
        sendCommand("RCPT TO", 251 || 250);
        sendCommand("DATA", 354);
        sendCommand("");
        this.close();
    }
    
    public void close() {
        isConnected = false;
        try {
            sendCommand("QUIT", 221); //close
        } catch (IOException e) {
            System.err.println("Unable to close connection: " + e);
            isConnected = true;
        }
    }
    
    private void sendCommand(String cmd, String append, String int expReply) throws IOException {
        //command to server
        if (!append.isEmpty) {
            toServer.writeBytes(cmd + " " +  append + CRLF);
        } else {
            toServer.writeBytes(cmd + CRLF);
        }
        //read reply
        int replyCode = parseReply(fromServer.readLine());
        //verify server reply code is same as parameter
        //throw exception if not
        if (replyCode == expCode) {
        } else {
            System.out.println("Rcv code " + replyCode);
            System.out.println("expected " + expCode);
            throw (IOException e);
        }
        
    }
    
    //Sugar for sending commands without user input fields
    //Like HELO
    private void sendCommand(String cmd, String expReply) throws IOException {
        sendCommand(cmd, "", expReply);
    }
    
    private int parseReply(String reply) {
        return Integer.parseInt(reply);
    }
    
    //Destructor
    protected void finalize() throws Throwable {
        if (isConnected) {
            close();
        }
        super.finalize();
    }
}
