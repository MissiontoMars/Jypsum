package net.mua.jypsum;
import java.net.*;
import java.io.*;
import java.util.*;

public class SMTPClient {
    protected Socket clientSocket;
    protected BufferedReader fromServer;
    protected DataOutputStream toServer;
    protected static final int SMTP_PORT = 25;
    protected static final String CRLF = "\r\n";
    protected boolean isConnected = false;
    /**
     * @param env
     */
    public SMTPClient(Envelope env) throws IOException {
        clientSocket = new Socket(env.destHost, SMTP_PORT);
        fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        toServer = new DataOutputStream(clientSocket.getOutputStream());
        
        String localhost = InetAddress.getLocalHost().toString();
        
        sendCommand("HELO", 250); //Handshake
        System.out.println("HELO");
        isConnected = true;
    }
    
    public void send(Envelope myEnv) throws IOException {
        sendCommand("MAIL FROM", myEnv.sender, 250);
        sendCommand("RCPT TO", myEnv.recipient, 250);
        sendCommand("DATA", 354);
        sendCommand(myEnv.eLetter, 250);
        //sendCommand(CLRF + "." + CLRF, 250);
        this.close();
    }
    
    public void close() {
        isConnected = false;
        try {
            sendCommand("QUIT", 221); //close
        } catch (IOException e) {
            System.err.println("Unable to close clientSocket: " + e);
            isConnected = true;
        }
    }
    
    protected void sendCommand(String cmd, String append, int expReply) throws IOException {
        //command to server
        if (!append.isEmpty()) {
            System.out.print("\n" + cmd + " " + append);
            toServer.writeBytes(cmd + " " + append + CRLF);
        } else {
            System.out.print("\n" + cmd);
            toServer.writeBytes(cmd + CRLF);
        }
        //read reply
        int replyCode = parseReply(fromServer.readLine());
        //verify server reply code is same as parameter
        //throw exception if not
        if (replyCode == expReply) {
            System.out.println("Received code: " + replyCode);
        } else {
            System.out.println("Rcv code: " + replyCode);
            System.out.println("expected: " + expReply);
            throw new IOException();
        }
        
    }
    
    //Sugar for sending commands without user input fields
    //Like HELO
    protected void sendCommand(String cmd, int expReply) throws IOException {
        sendCommand(cmd, "", expReply);
    }
    
    protected int parseReply(String reply) {
        String code = new StringTokenizer(reply, "").nextToken();
        return Integer.parseInt(code);
    }
    
    //Destructor
    protected void finalize() throws Throwable {
        if (isConnected) {
            close();
        }
        super.finalize();
    }
}
