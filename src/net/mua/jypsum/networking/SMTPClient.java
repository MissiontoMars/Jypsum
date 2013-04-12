package net.mua.jypsum.networking;
import java.net.*;
import java.io.*;
import java.util.*;

import net.mua.jypsum.mail.Envelope;

public class SMTPClient {
    private Socket connection;
    private BufferedInputStream fromServer;
    private DataOutputStream toServer;
    private static final int SMTP_PORT = 25;
    private static final String CRLF = "\r\n";
    private boolean isConnected = false;
    /**
     * @param env
     */
    public SMTPClient(Envelope env) throws IOException {
        connection =;
        fromServer =;
        toServer =;
        private String localhost = ;
        sendCommand(); //Handshake
        isConnected = true;
    }
    
    public void send(Envelope myEnv) throws IOException {
        //SMTP commands in order
    }
    
    public void close() {
        isConnected = false;
        try {
            sendCommand(); //close
        } catch (IOException e) {
            System.err.println("Unable to close connection: " + e);
            isConnected = true;
        }
    }
    
    private void sendCommand(String cmd, int expReply) throws IOException {
        //command to server
        //read reply
        //verify server reply code is same as parameter
        //throw exception if not
        
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
