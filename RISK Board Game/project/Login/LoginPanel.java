package project.Login;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class LoginPanel extends JPanel {
	private JLabel error;
	private JTextField unField;
	private JPasswordField pwField;
	private JTextField ipField;
	private JTextField portField;

	public LoginPanel(LoginControl lc) {
		// Create a panel for the labels at the top of the GUI.
		JPanel labelPanel = new JPanel(new GridLayout(2, 1, 5, 5));
		error = new JLabel();
		error.setForeground(Color.RED);
		JLabel instructionLabel = new JLabel("Enter your username and password to log in.", JLabel.CENTER);
		labelPanel.add(error);
		labelPanel.add(instructionLabel);

		// Create a panel for the login information form.
		JPanel loginPanel = new JPanel(new GridLayout(2, 2, 5, 5));
		JLabel usernameLabel = new JLabel("Username:", JLabel.RIGHT);
		unField = new JTextField(10);
		unField.setText("");
		JLabel passwordLabel = new JLabel("Password:", JLabel.RIGHT);
		pwField = new JPasswordField(10);
		pwField.setText("");
		loginPanel.add(usernameLabel);
		loginPanel.add(unField);
		loginPanel.add(passwordLabel);
		loginPanel.add(pwField);

		// Create a panel for the buttons.
		JPanel buttonPanel = new JPanel();
		JButton submitButton = new JButton("Submit");
		submitButton.addActionListener(lc);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(lc);
		buttonPanel.add(submitButton);
		buttonPanel.add(cancelButton);

		JPanel serverInfoBuffer = new JPanel(new GridLayout(2, 2, 5, 5));
		JLabel lblIPAddress = new JLabel("IP Address");
		ipField = new JTextField();
		ipField.setText("127.0.0.1");
		serverInfoBuffer.add(lblIPAddress);
		serverInfoBuffer.add(ipField);

		JLabel lblPortNum = new JLabel("Port");
		portField = new JTextField();
		portField.setText("9300");
		serverInfoBuffer.add(lblPortNum);
		serverInfoBuffer.add(portField);

		// Arrange the three panels in a grid.
		JPanel grid = new JPanel(new GridLayout(4, 1, 0, 10));
		grid.add(labelPanel);
		grid.add(loginPanel);
		grid.add(buttonPanel);
		grid.add(serverInfoBuffer);
		this.add(grid);
	}

	public void setError(String error) {
		this.error.setText(error);
	}

	public String getUsername() {
		return unField.getText();
	}

	public String getPassword() {
		return String.copyValueOf(pwField.getPassword());
	}

	public String getIP() {
		return ipField.getText();
	}

	public String getPort() {
		return portField.getText();
	}
}
