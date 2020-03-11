package project;

import java.io.Serializable;

public class OccupyLandData implements Serializable {

	private String color;
	private String land;

	public OccupyLandData(String s1, String s2) {
		color = s1;
		land = s2;
	}

	public String getColor() {
		return color;
	}

	public String getLand() {
		return land;
	}

	public String toString() {
		return land + ";" + color;
	}

}
