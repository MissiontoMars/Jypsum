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
        String localhost = InetAddress.getLocalHost().getCanonicalHostName();
        System.out.println("local hostname: " + localhost);
        String firstLine = fromServer.readLine();
        System.out.println(firstLine);
        if (220 != parseReply(firstLine)) {
            System.out.println("not ready");
            throw new IOException();
        }

        System.out.println("Try 1");
        if (501 == sendCommand("HELO", 250)) {
            System.out.println("Try 2");
            if (501 == sendCommand("HELO", localhost, 250)) {
                System.out.println("Try 3");
                sendCommand("HELO", "niflheimr.world.tree", 250); //Handshake
            }
        }
        //System.out.println("Sending HELO\n");
        isConnected = true;
    }
    
    public void send(Envelope myEnv) throws IOException {
        sendCommand("MAIL FROM:", myEnv.sender, 250);
        sendCommand("RCPT TO:", myEnv.recipient, 250);
        sendCommand("DATA", 354);
        sendCommand(myEnv.eLetter, 250);
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
    
    protected int sendCommand(String cmd, String append, int expReply) throws IOException {
        //command to server
        if (!append.equals("")) {
            System.out.println(cmd + " " + append);
            toServer.writeBytes(cmd + " " + append + CRLF);
        } else {
            System.out.println(cmd);
            toServer.writeBytes(cmd + CRLF);
        }
        //read reply
        String output = fromServer.readLine();
        System.out.println(output + "\n");
        int replyCode = parseReply(output);
        //verify server reply code is same as parameter
        //throw exception if not
        if (replyCode == expReply) {
            System.out.println("Received code: " + replyCode);
        } else {
            System.out.println("Rcv code: " + replyCode);
            System.out.println("expected: " + expReply);
            //throw new IOException();
        }
        return replyCode;
    }
    
    //Sugar for sending commands without user input fields
    //Like HELO
    protected int sendCommand(String cmd, int expReply) throws IOException {
        return sendCommand(cmd, "", expReply);
    }
    
    protected int parseReply(String reply) {
        String code = new StringTokenizer(reply, " ").nextToken();
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
