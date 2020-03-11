package project;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JTextArea;

import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import project.CreateAccount.CreateAccountData;
import project.Login.LoginData;

public class Server extends AbstractServer {
	private static BufferedReader br;
	private static FileReader fr;
	private JTextArea log;
	private JLabel status;
	// private static String[] colors = { "Red", "Orange", "Pink", "Green",
	// "Yellow", "Blue" };
	private static String[] colors = { "Green", "Cyan", "Red", "Pink", "Yellow", "Blue" };
	private static char[] hash = { 'f', 'f', 'f', 'f', 'f', 'f' };
	private static ArrayList<ConnectionToClient> listOfClients; // Player Queue
	private static int maxClients; // Number of Players
	private static int currentPlayer; // The Index of the current player
	private static int mapLands, claimedLands;
	private Database db;

	public Server() {
		super(12345);
		setTimeout(500);
		listOfClients = new ArrayList<ConnectionToClient>();
		currentPlayer = 0;
		mapLands = 42;
		claimedLands = 0;
		db = new Database();
	}

	public void broadcast(String s) {
		for (ConnectionToClient c : listOfClients) {
			try {
				c.sendToClient(s);
			} catch (IOException e) {
				System.out.println("Problem sending to " + c.toString());
			}
		}
	}

	public static ConnectionToClient getCurrentPlayer() {
		return listOfClients.get(currentPlayer % maxClients);
	}

	public int getMaxClients() {
		return maxClients;
	}

	public void setMaxClients(int maximum) {
		maxClients = maximum;
	}

	public void LinkLog(JTextArea log) {
		this.log = log;
	}

	public void LinkStatus(JLabel status) {
		this.status = status;
	}

	public Server(int port) {
		super(port);
	}

	public void setLog(JTextArea log) {
		this.log = log;
	}

	public JTextArea getLog() {
		return log;
	}

	public void setStatus(JLabel status) {
		this.status = status;
	}

	public JLabel getStatus() {
		return status;
	}

	@Override
	protected void handleMessageFromClient(Object arg0, ConnectionToClient arg1) {
		if (arg0 instanceof OccupyLandData) {
			claimedLands++;
			broadcast(arg0.toString());
			// <html>North Territory<br>0<html>;Red
			// System.out.println(arg0.toString());
			if (claimedLands == mapLands) {
				// change everyone's stage
				broadcast("STAGE;PLACE_UNITS1");
			}
			nextPlayersTurn();
		} else if (arg0 instanceof PlaceUnitsData) {
			broadcast(arg0.toString());
			// System.out.println(arg0.toString());
			if (((PlaceUnitsData) arg0).isSetupPhase()) {
				nextPlayersTurn();
			}

		} else if (arg0 instanceof ChangeCountryOwnershipData) {
			broadcast(arg0.toString());
			// System.out.println("broadcast: " + arg0.toString());
		} else if (arg0 instanceof String && ((String) arg0).startsWith("GAMEOVER")) {
			broadcast(arg0.toString());
		} else if (arg0 instanceof LoginData) {
			db.setConnection("project/db.properties");
			LoginData data = (LoginData) arg0;
			String query = "SELECT UserName FROM user WHERE UserName = '" + data.getUsername()
					+ "' AND aes_decrypt(password,'key') = '" + data.getPassword() + "';";
			ArrayList<String> result = db.query(query);
			System.out.println("result from db: " + result.get(0));
			System.out.println("Username submitted: " + data.getUsername());
			if ((result.get(0)).equals(data.getUsername())) {
				try {
					arg1.sendToClient("SET_VERIFY:true");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					arg1.sendToClient("SET_VERIFY:false");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (arg0 instanceof CreateAccountData) {
			db.setConnection("project/db.properties");
			CreateAccountData data = (CreateAccountData) arg0;
			String query = "INSERT INTO user VALUES('" + data.getUsername() + "',aes_encrypt('" + data.getPassword()
					+ "','key'),0,0);";
			try {
				db.executeDML(query);
				arg1.sendToClient(true);
			} catch (SQLException e) {
				try {
					arg1.sendToClient(false);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected void listeningException(Throwable exception) {
		// Display info about the exception
		System.out.println("Listening Exception:" + exception);
		exception.printStackTrace();
		System.out.println(exception.getMessage());

	}

	protected void serverStarted() {
		System.out.println("Server Started");
		status.setForeground(Color.GREEN);
		status.setText("Listening");
	}

	protected void serverStopped() {
		System.out.println("Server Stopped Listening");
		status.setForeground(Color.BLACK);
		status.setText("Server Stopped Listening");
	}

	protected void serverClosed() { // Destroys all connections and accepts no more.
		System.out.println("Server and all current clients are closed - Press Listen to Restart");
	}

	protected void clientConnected(ConnectionToClient client) {
		System.out.println("Client Connected");
		log.append("Client Connected\n");

		if (listOfClients.size() < maxClients) {
			listOfClients.add(client);
			try {
				// Gives the client their color, and num of players.
				client.sendToClient(getColor() + maxClients);
			} catch (IOException e) {
				System.out.println("Could not assign color to client");
			}
			if (listOfClients.size() == maxClients) {
				this.stopListening();// Do not accept more clients
				activatePlayerOne();
			}
		}

	}

	private void activatePlayerOne() {
		ConnectionToClient cp = getCurrentPlayer();
		try {
			cp.sendToClient("TURN_STARTED");
		} catch (IOException e) {
			System.out.println("Problem activating player.");
		}
	}

	public static void nextPlayersTurn() {
		ConnectionToClient cp = getCurrentPlayer();
		try {
			cp.sendToClient("TURN_ENDED");
		} catch (IOException e) {
			System.out.println("Problem terminating player.");
		}

		currentPlayer++;
		cp = getCurrentPlayer();
		try {
			cp.sendToClient("TURN_STARTED");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getColor() {
		for (int i = 0; i < hash.length; i++) {
			if (hash[i] == 'f') {
				hash[i] = 'n';
				return colors[i];
			}
		}
		return ":)";
	}

}
