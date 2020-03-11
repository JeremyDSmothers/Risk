package project;

import java.io.Serializable;

public class ChangeCountryOwnershipData implements Serializable {

	private String land;
	private String color;
	private int units;
	private String oldColor;

	public ChangeCountryOwnershipData(String l, String c, int u, String oc) {
		land = l;
		color = c;
		units = u;
		oldColor = oc;
	}

	public String toString() {
		return "CHANGE" + oldColor + ";" + color + ";" + units + ';' +  land;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getUnits() {
		return units;
	}

	public void setUnits(int units) {
		this.units = units;
	}

	public String getOldColor() {
		return oldColor;
	}

	public void setOldColor(String oldColor) {
		this.oldColor = oldColor;
	}

}
