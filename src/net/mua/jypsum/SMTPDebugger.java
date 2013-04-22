package net.mua.jypsum;
import java.net.*;
import java.io.*;
import java.util.*;

public class SMTPDebugger extends SMTPClient {
    /*private static final int SMTP_PORT = 25;
    private static final String CRLF = "\r\n";
    private BufferedReader fromServer;
    private DataOutputStream toServer;
    private boolean isConnnected = false;
    */
    InputStream toServer;
    public SMTPDebugger(Envelope env) throws IOException {
        super(env);
        this.clientSocket.close();
        this.fromServer = new BufferedReader(new InputStreamReader(System.in));
        this.toServer = System.in;
        this.isConnected = true;
    }

    @Override
    protected void sendCommand(String cmd, String append, int expReply) throws IOException {
        //command to server
        if (!append.isEmpty()) {
            System.out.print(cmd + " " + append + CRLF);
        } else {
            System.out.print(cmd + CRLF);
        }
        //read reply
        int replyCode = super.parseReply(fromServer.readLine());
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

}
 
