package project;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JButton;

public class Country {
	private JButton btn;
	private String name;
	private int troops;
	private String description; // I don't think this is needed
	private Rectangle bounds;
	private String owner; // I don't think this is implemented
	private Color color;
	private String colorName;
	private ArrayList<Country> AdjacentCountry;
	private String group;

	public Country(JButton btn) {
		this.btn = btn;
		color = Color.gray;
		colorName = "Gray";
		troops = 0;
		name = btn.getText();
		updateLabel();
		this.btn.setBackground(color);
		AdjacentCountry = new ArrayList<Country>();
	}

	public void setColor(String c) {
		switch (c) {
		case "Red":
			color = Color.RED;
			colorName = "Red";
			break;
		case "Orange":
			color = Color.ORANGE;
			colorName = "Orange";
			break;
		case "Blue":
			color = Color.BLUE;
			colorName = "Blue";
			break;
		case "Green":
			color = Color.GREEN;
			colorName = "Green";
			break;
		case "Yellow":
			color = Color.YELLOW;
			colorName = "Yellow";
			break;
		case "Pink":
			color = Color.PINK;
			colorName = "Pink";
			break;
		case "Cyan":
			color = Color.CYAN;
			colorName = "Cyan";
			break;

		default:

			break;
		}

		this.btn.setBackground(color);
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String S) {
		group = S;
	}

	public String getColor() {
		return color.toString();
	}

	public ArrayList<Country> getAdjacentCountries() {
		return AdjacentCountry;
	}

	public void addAdjacentCountry(Country country) {
		AdjacentCountry.add(country);
	}

	public Country(JButton btn, String name) {
		this(btn);
		this.name = name;
	}

	public Country(JButton btn, String name, String desc) {
		this(btn, name);
		description = desc;
	}

	public void setBtnBounds(int left, int top, int right, int bottom) {
		bounds = new Rectangle(left, top, right, bottom);
		this.btn.setBounds(bounds);
	}

	public Rectangle getBtnBounds() {
		return this.btn.getBounds();
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String desc) {
		description = desc;
	}

	public void setTroops(int troops) {
		this.troops = troops;
	}

	public String getName() {
		return name;
	}

	public int getTroops() {
		return troops;
	}

	public String getDescription() {
		return description;
	}

	public JButton getButton() {
		return btn;
	}

	public void updateLabel() {
		this.btn.setText("<html>" + name + "<br>" + troops + "<html>");

	}

	public String getColorName() {
		return colorName;
	}
}
