package project.Login;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JPanel;

import project.GameClient;

public class LoginControl implements ActionListener {

	private JPanel container;
	private GameClient client;
	private LoginData data;

	public LoginControl(JPanel container, GameClient client) {
		this.container = container;
		this.client = client;
		data = new LoginData();
	}

	// event handling
	public void actionPerformed(ActionEvent ae) {
		CardLayout cardLayout = (CardLayout) container.getLayout();
		String command = ae.getActionCommand();

		// The submit button gets the username and password entered and stores them in
		// variables
		// The cancel button sends the user back to the InitialPanel
		if (command.contentEquals("Cancel")) {
			cardLayout = (CardLayout) container.getLayout();
			cardLayout.show(container, "1");
		} else if (command.contentEquals("Submit")) {
			LoginPanel loginPanel = (LoginPanel) container.getComponent(1);
			if (loginPanel.getUsername().equals("") || loginPanel.getPassword().equals("")) {
				loginPanel.setError("Required Field(s) Left Empty");
			} else {
				data.setUsername(loginPanel.getUsername());
				data.setPassword(loginPanel.getPassword());
				client.setHost(loginPanel.getIP());
				client.setPort(Integer.parseInt(loginPanel.getPort()));
				try {
					client.openConnection();
					client.sendToServer(data);
				} catch (IOException e) {
					e.printStackTrace();
				}

				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (client.getVerifyCredentials()) {
					cardLayout = (CardLayout) container.getLayout();
					cardLayout.show(container, "4");
					client.setName(data.getUsername());
				} else
					loginPanel.setError("Invalid Username or Password");
			}
		}
	}
}
