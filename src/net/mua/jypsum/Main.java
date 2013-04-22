package net.mua.jypsum;
import java.io.*;
import java.util.*;
import java.net.*;

public class Main {
        @SuppressWarnings("unused")
        public Main () {
            UI gui = new UI();
        }

        public Main (String foo) {
        }

        @SuppressWarnings("unused")
        public static void main (String[] args) throws Exception {
            if (args.length > 0) {
                if (args[0].equals("--debug") || args[0].equals("-d")) {
                    Main debugJypsum = new Main("DEBUG");
                    debugJypsum.debug(); //Do options
                }
            } else {
                Main myJypsum = new Main();
            }
        }
        
        private String prompt(String what, Scanner sc) throws IOException {
            System.out.println(what + "?");
            what = sc.nextLine();
            return what;
        }

        public void debug() throws Exception {
            Scanner s = new Scanner(System.in);
            System.out.println("Sending mail");
            String localsvr;
            Envelope envelope;  // TRY IT HERE 
            /* Check that we have the local mailserver */
            try {
                System.out.println("Writing a letter");
                Msg mailMessage = new Msg(prompt("FROM", s), 
                        prompt("TO", s),
                        prompt("CC, seperated by commas", s),
                        prompt("SUBJECT", s), 
                        prompt("BODY", s),
                        false,
                        localsvr = prompt("LOCAL MAIL SERVER", s));
                /* Check that the message is valid, i.e., sender and
                recipient addresses look ok. */
                if(!mailMessage.isValid()) {
                    System.out.println("NOT VALID");
                    return;
                }

                /* Create the envelope, open the connection and try to send
                the message. */
                try {
                    System.out.println("Stuffing an envelope");
                    envelope = new Envelope(mailMessage, 
                            localsvr);
                } catch (UnknownHostException e) {
                    /* If there is an error, do not go further */
                    System.out.println("WRONG");
                    return;
                }

            }
            catch(IOException e) {
                System.out.println("IO WRONG");
                throw e;
            }

            try {
                System.out.println("Debugging");
                SMTPClient connection = new SMTPDebugger(envelope);
                connection.send(envelope);
                connection.close();
            } catch (IOException error) {
                System.out.println("Sending failed: " + error);
                return;
            }
            System.out.println("Mail sent succesfully!");
            s.close();
        }
}

