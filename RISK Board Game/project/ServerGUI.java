package project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ServerGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLabel sLabel, pNum, tOut, sLog;
	public static JLabel status;
	private JButton listen, close, stop, quit;
	private JPanel northPanel, southPanel, statusPanel, portPanel, timeoutPanel, buttonPanel;
	private String[] labels = { "Port #", "Timeout" };
	private JTextField[] textFields = new JTextField[labels.length];
	private JTextArea log;
	private JScrollPane logPane;

	private Server server;
	private boolean serverStarted;
	private JPanel PlayerCountPanel;
	private JLabel lblPlayerCount;
	private JComboBox<String[]> comboBox;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ServerGUI(String title) {

		server = new Server();
		serverStarted = false;

		this.setTitle(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		northPanel = new JPanel();
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
		southPanel = new JPanel();
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));

		// Beginning of Status Panel
		sLabel = new JLabel("Status:");

		status = new JLabel("Not Connected");
		status.setForeground(Color.RED);

		statusPanel = new JPanel(new FlowLayout());
		statusPanel.add(sLabel);
		statusPanel.add(status);

		northPanel.add(statusPanel);

		// Beginning Port Panel
		pNum = new JLabel(labels[0]);
		textFields[0] = new JTextField(10);

		portPanel = new JPanel();
		portPanel.add(pNum);
		portPanel.add(textFields[0]);

		northPanel.add(portPanel);

		// Beginning Timeout Panel
		tOut = new JLabel(labels[1]);
		textFields[1] = new JTextField(10);

		timeoutPanel = new JPanel();
		timeoutPanel.add(tOut);
		timeoutPanel.add(textFields[1]);

		northPanel.add(timeoutPanel);

		// Loose Components
		sLog = new JLabel("Server Log Below");
		sLog.setAlignmentX(CENTER_ALIGNMENT);
		southPanel.add(sLog);

		log = new JTextArea(10, 2);
		logPane = new JScrollPane();
		logPane.setViewportView(log);
		southPanel.add(logPane);

		server.LinkLog(log);// Combines both logs
		server.LinkStatus(status);

		// Beginning Button Panel

		textFields[0].setText("9300");
		textFields[1].setText("500");
		listen = new JButton("Listen");
		listen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { // Listens for connections
				textFields[0].setEditable(false);
				textFields[1].setEditable(false);
				comboBox.setEnabled(false);
				if (textFields[0].getText().length() < 1 || textFields[1].getText().length() < 1) {
					log.setText(log.getText() + "Port Number/ Timeout not entered before pressing \"Listen\"\n");
				} else {
					server.setMaxClients(Integer.parseInt(comboBox.getSelectedItem().toString()));
					server.setPort(Integer.parseInt(textFields[0].getText()));
					server.setTimeout(Integer.parseInt(textFields[1].getText()));
					try {
						server.listen();
						log.setText("listening\n");
					} catch (IOException e1) {
						log.setText("Listening Failed\n");
						System.out.println("Error connecting through Port: " + textFields[0].getText());
						System.out.println("Make sure that there are no Risk Servers running on the same port.");
					}
					serverStarted = true;

				}
			}
		});

		close = new JButton("Close");
		close.setVisible(false);
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {// Stops accepting new connections
				comboBox.setEnabled(true);
				if (!serverStarted) {
					log.setText(log.getText() + "Server not currently started.\n");
				} else {
					try {
						server.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		stop = new JButton("Stop");
		stop.addActionListener(new ActionListener() { // Destroys all connections and stops listening
			public void actionPerformed(ActionEvent e) {
				comboBox.setEnabled(true);
				if (!serverStarted) {
					log.setText(log.getText() + "Server not currently started.\n");
				} else {
					server.stopListening();
				}
			}
		});

		quit = new JButton("Quit");
		quit.addActionListener(new ActionListener() { // Closes down the whole server
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		buttonPanel = new JPanel(new FlowLayout());

		buttonPanel.add(listen);
		buttonPanel.add(close);
		buttonPanel.add(stop);
		buttonPanel.add(quit);

		southPanel.add(buttonPanel);

		getContentPane().add(northPanel, BorderLayout.NORTH);

		PlayerCountPanel = new JPanel();
		northPanel.add(PlayerCountPanel);

		lblPlayerCount = new JLabel("Number of Players: ");
		PlayerCountPanel.add(lblPlayerCount);

		comboBox = new JComboBox<String[]>();
		comboBox.setModel(new DefaultComboBoxModel(new String[] { "2", "3", "4", "5", "6" }));
		PlayerCountPanel.add(comboBox);
		getContentPane().add(southPanel, BorderLayout.SOUTH);
		this.setSize(400, 450);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		String title = "Risk - Semester Project";
		try {
			title = args[0];
			title = InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
		}
		System.out.println();
		new ServerGUI(title); // Title of GUI
	}
}