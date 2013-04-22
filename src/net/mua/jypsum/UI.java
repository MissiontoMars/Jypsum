package net.mua.jypsum;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

public class UI extends Frame {
	private Button btSend = new Button("Send");
	private Button btClear = new Button("Clear");
	private Button btQuit = new Button("Quit");

	private Label serverLabel = new Label("Local mailserver:");
	private TextField serverField = new TextField("",40);

	private Label fromLabel = new Label("From:");
	private TextField fromField = new TextField("",40);

	private Label toLabel = new Label("To:");
	private TextField toField = new TextField("",40);

	private Label ccLabel = new Label("cc:");
	private TextField ccField = new TextField("",40);

	private Checkbox usrName = new Checkbox("Auto Generate?");

	private Label subjectLabel = new Label("Subject:");
	private TextField subjectField = new TextField("",40);

	private Label messageLabel = new Label("Message:");
	private TextArea messageText = new TextArea(10,40);

	public UI() {
        //Call Frame's contructor
        super("Jypsum User Agent");

		Panel serverPanel = new Panel(new BorderLayout());
		Panel fromPanel = new Panel(new BorderLayout());
		Panel toPanel = new Panel(new BorderLayout());

		// add ccPanel and usrPanel to GUI
		Panel ccPanel = new Panel(new BorderLayout());
		Panel usrPanel = new Panel(new BorderLayout());

		Panel subjectPanel = new Panel(new BorderLayout());
		Panel messagePanel = new Panel(new BorderLayout());
		serverPanel.add(serverLabel, BorderLayout.WEST);
		serverPanel.add(serverField, BorderLayout.CENTER);
		fromPanel.add(fromLabel, BorderLayout.WEST);
		fromPanel.add(fromField, BorderLayout.CENTER);
		toPanel.add(toLabel, BorderLayout.WEST);
		toPanel.add(toField, BorderLayout.CENTER);

		// Add ccLabel, ccField, & usrName to Panels
		ccPanel.add(ccLabel, BorderLayout.WEST);
		ccPanel.add(ccField, BorderLayout.CENTER);
		usrPanel.add(usrName, BorderLayout.WEST);

		subjectPanel.add(subjectLabel, BorderLayout.WEST);
		subjectPanel.add(subjectField, BorderLayout.CENTER);
		messagePanel.add(messageLabel, BorderLayout.NORTH); 
		messagePanel.add(messageText, BorderLayout.CENTER);
		Panel fieldPanel = new Panel(new GridLayout(0, 1));
		fieldPanel.add(serverPanel);
		fieldPanel.add(fromPanel);
		fieldPanel.add(toPanel);
		fieldPanel.add(ccPanel);
		fieldPanel.add(usrPanel);
		fieldPanel.add(subjectPanel);

		/* Create a panel for the buttons and add listeners to the
        buttons. */
		Panel buttonPanel = new Panel(new GridLayout(1, 0));
		btSend.addActionListener(new SendListener());
		btClear.addActionListener(new ClearListener());
		btQuit.addActionListener(new QuitListener());
		buttonPanel.add(btSend);
		buttonPanel.add(btClear);
		buttonPanel.add(btQuit);

		/* Add, pack, and show. */
		add(fieldPanel, BorderLayout.NORTH);
		add(messagePanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		pack();
		show();
	}

	/* Handler for the Send-button. */
	class SendListener implements ActionListener {
		public void actionPerformed(ActionEvent event) { 
			System.out.println("Sending mail");
			Envelope envelope;  // TRY IT HERE 
			/* Check that we have the local mailserver */
			if ((serverField.getText()).equals("")) {
				System.out.println("Need name of local mailserver!");
				return;
			}

			/* Check that we have the sender and recipient.
			 * Also, still send if fromField is blank IFF usrName checkbox
			 * is checked.
			 */ 
			if((fromField.getText()).equals("") && !usrName.getState()) {
				System.out.println("Need sender!");
				return;
			}
			if((toField.getText()).equals("")) {
				System.out.println("Need recipient!");
				return;
			}

			if(usrName.getState())
				System.out.println("Checked.");
			/* Create the message */

			// Modified the message format to include Extra fields
			try {
				System.out.println("Writing a letter");
				Msg mailMessage = new Msg(fromField.getText(), 
						toField.getText(),
						ccField.getText(),
						subjectField.getText(), 
						messageText.getText(),
						usrName.getState(),
						serverField.getText());

				/* Check that the message is valid, i.e., sender and
                recipient addresses look ok. */
				if(!mailMessage.isValid()) {
					return;
				}

				/* Create the envelope, open the connection and try to send
                the message. */
				try {
				    System.out.println("Stuffing an envelope");
					envelope = new Envelope(mailMessage, 
							serverField.getText());
				} catch (UnknownHostException e) {
					/* If there is an error, do not go further */
					return;
				}

			}
			catch(IOException e) {
				return;
			}

			try {
			    System.out.println("Opening Socket...");
                SMTPClient connection = new SMTPClient(envelope);
				
				connection.send(envelope);
				connection.close();
			} catch (IOException error) {
				System.out.println("Sending failed: " + error);
				return;
			}
			System.out.println("Mail sent succesfully!");
		}
	}

	/* Clear the fields in the GUI. */
	class ClearListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("Clearing fields");
			fromField.setText("");
			toField.setText("");
			subjectField.setText("");
			messageText.setText("");
		}
	}

	/* Quit. */
	class QuitListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
}

