package project;

import java.io.Serializable;

public class PlaceUnitsData implements Serializable {

	private String land;
	private int units;
	private boolean setupPhase;

	public PlaceUnitsData(String L, int u) {
		land = L;
		units = u;
		setupPhase = false;
	}

	public PlaceUnitsData(String L, int u, boolean s) {
		land = L;
		units = u;
		setupPhase = s;
	}

	public boolean isSetupPhase() {
		return setupPhase;
	}

	public void setSetupPhase(boolean b) {
		setupPhase = b;
	}

	public String getLand() {
		String landName = land;
		// <html>North Territory<br>0<html>
		landName = landName.replace("<html>", "");
		// North Territory<br>0
		if (landName.contains("<")) {
			landName = landName.substring(0, landName.indexOf("<"));
		}
		return landName;
	}

	public int getUnits() {
		return units;
	}

	public String toString() {
		if (units > 0) {
			return "ADD" + getLand() + ";" + units;
		} else {
			// Change to "SUB" if implemented in GameClient
			return "SUB" + getLand() + ";" + units * -1;
		}

	}
}
