package project.Game;

import java.awt.CardLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.midi.Receiver;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import project.ChangeCountryOwnershipData;
import project.GameClient;
import project.OccupyLandData;
import project.PlaceUnitsData;
import project.Login.LoginPanel;

public class GameControl implements ActionListener {
	private JPanel container;
	private GameClient client;
	private ArrayList<String> countries;
	private ArrayList<String> deck;
	private ArrayList<String> descriptions;
	private ArrayList<String> cardImages;
	private final int MAP_LAND_COUNT = 42;

	// country click
	private JLabel leftPanelNameLabel;
	private JLabel leftPanelOwnedByLabel;
	private JLabel leftPanelTroopsLabel;
	private JLabel southPanelTitleLabel;
	private JLabel southPanelMsgLabel;
	private Random randy = new Random();

	// card click
	private JButton cardInfoButton;
	private JLabel cardTitle;
	private JLabel cardDescription;

	// private boolean attackSkip;

	public GameControl(JPanel container, GameClient client, JFrame frame) {
		this.container = container;
		this.client = client;
		frame.setTitle("RISK Board");
		countries = client.getCountries();
		deck = client.getCardOptions();
		client.setAttackSkip(false);
		// System.out.println(descriptions);
	}

	public void actionPerformed(ActionEvent ae) {
		// get the name of the button clicked
		String command = ae.getActionCommand();
		// System.out.println(command);// Temporarily disabled
		if (command.contentEquals("Back")) {
			LoginPanel loginPanel = (LoginPanel) container.getComponent(1);
			CardLayout cardLayout = (CardLayout) container.getLayout();
			cardLayout.show(container, "1");
		} else if (command.contentEquals("End Attack")) {
			GamePanel.setTextSouthPanelTitleLabel("Fortify: ");
			GamePanel.setTextSouthPanelMsgLabel("Pick a land to donate from");
			client.setAttacker("");
			client.setAttackSkip(true);
		} else if (command.contentEquals("Submit Fortification") && client.getDonor().length() > 1
				&& client.getReceiver().length() > 1) {
			int units = GamePanel.getFortifyUnits();
			if (units + 1 <= parseUnitsString(client.getDonor())) {
				System.out.println("Units that are being donated: " + units);
				GamePanel.setTextSouthPanelTitleLabel("Fortification: ");
				GamePanel.setTextSouthPanelMsgLabel(parseLandString(client.getDonor()) + " donated " + units + " to "
						+ parseLandString(client.getReceiver()));
				PlaceUnitsData pud = new PlaceUnitsData(parseLandString(client.getDonor()), units * -1);
				try {
					client.sendToServer(pud);
				} catch (IOException e) {
					System.out.println("Could not donate units.");
					GamePanel.setTextSouthPanelTitleLabel("");
					GamePanel.setTextSouthPanelMsgLabel("Could not donate units");
				}
				pud = new PlaceUnitsData(parseLandString(client.getReceiver()), units);
				try {
					client.sendToServer(pud);
				} catch (IOException e) {
					System.out.println("Could not remove donating units.");
					GamePanel.setTextSouthPanelTitleLabel("");
					GamePanel.setTextSouthPanelMsgLabel("Could not remove donating units");
				}
				GamePanel.setBtnSubmitFortificationEnabled(false);
				pud = new PlaceUnitsData("Alberta", 0, true);
				try {
					client.sendToServer(pud);
				} catch (IOException e) {
					System.out.println("Failure to send the pud");
				}
				client.setReceiver("");
				client.setDonor("");
			} else {
				System.out.println("Not enough trooperrrs.");
				GamePanel.setTextSouthPanelTitleLabel("");
				GamePanel.setTextSouthPanelMsgLabel("Not enough troooopppeeerrrrsss");
			}
		} else if (Character.isDigit(command.charAt(0))) {
			// need to instantiate labels so we can write to them
			GamePanel gp = (GamePanel) container.getComponent(3);
			System.out.println(cardDescription.getText());

			// find out which card was passed by number in top right of card
			int id = Integer.parseInt(command);

			// actually write to them
			cardInfoButton.setText(Integer.toString(id));
			File imageCheck = new File("resources/" + cardImages.get(id - 1));
			if (!imageCheck.exists()) {
				System.out.println("Image file not found!");
			}
			Image img = null;
			try {
				img = ImageIO.read(imageCheck);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			cardInfoButton.setIcon(new ImageIcon(img));

			cardTitle.setText(deck.get(id - 1));

			String descript = descriptions.get(id - 1);
			cardDescription.setText(descript);

		} else {// when lands are pressed

			// System.out.println("Client is active: " + client.isActive());
			switch (client.getStage()) {
			///////////
			case "OCCUPY_LANDS":
				if (client.isActive() && GamePanel.isUnoccupiedLand(command)) {
					try {
						OccupyLandData data = new OccupyLandData(client.getColor(), command);
						GamePanel.setTextSouthPanelTitleLabel("");
						GamePanel.setTextSouthPanelMsgLabel("You claimed " + parseLandString(command));
						client.sendToServer(data);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			///////////
			case "PLACE_UNITS1":
				if (client.isActive() && client.getUnits() > 0
						&& client.getColor().equals(GamePanel.getColorOfLand(command))) {

					// Decrease units by 1
					client.setUnits(client.getUnits() - 1);

					GamePanel.setTextSouthPanelTitleLabel("Placed unit: ");
					GamePanel.setTextSouthPanelMsgLabel(
							parseLandString(command) + " : Units Left(" + client.getUnits() + ")");

					// Create an PlaceUnitsData
					PlaceUnitsData pud = new PlaceUnitsData(command, 1, true);
					// Broadcast it to all by sending to server
					try {
						client.sendToServer(pud);
					} catch (IOException e) {
						System.out.println("Error sending \"pud\" to server.");
					}

					if (client.getUnits() == 0) {
						client.setStage("MOVE");
					}
				} else {
					System.out.println("I cant place a unit here");
				}
				break;
			///////////
			case "MOVE":
				// System.out.println("attackSkip: " + attackSkip);
				if (client.isActive() && client.getUnits() > 0
						&& client.getColor().equals(GamePanel.getColorOfLand(command))) { // Placing units
					// Decrease units by 1
					client.setUnits(client.getUnits() - 1);

					GamePanel.setTextSouthPanelTitleLabel("Placed unit: ");
					GamePanel.setTextSouthPanelMsgLabel(
							parseLandString(command) + " : Units Left(" + client.getUnits() + ")");

					// Create an PlaceUnitsData
					PlaceUnitsData pud = new PlaceUnitsData(command, 1);
					// Broadcast it to all by sending to server
					try {
						client.sendToServer(pud);
					} catch (IOException e) {
						System.out.println("Error sending \"pud\" to server.");
					}
					client.setAttackSkip(false);
				} else if (client.getTarget().length() < 1 && client.isActive() && !client.getAttackSkip()) { // Attacking
																												// <BIG
																												// PICTURE>
					attack(command);
				} else if (client.isActive() && client.getAttackSkip()) { // Fortify Stage

					// Get donor Land
					if (parseLandString(client.getDonor()).length() < 1
							&& client.getColor().equals(GamePanel.getColorOfLand(command))) { // Assigns
						// Lands
						client.setDonor((command));

						GamePanel.setTextSouthPanelTitleLabel("Fortify: ");
						GamePanel.setTextSouthPanelMsgLabel(
								parseLandString(client.getDonor()) + " is donating to ________");

						System.out.println("Donor: " + parseLandString(command));

					} else if (parseLandString(client.getDonor()).length() > 1
							&& client.getColor().equals(GamePanel.getColorOfLand(command))) {// get Receiver
						client.setReceiver(command);

						GamePanel.setTextSouthPanelTitleLabel("Fortify: ");
						GamePanel.setTextSouthPanelMsgLabel(parseLandString(
								client.getDonor() + " is donating to " + parseLandString(client.getReceiver())));

						System.out.println("Receiver: " + parseLandString(command));

						GamePanel.setBtnSubmitFortificationEnabled(true);
					}
				}
				break;
			///////////
			case "BASE":

				break;
			///////////
			default:
				break;
			}

			for (String country : countries) {
				if (command.contains(country)) {
					// need to instantiate labels so we can write to them
					GamePanel gamePanel = (GamePanel) container.getComponent(3);
					leftPanelNameLabel = gamePanel.getLeftPanelNameLabel();
					southPanelTitleLabel = gamePanel.getSouthPanelTitleLabel();
					southPanelMsgLabel = gamePanel.getSouthPanelMsgLabel();

					// actually write to them
					leftPanelNameLabel.setText(country);
					// southPanelTitleLabel.setText(country);
					// southPanelMsgLabel.setText("You just clicked on: " + country);
				}
			}
		}
	}

	private void attack(String command) {

		GamePanel.setTextSouthPanelTitleLabel("Attack Phase");
		GamePanel.setTextSouthPanelMsgLabel("");
		// Get Attacker and Target
		String colorOfTarget = "";
		int aInt = -1;
		int dInt = -1;

		// Get Attacker
		if (client.getColor().equals(GamePanel.getColorOfLand(command)) && parseUnitsString(command) > 1) { // Assigns
			// Lands
			client.setAttacker((command));
			GamePanel.setTextSouthPanelTitleLabel("Attack: ");
			GamePanel.setTextSouthPanelMsgLabel(parseLandString(client.getAttacker()) + " is attacking _______");

			// System.out.println("Attacker: " + parseLandString(command));
		} else if (parseLandString(client.getAttacker()).length() > 1
				&& !client.getColor().equals(GamePanel.getColorOfLand(command))) {// get Target
			client.setTarget(command);

			GamePanel.setTextSouthPanelTitleLabel("Attack: ");
			GamePanel.setTextSouthPanelMsgLabel(
					parseLandString(client.getAttacker()) + " is attacking " + parseLandString(client.getTarget()));

			colorOfTarget = GamePanel.getColorOfLand(parseLandString(command));
			// System.out.println("Victim: " + parseLandString(command));

		} else if (client.getAttacker().length() > 1 && client.getTarget().length() < 1) {
			// This happens when we are trying to fortify
			System.out.println("Attacker And Target Reset. <method Dos>");
			GamePanel.setTextSouthPanelTitleLabel("Attack: ");
			GamePanel.setTextSouthPanelMsgLabel("Attacker and Target Reset");

			System.out.println("Command " + command);
			System.out.println("1 " + (client.getUnits() > 1));
			System.out.println("2 " + client.getColor().equals(GamePanel.getColorOfLand(command)));
			// GamePanel.setBtnSubmitFortificationEnabled(true);
		}

		if (client.getTarget().length() > 1
				&& !GamePanel.areAdjacent(parseLandString(client.getAttacker()), parseLandString(client.getTarget()))) {
			// Reset lands if not adjacent
			client.setTarget("");
			client.setAttacker("");
			System.out.println("Attacker And Target Reset.");
			GamePanel.setTextSouthPanelTitleLabel("Attack: ");
			GamePanel.setTextSouthPanelMsgLabel("Attacker and Target Reset");
		}
		if (client.getTarget().length() > 1 && parseLandString(client.getAttacker()).length() > 1) {
			// Actual Attack Logic <SMALL PICTURE>
			System.out.println(
					parseLandString(client.getAttacker()) + " is attacking " + parseLandString(client.getTarget()));
			aInt = parseUnitsString(client.getAttacker());
			dInt = parseUnitsString(client.getTarget());

			// Attacking logic:
			while (aInt > 1 && dInt > 0) {

				System.out.println("Attacker units " + parseUnitsString(client.getAttacker()));
				System.out.println("Target units " + parseUnitsString(client.getTarget()));

				// Sets number of dice for attacker
				int offenseDice = 1;
				if (aInt == 3) {
					offenseDice = 2;
				} else if (aInt > 3) {
					offenseDice = 3;
				}
				// Sets number of dice for defender
				int defenseDice = 1;
				if (dInt > 1) {
					defenseDice = 2;
				}

				// Dice comparison
				// System.out.println("Before U assignation");
				int u = battle(offenseDice, defenseDice);
				// System.out.println("" + u);
				if (u < 0) {
					// ADD U to Attacking Land
					try {
						PlaceUnitsData pud = new PlaceUnitsData(parseLandString(client.getAttacker()), u);
						client.sendToServer(pud);
						// System.out
						// .println("remove " + u + " from " + parseLandString(client.getAttacker()));
					} catch (IOException e) {
						System.out.println("Error adding U to attacking land");
					}

					aInt += u;
				} else if (u > 0) {// ADD U to Defending Land
					try {
						PlaceUnitsData pud = new PlaceUnitsData(client.getTarget(), u * -1);
						client.sendToServer(pud);
						// System.out.println("remove " + u + " from " +
						// parseLandString(client.getTarget()));
					} catch (IOException e) {
						System.out.println("Error adding U to defending land");
					}

					dInt -= u;
				} else if (u == 0) {
					try {
						PlaceUnitsData pud = new PlaceUnitsData(parseLandString(client.getAttacker()), -1);
						client.sendToServer(pud);
						// System.out
						// .println("remove " + u + " from " + parseLandString(client.getAttacker()));
					} catch (IOException e) {
						System.out.println("Error adding -1 to attacking land");
					}
					try {
						PlaceUnitsData pud = new PlaceUnitsData(client.getTarget(), -1);
						client.sendToServer(pud);
						// System.out.println("remove " + u + " from " +
						// parseLandString(client.getTarget()));
					} catch (IOException e) {
						System.out.println("Error adding -1 to defending land");
					}

					aInt -= 1;
					dInt -= 1;
				}
				// System.out.println("aInt: " + aInt);
				// System.out.println("dInt: " + dInt);
			}

			// runs for change of ownership
			if (dInt < 1) {
				GamePanel.setTextSouthPanelTitleLabel("Victory!: ");
				GamePanel.setTextSouthPanelMsgLabel("You claimed: " + parseLandString(client.getTarget()));
				System.out.println("You have " + client.getCountries().size() + " lands señor(ita)");
				if (client.getCountries().size() >= MAP_LAND_COUNT - 1) { // 42
					System.out.println("You have all 42 lands");
					try {
						client.sendToServer("GAMEOVER;" + client.getColor());
						System.out.println("GAMEOVER sent to serber");
					} catch (IOException e) {
						System.out.println("Could not send \"GameOver\" to server.");
					}
				}

				// change color Add to Attacker's list
				// need land, color, unit#
				client.addCountry(parseLandString(client.getTarget()));
				for (int i = 0; i < countries.size(); i++) {
					if (countries.get(i).equals(parseLandString(client.getTarget()))) {
						GamePanel.colorCountry(countries.get(i), client.getColor());
					}
				}

				PlaceUnitsData pud = new PlaceUnitsData(parseLandString(client.getAttacker()), (aInt - 1) * -1);
				try {
					client.sendToServer(pud);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				ChangeCountryOwnershipData ccod = new ChangeCountryOwnershipData(parseLandString(client.getTarget()),
						client.getColor(), aInt - 1, colorOfTarget);
				try {
					client.sendToServer(ccod);
				} catch (IOException e) {
					e.printStackTrace();
				}
				// reset attacker and target
				client.setAttacker("");
				client.setTarget("");
				if (client.getAttackSkip()) {
					return;
				}
			} // end change owner
			else {
				GamePanel.setTextSouthPanelTitleLabel("Defeat: ");
				GamePanel.setTextSouthPanelMsgLabel("You were unable to claim: " + parseLandString(client.getTarget()));
				client.setAttacker("");
				client.setTarget("");
				attack(command);
			}

		} // end actual attack logic
		System.out.println("Attacker: " + parseLandString(client.getAttacker()));
		System.out.println("Target: " + client.getTarget());

	}

	private int battle(int offenseDice, int defenseDice) {
		int a, d;
		if (offenseDice < 2 || defenseDice < 2) {// only 1 comparison
			if (offenseDice == 3) {
				a = Math.max(randy.nextInt(7), Math.max(randy.nextInt(7), randy.nextInt(7)));
			} else if (offenseDice == 2) {
				a = Math.max(randy.nextInt(7), randy.nextInt(7));
			} else
				a = randy.nextInt(7);
			if (defenseDice == 2) {
				d = Math.max(randy.nextInt(7), randy.nextInt(7));
			} else {
				d = randy.nextInt(7);
			}
			if (a > d) {
				return 1; // Target loses a unit
			} else {
				return -1; // Attacker loses a unit
			}
		} else {// for 2 comparisons
			int a1, d1;
			if (offenseDice == 3) {
				a = randy.nextInt(7);
				a1 = randy.nextInt(7);
				int t = randy.nextInt(7);
				t = randy.nextInt(7);
				if (t > a) {
					a = t;
				} else {
					a1 = Math.max(a1, t);
				}
			} else {// offenseDice == 2
				a = randy.nextInt(7);
				a1 = randy.nextInt(7);
			}
			d = randy.nextInt(7);
			d1 = randy.nextInt(7);

			if (a > d && a1 > d1) {
				return 2;
			}
			if (a <= d && a1 <= d1) {
				return -2;
			}

		}
		return 0;
	}

	private String parseLandString(String s) {
		String toR = s;
		// Sample text: <html>Central America<br>14<html>
		toR = toR.replace("<html>", "");
		// Sample text: Central America<br>14
		if (toR.contains("<")) {
			toR = toR.substring(0, toR.indexOf("<"));
		}
		// Sample text: Central America

		return toR;
	}

	private int parseUnitsString(String s) {
		String toR = s;
		// Sample text: <html>Central America<br>14<html>
		toR = toR.replace("<html>", "");
		// Sample text: Central America<br>14
		if (toR.contains(">")) {
			toR = toR.substring(toR.indexOf(">") + 1);
		}
		// Sample text: 14

		return Integer.parseInt(toR);
	}
}
