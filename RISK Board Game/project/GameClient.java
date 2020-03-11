package project;

import java.io.IOException;
import java.util.ArrayList;

import ocsf.client.AbstractClient;
import project.Game.GamePanel;

public class GameClient extends AbstractClient {
	private String host;
	private int port;
	private ArrayList<String> countries; // List of countries player controls
	private ArrayList<String> cards; // List of cards player has
	private int units;
	private String color;
	private String name;
	private static String stage;
	private boolean active;
	private boolean attackSkip;
	private String attacker;
	private String target;
	private String donor;
	private String receiver;
	private boolean verifyCredentials;

	public GameClient(String host, int port) {
		super(host, port);

		// instantiate arraylists as arraylists
		countries = new ArrayList<>();
		cards = new ArrayList<>();
		setStage("PLAYER_SETUP");
		active = false;
		attackSkip = false;
		units = 0;
		donor = attacker = "";
		receiver = target = "";
	}

	public void setAttackSkip(boolean b) {
		attackSkip = b;
	}

	public boolean getAttackSkip() {
		return attackSkip;
	}

	public void setDonor(String d) {
		donor = d;
	}

	public String getDonor() {
		return donor;
	}

	public void setReceiver(String r) {
		receiver = r;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setAttacker(String s) {
		attacker = s;
	}

	public String getAttacker() {
		return attacker;
	}

	public void setTarget(String s) {
		target = s;
	}

	public String getTarget() {
		return target;
	}

	public void setUnits(int u) {
		units = u;
	}

	public int getUnits() {
		return units;
	}

	public static void setStage(String s) {
		stage = s.toUpperCase();
		System.out.println("STAGE CHANGED: " + stage);
	}

	public String getStage() {
		return stage;
	}

	public void setActive(boolean a) {
		active = a;
	}

	public boolean isActive() {
		return active;
	}

	public ArrayList<String> getCountries() {
		return countries;
	}

	public ArrayList<String> getCardOptions() {
		return cards;
	}

	protected void handleMessageFromServer(Object arg0) {
		String input = (String) arg0;
		if (input.startsWith("SET_VERIFY:")) {
			verifyCredentials = Boolean.parseBoolean(input.substring(11));
		} else if (!(input.equals("TURN_STARTED") || input.equals("TURN_ENDED") || input.startsWith("STAGE")
				|| input.startsWith("ADD") || input.startsWith("SUB") || input.startsWith("CHANGE")
				|| input.startsWith("GAMEOVER"))) {
			switch (stage) {
			case "PLAYER_SETUP":
				playerSetup(input);
				setStage("OCCUPY_LANDS");

				GamePanel.setTextSouthPanelTitleLabel("Setup: ");
				GamePanel.setTextSouthPanelMsgLabel("Place your armies");

				break;
			case "OCCUPY_LANDS":
				// land;color
				colorCountries(input);
				break;
			case "PLACE_UNITS1":
				System.out.println("This should never be used?; in GameClient");
				break;
			case "MOVE":

				break;
			default:
				break;
			}
		} else {
			if (input.equals("TURN_ENDED")) {
				this.setAttackSkip(false);
				this.setActive(false);
				System.out.println("Status: Not-Active");
			} else if (input.startsWith("STAGE")) {
				setStage(input.substring(6));
				switch (input.substring(6)) {
				case "OCCUPY_LANDS":

					GamePanel.setTextSouthPanelTitleLabel("Setup: ");
					GamePanel.setTextSouthPanelMsgLabel("Claim your land!");
					break;

				case "PLAYER_SETUP":

					GamePanel.setTextSouthPanelTitleLabel("Setup: ");
					GamePanel.setTextSouthPanelMsgLabel("Player Setup");
					break;

				case "PLACE_UNITS1":

					GamePanel.setTextSouthPanelTitleLabel("Placement: ");
					GamePanel.setTextSouthPanelMsgLabel("Place your units!");
					break;

				}
			} else if (input.startsWith("ADD")) {
				addUnitsToCountry(input.substring(3));
			} else if (input.startsWith("SUB")) {
				subUnitsToCountry(input.substring(3));
			} else if (input.startsWith("CHANGE")) {
				changeCountryOwnership(input.substring(6));
			} else if (input.startsWith("GAMEOVER")) {
				System.out.println("Client RECIEVED GAMEOVER!!!!!! YAYYAYYAYAYAY");
				GamePanel.setTextSouthPanelTitleLabel("Game Over : ");
				if (this.getColor().equals(input.substring(9))) {
					GamePanel.setTextSouthPanelMsgLabel("You Win!!");
				} else {
					GamePanel.setTextSouthPanelMsgLabel("Thanks for playing!");
				}
				// <SERVER> cp-> setActive(false); [or broadcast("TURN_ENDED"); ]
				// Display to client "GameOver"
			} else if (input.equals("TURN_STARTED")) {
				this.setActive(true);
				System.out.println("Status: Active");
				if (this.getStage().equals("MOVE")) {
					// Calculate the number of armies gained
					this.setUnits(calculateUnits());
					// tell client that (s)he has x units
					GamePanel.setTextSouthPanelTitleLabel("Units :");
					GamePanel.setTextSouthPanelMsgLabel(this.getUnits() + "");
					// Place all armies (one army per click for now)
					/////// tell client to click countries in order to place units
					/////// client.setUnits(client.getUnits() -1);
					/////// Use PlaceUnitsData to send update to every Client
					// Attack <Optional> Click Attacker, then click victim
					///////
					// Fortify <Optional> Click Donor, then click Receiver, then input how many.
				}
			}
		}

	}

	private void changeCountryOwnership(String input) {
		// oldcolor;color;units;land
		String oldColor = input.substring(0, input.indexOf(";"));

		// color;units;land
		input = input.substring(input.indexOf(";") + 1);

		String color = input.substring(0, input.indexOf(";"));

		// units;land
		input = input.substring(input.indexOf(";") + 1);

		int units = Integer.parseInt(input.substring(0, input.indexOf(';')));

		// land
		input = input.substring(input.indexOf(';') + 1);

		String land = input;

		// change the color of the country
		GamePanel.colorCountry(land, color);

		GamePanel.setCountryUnits(land, units);

		if (oldColor.equals(getColor())) {
			for (int i = 0; i < countries.size(); i++) {
				if (countries.get(i).equals(land)) {
					countries.remove(i);
					break;
				}
			}
		}
	}

	private int calculateUnits() {
		// Based on number of lands in possession
		int armies = countries.size() / 3;
		if (armies < 3) {
			armies = 3;
		}
		// System.out.println("armies: " + armies);
		// Check if you control all of a country
		String[] group = { "NorthAmerica", "SouthAmerica", "Europe", "Africa", "Asia", "Australia" };
		int[] landsInControl = { 0, 0, 0, 0, 0, 0 };
		int[] landsInGroup = { 9, 4, 7, 6, 12, 4 };
		int[] bonus = { 5, 2, 5, 3, 7, 2 };

		for (String landName : countries) { // get GroupName
			String key = GamePanel.getGroupName(landName);
			if (key.equals(group[0])) {
				landsInControl[0]++;
			} else if (key.equals(group[1])) {
				landsInControl[1]++;
			} else if (key.equals(group[2])) {
				landsInControl[2]++;
			} else if (key.equals(group[3])) {
				landsInControl[3]++;
			} else if (key.equals(group[4])) {
				landsInControl[4]++;
			} else if (key.equals(group[5])) {
				landsInControl[5]++;
			} else {
				System.out.println("Error in GameClient.calculateUnits()");
			}

		}

		for (int i = 0; i < group.length; i++) {
			if (landsInControl[i] == landsInGroup[i]) {
				armies += bonus[i];
			}
		}

		// Check if player has traded cards <Skip for now> :)
		System.out.println("New units: " + armies);
		return armies;
	}

	private void addUnitsToCountry(String input) {
		String land = input.substring(0, input.indexOf(';'));
		// System.out.println("Land: " + land);

		int units = Integer.parseInt(input.substring(input.indexOf(';') + 1));
		// System.out.println("Units: " + units);

		GamePanel.addUnitsToCountry(land, units);
	}

	private void subUnitsToCountry(String input) {
		String land = input.substring(0, input.indexOf(';'));
		// System.out.println("Land: " + land);

		int units = Integer.parseInt(input.substring(input.indexOf(';') + 1));
		// System.out.println("Units: " + units);

		GamePanel.addUnitsToCountry(land, units * -1);
	}

	private void colorCountries(String input) {
		String land = input.substring(0, input.indexOf(';'));
		land = land.replace("<html>", "");
		land = land.substring(0, land.indexOf("<"));
		// System.out.println("Land: " + land);

		String color = input.substring(input.indexOf(';') + 1);
		// System.out.println("Color: " + color);

		GamePanel.colorCountry(land, color);

		// Add land to ArrayList
		if (color.equals(this.getColor())) {
			countries.add(land);
		}
		/** MAKE SURE IT IS REMOVED WHEN YOU LOSE THIS LAND */
	}

	public void addCountry(String landName) {
		countries.add(landName);
	}

	private void playerSetup(String input) {
		color = input.substring(0, input.length() - 1);
		GamePanel.setTextSouthPanelTitleLabel(color);
		/** GUI */ // Tell user their color and how many units they have
		System.out.println("---Color: " + this.color);
		int players = Integer.parseInt(input.substring(input.length() - 1));
		switch (players) {
		case 2: // delete later, do not map out play for 2 players
			units = 40;// 2
			break;
		case 3:
			units = 35;
			break;
		case 4:
			units = 30;
			break;
		case 5:
			units = 25;
			break;
		case 6:
			units = 20;
			break;
		default:
			System.out.println("Error, why are there not 3-6 players?");
			break;
		}
		System.out.println("---Units: " + this.getUnits());
	}

	private void update(String text) {
		/** GUI */ // update all the labels according to input substring
		String country = text.substring(0, text.indexOf(";"));
		System.out.println("Country: " + country); /** Implement On GUI */

		text = text.substring(text.indexOf(";") + 1);
		String color = text.substring(0, text.indexOf(";"));
		System.out.println("Color: " + color); /** Implement On GUI */

		int units = Integer.parseInt(text.substring(text.indexOf(";") + 1));
		System.out.println("Units: " + units); /** Implement On GUI */

		GamePanel.update(country, color, units);
	}

	private void increase(String input) {
		// Alberta--INCREASE--1
		String country = input.substring(0, input.indexOf("-") - 1);
		System.out.println("Country: " + country);

		input = input.replace("INCREASE", "~");
		int amt = Integer.parseInt(input.substring(input.indexOf("~")));

		System.out.println("amount: " + amt);

		GamePanel.increase(country, units);
	}

	// private void TerminateTurn() {
	// this.setActive(false);
	// Server.nextPlayersTurn();
	// }

	public void connectionEstablished() {
		System.out.println("Connection Established");
		try {
			sendToServer("Connection Established");
		} catch (IOException e) {
			System.out.println("error connecting");
			e.printStackTrace();
		}
	}

	public void connectionException(Throwable exception) {
		System.out.println("Connection Exception Occured");
		System.out.println(exception.getMessage());
		exception.printStackTrace();
	}

	public void setIPAddress(String ip) {
		this.host = ip;
		this.setHost(host);
	}

	public void setPortNumber(int parseInt) {
		this.port = parseInt;
		this.setPort(port);
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getVerifyCredentials() {
		boolean temp = verifyCredentials;
		verifyCredentials = false;
		return temp;
	}

	public void setVerifyCredentials(boolean flag) {
		verifyCredentials = flag;
	}
}
