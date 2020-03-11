package project.CreateAccount;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JPanel;

import project.GameClient;
import project.Login.LoginPanel;

public class CreateAccountControl implements ActionListener {
	private JPanel container;
	private GameClient client;
	private CreateAccountData data;

	public CreateAccountControl(JPanel container, GameClient client) {
		this.container = container;
		this.client = client;
		data = new CreateAccountData();
	}

	public void actionPerformed(ActionEvent ae) {
		CardLayout cardLayout = (CardLayout) container.getLayout();
		String command = ae.getActionCommand();

		if (command.contentEquals("Cancel")) {
			cardLayout = (CardLayout) container.getLayout();
			cardLayout.show(container, "1");
		} else if (command.contentEquals("Submit")) {
			CreateAccountPanel createAccountPanel = (CreateAccountPanel) container.getComponent(2);

			if (createAccountPanel.getUsername().equals("") || createAccountPanel.getPassword().equals("")
					|| createAccountPanel.getVerify().equals("")) {
				createAccountPanel.setError("Required Field(s) Left Empty");
			} else if (!createAccountPanel.getPassword().equals(createAccountPanel.getVerify())) {
				createAccountPanel.setError("Passwords Not Matching");
			} else {
				data.setUsername(createAccountPanel.getUsername());
				data.setPassword(createAccountPanel.getPassword());
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
					LoginPanel loginPanel = (LoginPanel) container.getComponent(1);
					loginPanel.setError("");
					cardLayout = (CardLayout) container.getLayout();
					cardLayout.show(container, "2");
					client.setName(data.getUsername());
				} else
					createAccountPanel.setError("Invalid Username or Password");
			}
		}
	}

}
