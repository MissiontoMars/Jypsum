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
    PrintStream toServer;
    public SMTPDebugger(Envelope env) throws IOException {
        super(env);
        this.clientSocket.close();
        this.fromServer = new BufferedReader(new InputStreamReader(System.in));
        this.toServer = System.out;
        super.sendCommand("HELO", 250);
        this.isConnected = true;
    }
}
