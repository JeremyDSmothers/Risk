package project.Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import project.Country;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import java.awt.Graphics;
//import project.GameClient;

public class GamePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static ArrayList<Country> countries;
	private ArrayList<CardPanel> deck;
	private static JSpinner troopCount;
	private static JButton btnSubmitFortification;
	// private GameClient client;

	// country click
	private JLabel leftPanelNameLabel;
	private static JLabel SouthPanelTitleLabel;
	private static JLabel SouthPanelMsgLabel;

	private GameControl gc;

	private boolean drawLine;

	@SuppressWarnings("unchecked")
	public GamePanel(GameControl gc) {
		this.gc = gc;
		setLayout(new BorderLayout(0, 0));
		countries = new ArrayList<>();
		deck = new ArrayList<>();
		JPanel leftInfoPanel = new JPanel();
		add(leftInfoPanel, BorderLayout.WEST);
		leftInfoPanel.setLayout(
				new FormLayout(new ColumnSpec[] { FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, ColumnSpec.decode("160px"), },
						new RowSpec[] { FormSpecs.LINE_GAP_ROWSPEC, RowSpec.decode("1px"),
								FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
								FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
								FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
								FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
								FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
								FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
								FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
								FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
								FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
								FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
								FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
								FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
								FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
								FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
								FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
								FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
								FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
								FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
								FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
								FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
								FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, }));
		leftInfoPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 2));

		Component leftStrut = Box.createHorizontalStrut(160);
		leftInfoPanel.add(leftStrut, "2, 2, left, fill");

		JLabel lblName = new JLabel("Country Name: ");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 18));
		leftInfoPanel.add(lblName, "2, 4, center, default");

		leftPanelNameLabel = new JLabel("0");
		leftInfoPanel.add(leftPanelNameLabel, "2, 6, center, default");

		JButton btnEndAttack = new JButton("End Attack");
		leftInfoPanel.add(btnEndAttack, "2, 46");
		btnEndAttack.addActionListener(gc);

		troopCount = new JSpinner();
		troopCount.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		leftInfoPanel.add(troopCount, "2, 50");

		btnSubmitFortification = new JButton("Submit Fortification");
		btnSubmitFortification.setEnabled(false);
		leftInfoPanel.add(btnSubmitFortification, "2, 52");
		btnSubmitFortification.addActionListener(gc);

		JButton btnBack = new JButton("Back");
		leftInfoPanel.add(btnBack, "2, 56");
		btnBack.addActionListener(gc);

		JPanel southInfoPanel = new JPanel();
		add(southInfoPanel, BorderLayout.SOUTH);
		southInfoPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 2));

		SouthPanelTitleLabel = new JLabel("SouthPanelLabel: ");
		SouthPanelTitleLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
		southInfoPanel.add(SouthPanelTitleLabel);

		SouthPanelMsgLabel = new JLabel("Waiting on players");
		SouthPanelMsgLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		southInfoPanel.add(SouthPanelMsgLabel);

		JPanel boardPanel = new JPanel();
		add(boardPanel, BorderLayout.CENTER);
		boardPanel.setLayout(null);
		boardPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 2));

		instantiateCountryList(boardPanel);

	}

	// @Override
	// protected void paintComponent(Graphics g) {
	// super.paint(g);
	// g.drawLine(0, 0, 100, 100);
	// }

	public static void setTextSouthPanelTitleLabel(String s) {
		SouthPanelTitleLabel.setText(s);
	}

	public static void setTextSouthPanelMsgLabel(String s) {
		SouthPanelMsgLabel.setText(s);
	}

	public static void increase(String country, int units) { // May be obsolete
		for (int i = 0; i < countries.size(); i++) {
			if (countries.get(i).getName().equals(country)) {
				countries.get(i).setTroops(countries.get(i).getTroops() + units);
				countries.get(i).updateLabel(); //
			}
		}
	}

	public ArrayList<CardPanel> getDeck() {
		return deck;
	}

	public ArrayList<Country> getCountries() {
		return countries;
	}

	public static void setCountryUnits(String l, int u) {
		for (int i = 0; i < countries.size(); i++) {
			if (countries.get(i).getName().equals(l)) {
				countries.get(i).setTroops(u);
				countries.get(i).updateLabel();
			}
		}
	}

	// country click
	public JLabel getLeftPanelNameLabel() {
		return leftPanelNameLabel;
	}

	public JLabel getSouthPanelTitleLabel() {
		return SouthPanelTitleLabel;
	}

	public JLabel getSouthPanelMsgLabel() {
		return SouthPanelMsgLabel;
	}

	public void setSouthPanelMsgLabel(String s) {
		SouthPanelMsgLabel.setText(s);
	}

	// setup
	private void instantiateCountryList(JPanel boardPanel) {

		// North America - 9 Countries

		JButton btnAlaska = new JButton("Alaska");
		btnAlaska.setBounds(0, 51, 91, 106);
		btnAlaska.addActionListener(gc);
		boardPanel.add(btnAlaska);
		Country Alaska = new Country(btnAlaska);
		Alaska.setGroup("NorthAmerica");

		JButton btnAlberta = new JButton("Alberta");
		btnAlberta.setBounds(92, 148, 122, 51);
		btnAlberta.addActionListener(gc);
		boardPanel.add(btnAlberta);
		Country Alberta = new Country(btnAlberta);
		Alberta.setGroup("NorthAmerica");

		JButton btnCentralAmerica = new JButton("Central America");
		btnCentralAmerica.setBounds(150, 392, 194, 162);
		btnCentralAmerica.addActionListener(gc);
		boardPanel.add(btnCentralAmerica);
		Country CentralAmerica = new Country(btnCentralAmerica);
		CentralAmerica.setGroup("NorthAmerica");

		JButton btnEasternUnitedStates = new JButton("Eastern United States");
		btnEasternUnitedStates.setBounds(239, 201, 162, 178);
		btnEasternUnitedStates.addActionListener(gc);
		boardPanel.add(btnEasternUnitedStates);
		Country EasternUnitedStates = new Country(btnEasternUnitedStates);
		EasternUnitedStates.setGroup("NorthAmerica");

		JButton btnGreenland = new JButton("Greenland");
		btnGreenland.setBounds(226, 34, 244, 65);
		btnGreenland.addActionListener(gc);
		boardPanel.add(btnGreenland);
		Country Greenland = new Country(btnGreenland);
		Greenland.setGroup("NorthAmerica");

		JButton btnNorthWestTerritory = new JButton("North West Territory");
		btnNorthWestTerritory.setBounds(92, 51, 122, 97);
		btnNorthWestTerritory.addActionListener(gc);
		boardPanel.add(btnNorthWestTerritory);
		Country NorthWestTerritory = new Country(btnNorthWestTerritory);
		NorthWestTerritory.setGroup("NorthAmerica");

		JButton btnOntario = new JButton("Ontario");
		btnOntario.setBounds(215, 102, 81, 97);
		btnOntario.addActionListener(gc);
		boardPanel.add(btnOntario);
		Country Ontario = new Country(btnOntario);
		Ontario.setGroup("NorthAmerica");

		JButton btnQuebec = new JButton("Quebec");
		btnQuebec.setBounds(300, 121, 97, 78);
		btnQuebec.addActionListener(gc);
		boardPanel.add(btnQuebec);
		Country Quebec = new Country(btnQuebec);
		Quebec.setGroup("NorthAmerica");

		JButton btnWesternUnitedStates = new JButton("Western United States");
		btnWesternUnitedStates.setBounds(75, 201, 162, 178);
		btnWesternUnitedStates.addActionListener(gc);
		boardPanel.add(btnWesternUnitedStates);
		Country WesternUnitedStates = new Country(btnWesternUnitedStates);
		WesternUnitedStates.setGroup("NorthAmerica");

		// South America - 4 Countries

		JButton btnArgentina = new JButton("Argentina");
		btnArgentina.setBounds(285, 712, 149, 140);
		btnArgentina.addActionListener(gc);
		boardPanel.add(btnArgentina);
		Country Argentina = new Country(btnArgentina);
		Argentina.setGroup("SouthAmerica");

		JButton btnBrazil = new JButton("Brazil");
		btnBrazil.setBounds(285, 612, 162, 97);
		btnBrazil.addActionListener(gc);
		boardPanel.add(btnBrazil);
		Country Brazil = new Country(btnBrazil);
		Brazil.setGroup("SouthAmerica");

		JButton btnPeru = new JButton("Peru");
		btnPeru.setBounds(171, 612, 111, 148);
		btnPeru.addActionListener(gc);
		boardPanel.add(btnPeru);
		Country Peru = new Country(btnPeru);
		Peru.setGroup("SouthAmerica");

		JButton btnVenezuela = new JButton("Venezuela");
		btnVenezuela.setBounds(215, 558, 130, 51);
		btnVenezuela.addActionListener(gc);
		boardPanel.add(btnVenezuela);
		Country Venezuela = new Country(btnVenezuela);
		Venezuela.setGroup("SouthAmerica");

		// Europe - 7 Countries

		JButton btnGreatBritain = new JButton("Great Britain");
		btnGreatBritain.setBounds(546, 285, 120, 120);
		btnGreatBritain.addActionListener(gc);
		boardPanel.add(btnGreatBritain);
		Country GreatBritain = new Country(btnGreatBritain);
		GreatBritain.setGroup("Europe");

		JButton btnIceland = new JButton("Iceland");
		btnIceland.setBounds(482, 102, 150, 115);
		btnIceland.addActionListener(gc);
		boardPanel.add(btnIceland);
		Country Iceland = new Country(btnIceland);
		Iceland.setGroup("Europe");

		JButton btnNorthernEurope = new JButton("Northern Europe");
		btnNorthernEurope.setBounds(672, 372, 132, 45);
		btnNorthernEurope.addActionListener(gc);
		boardPanel.add(btnNorthernEurope);
		Country NorthernEurope = new Country(btnNorthernEurope);
		NorthernEurope.setGroup("Europe");

		JButton btnScandinavia = new JButton("Scandinavia");
		btnScandinavia.setBounds(670, 201, 119, 158);
		btnScandinavia.addActionListener(gc);
		boardPanel.add(btnScandinavia);
		Country Scandinavia = new Country(btnScandinavia);
		Scandinavia.setGroup("Europe");

		JButton btnSouthernEurope = new JButton("Southern Europe");
		btnSouthernEurope.setBounds(672, 420, 132, 45);
		btnSouthernEurope.addActionListener(gc);
		boardPanel.add(btnSouthernEurope);
		Country SouthernEurope = new Country(btnSouthernEurope);
		SouthernEurope.setGroup("Europe");

		JButton btnUkraine = new JButton("Ukraine");
		btnUkraine.setBounds(790, 210, 134, 160);
		btnUkraine.addActionListener(gc);
		boardPanel.add(btnUkraine);
		Country Ukraine = new Country(btnUkraine);
		Ukraine.setGroup("Europe");

		JButton btnWesternEurope = new JButton("Western Europe");
		btnWesternEurope.setBounds(534, 410, 130, 65);
		btnWesternEurope.addActionListener(gc);
		boardPanel.add(btnWesternEurope);
		Country WesternEurope = new Country(btnWesternEurope);
		WesternEurope.setGroup("Europe");

		// Africa - 6 Countries

		JButton btnCongo = new JButton("Congo");
		btnCongo.setBounds(647, 628, 172, 117);
		btnCongo.addActionListener(gc);
		boardPanel.add(btnCongo);
		Country Congo = new Country(btnCongo);
		Congo.setGroup("Africa");

		JButton btnEastAfrica = new JButton("East Africa");
		btnEastAfrica.setBounds(822, 628, 102, 117);
		btnEastAfrica.addActionListener(gc);
		boardPanel.add(btnEastAfrica);
		Country EastAfrica = new Country(btnEastAfrica);
		EastAfrica.setGroup("Africa");

		JButton btnEgypt = new JButton("Egypt");
		btnEgypt.setBounds(722, 540, 202, 87);
		btnEgypt.addActionListener(gc);
		boardPanel.add(btnEgypt);
		Country Egypt = new Country(btnEgypt);
		Egypt.setGroup("Africa");

		JButton btnMadagascar = new JButton("Madagascar");
		btnMadagascar.setBounds(926, 776, 112, 36);
		btnMadagascar.addActionListener(gc);
		boardPanel.add(btnMadagascar);
		Country Madagascar = new Country(btnMadagascar);
		Madagascar.setGroup("Africa");

		JButton btnNorthAfrica = new JButton("North Africa");
		btnNorthAfrica.setBounds(546, 521, 176, 106);
		btnNorthAfrica.addActionListener(gc);
		boardPanel.add(btnNorthAfrica);
		Country NorthAfrica = new Country(btnNorthAfrica);
		NorthAfrica.setGroup("Africa");

		JButton btnSouthAfrica = new JButton("South Africa");
		btnSouthAfrica.setBounds(696, 746, 202, 97);
		btnSouthAfrica.addActionListener(gc);
		boardPanel.add(btnSouthAfrica);
		Country SouthAfrica = new Country(btnSouthAfrica);
		SouthAfrica.setGroup("Africa");

		// Asia - 12 Countries
		JButton btnAfghanistan = new JButton("Afghanistan");
		btnAfghanistan.setBounds(924, 307, 104, 162);
		btnAfghanistan.addActionListener(gc);
		boardPanel.add(btnAfghanistan);
		Country Afghanistan = new Country(btnAfghanistan);
		Afghanistan.setGroup("Asia");

		JButton btnChina = new JButton("China");
		btnChina.setBounds(1031, 372, 154, 106);
		btnChina.addActionListener(gc);
		boardPanel.add(btnChina);
		Country China = new Country(btnChina);
		China.setGroup("Asia");

		JButton btnIndia = new JButton("India");
		btnIndia.setBounds(926, 482, 162, 45);
		btnIndia.addActionListener(gc);
		boardPanel.add(btnIndia);
		Country India = new Country(btnIndia);
		India.setGroup("Asia");

		JButton btnIrkutsk = new JButton("Irkutsk");
		btnIrkutsk.setBounds(1040, 121, 176, 117);
		btnIrkutsk.addActionListener(gc);
		boardPanel.add(btnIrkutsk);
		Country Irkutsk = new Country(btnIrkutsk);
		Irkutsk.setGroup("Asia");

		JButton btnJapan = new JButton("Japan");
		btnJapan.setBounds(1218, 315, 89, 49);
		btnJapan.addActionListener(gc);
		boardPanel.add(btnJapan);
		Country Japan = new Country(btnJapan);
		Japan.setGroup("Asia");

		JButton btnKamchatka = new JButton("Kamchatka");
		btnKamchatka.setBounds(1219, 34, 108, 271);
		btnKamchatka.addActionListener(gc);
		boardPanel.add(btnKamchatka);
		Country Kamchatka = new Country(btnKamchatka);
		Kamchatka.setGroup("Asia");

		JButton btnMiddleEast = new JButton("Middle East");
		btnMiddleEast.setBounds(810, 376, 109, 162);
		btnMiddleEast.addActionListener(gc);
		boardPanel.add(btnMiddleEast);
		Country MiddleEast = new Country(btnMiddleEast);
		MiddleEast.setGroup("Asia");

		JButton btnMongolia = new JButton("Mongolia");
		btnMongolia.setBounds(1036, 252, 171, 117);
		btnMongolia.addActionListener(gc);
		boardPanel.add(btnMongolia);
		Country Mongolia = new Country(btnMongolia);
		Mongolia.setGroup("Asia");

		JButton btnSiam = new JButton("Siam");
		btnSiam.setBounds(1088, 482, 73, 75);
		btnSiam.addActionListener(gc);
		boardPanel.add(btnSiam);
		Country Siam = new Country(btnSiam);
		Siam.setGroup("Asia");

		JButton btnSiberia = new JButton("Siberia");
		btnSiberia.setBounds(942, 34, 97, 148);
		btnSiberia.addActionListener(gc);
		boardPanel.add(btnSiberia);
		Country Siberia = new Country(btnSiberia);
		Siberia.setGroup("Asia");

		JButton btnUral = new JButton("Ural");
		btnUral.setBounds(937, 190, 91, 104);
		btnUral.addActionListener(gc);
		boardPanel.add(btnUral);
		Country Ural = new Country(btnUral);
		Ural.setGroup("Asia");

		JButton btnYakutsk = new JButton("Yakutsk");
		btnYakutsk.setBounds(1051, 34, 171, 83);
		btnYakutsk.addActionListener(gc);
		boardPanel.add(btnYakutsk);
		Country Yakutsk = new Country(btnYakutsk);
		Yakutsk.setGroup("Asia");

		// Australia - 4
		JButton btnEasternAustralia = new JButton("Eastern Australia");
		btnEasternAustralia.setBounds(1219, 674, 122, 137);
		btnEasternAustralia.addActionListener(gc);
		boardPanel.add(btnEasternAustralia);
		Country EasternAustralia = new Country(btnEasternAustralia);
		EasternAustralia.setGroup("Australia");

		JButton btnIndonesia = new JButton("Indonesia");
		btnIndonesia.setBounds(1070, 605, 146, 56);
		btnIndonesia.addActionListener(gc);
		boardPanel.add(btnIndonesia);
		Country Indonesia = new Country(btnIndonesia);
		Indonesia.setGroup("Australia");

		JButton btnNewGuinea = new JButton("New Guinea");
		btnNewGuinea.setBounds(1259, 616, 108, 36);
		btnNewGuinea.addActionListener(gc);
		boardPanel.add(btnNewGuinea);
		Country NewGuinea = new Country(btnNewGuinea);
		NewGuinea.setGroup("Australia");

		JButton btnWesternAustralia = new JButton("Western Australia");
		btnWesternAustralia.setBounds(1092, 674, 130, 137);
		btnWesternAustralia.addActionListener(gc);
		boardPanel.add(btnWesternAustralia);
		Country WesternAustralia = new Country(btnWesternAustralia);
		WesternAustralia.setGroup("Australia");

		// North America Connections
		Alaska.addAdjacentCountry(Kamchatka);
		Alaska.addAdjacentCountry(NorthWestTerritory);
		Alaska.addAdjacentCountry(Alberta);

		Alberta.addAdjacentCountry(Alaska);
		Alberta.addAdjacentCountry(NorthWestTerritory);
		Alberta.addAdjacentCountry(Ontario);
		Alberta.addAdjacentCountry(WesternUnitedStates);

		CentralAmerica.addAdjacentCountry(WesternUnitedStates);
		CentralAmerica.addAdjacentCountry(EasternUnitedStates);
		CentralAmerica.addAdjacentCountry(Venezuela);

		EasternUnitedStates.addAdjacentCountry(Quebec);
		EasternUnitedStates.addAdjacentCountry(Ontario);
		EasternUnitedStates.addAdjacentCountry(WesternUnitedStates);
		EasternUnitedStates.addAdjacentCountry(CentralAmerica);

		Greenland.addAdjacentCountry(Iceland);
		Greenland.addAdjacentCountry(NorthWestTerritory);
		Greenland.addAdjacentCountry(Ontario);
		Greenland.addAdjacentCountry(Quebec);

		NorthWestTerritory.addAdjacentCountry(Alaska);
		NorthWestTerritory.addAdjacentCountry(Alberta);
		NorthWestTerritory.addAdjacentCountry(Ontario);
		NorthWestTerritory.addAdjacentCountry(Greenland);

		Ontario.addAdjacentCountry(Greenland);
		Ontario.addAdjacentCountry(Quebec);
		Ontario.addAdjacentCountry(NorthWestTerritory);
		Ontario.addAdjacentCountry(Alberta);
		Ontario.addAdjacentCountry(WesternUnitedStates);
		Ontario.addAdjacentCountry(EasternUnitedStates);

		Quebec.addAdjacentCountry(Greenland);
		Quebec.addAdjacentCountry(Ontario);
		Quebec.addAdjacentCountry(EasternUnitedStates);

		WesternUnitedStates.addAdjacentCountry(Alberta);
		WesternUnitedStates.addAdjacentCountry(Ontario);
		WesternUnitedStates.addAdjacentCountry(EasternUnitedStates);
		WesternUnitedStates.addAdjacentCountry(CentralAmerica);

		// South America Connections
		Argentina.addAdjacentCountry(Brazil);
		Argentina.addAdjacentCountry(Peru);

		Brazil.addAdjacentCountry(Venezuela);
		Brazil.addAdjacentCountry(Peru);
		Brazil.addAdjacentCountry(Argentina);
		Brazil.addAdjacentCountry(NorthAfrica);

		Peru.addAdjacentCountry(Venezuela);
		Peru.addAdjacentCountry(Brazil);
		Peru.addAdjacentCountry(Argentina);

		Venezuela.addAdjacentCountry(CentralAmerica);
		Venezuela.addAdjacentCountry(Brazil);
		Venezuela.addAdjacentCountry(Peru);

		// Europe Connections
		GreatBritain.addAdjacentCountry(Iceland);
		GreatBritain.addAdjacentCountry(Scandinavia);
		GreatBritain.addAdjacentCountry(NorthernEurope);
		GreatBritain.addAdjacentCountry(WesternEurope);

		Iceland.addAdjacentCountry(Greenland);
		Iceland.addAdjacentCountry(GreatBritain);
		Iceland.addAdjacentCountry(Scandinavia);

		NorthernEurope.addAdjacentCountry(GreatBritain);
		NorthernEurope.addAdjacentCountry(Scandinavia);
		NorthernEurope.addAdjacentCountry(Ukraine);
		NorthernEurope.addAdjacentCountry(WesternEurope);
		NorthernEurope.addAdjacentCountry(SouthernEurope);
		NorthernEurope.addAdjacentCountry(MiddleEast);

		Scandinavia.addAdjacentCountry(Iceland);
		Scandinavia.addAdjacentCountry(GreatBritain);
		Scandinavia.addAdjacentCountry(NorthernEurope);
		Scandinavia.addAdjacentCountry(Ukraine);

		SouthernEurope.addAdjacentCountry(NorthernEurope);
		SouthernEurope.addAdjacentCountry(WesternEurope);
		SouthernEurope.addAdjacentCountry(Egypt);
		SouthernEurope.addAdjacentCountry(NorthAfrica);
		SouthernEurope.addAdjacentCountry(MiddleEast);

		Ukraine.addAdjacentCountry(Scandinavia);
		Ukraine.addAdjacentCountry(NorthernEurope);
		Ukraine.addAdjacentCountry(MiddleEast);
		Ukraine.addAdjacentCountry(Afghanistan);
		Ukraine.addAdjacentCountry(Ural);

		WesternEurope.addAdjacentCountry(GreatBritain);
		WesternEurope.addAdjacentCountry(NorthernEurope);
		WesternEurope.addAdjacentCountry(SouthernEurope);
		WesternEurope.addAdjacentCountry(NorthAfrica);

		// Africa Connections
		Congo.addAdjacentCountry(NorthAfrica);
		Congo.addAdjacentCountry(EastAfrica);
		Congo.addAdjacentCountry(SouthAfrica);
		Congo.addAdjacentCountry(Egypt);

		EastAfrica.addAdjacentCountry(Egypt);
		EastAfrica.addAdjacentCountry(Congo);
		EastAfrica.addAdjacentCountry(SouthAfrica);
		EastAfrica.addAdjacentCountry(Madagascar);

		Egypt.addAdjacentCountry(MiddleEast);
		Egypt.addAdjacentCountry(SouthernEurope);
		Egypt.addAdjacentCountry(NorthAfrica);
		Egypt.addAdjacentCountry(EastAfrica);
		Egypt.addAdjacentCountry(Congo);

		Madagascar.addAdjacentCountry(EastAfrica);
		Madagascar.addAdjacentCountry(SouthAfrica);

		NorthAfrica.addAdjacentCountry(Brazil);
		NorthAfrica.addAdjacentCountry(WesternEurope);
		NorthAfrica.addAdjacentCountry(SouthernEurope);
		NorthAfrica.addAdjacentCountry(Egypt);
		NorthAfrica.addAdjacentCountry(Congo);

		SouthAfrica.addAdjacentCountry(Madagascar);
		SouthAfrica.addAdjacentCountry(EastAfrica);
		SouthAfrica.addAdjacentCountry(Congo);

		// Asia Connections
		Afghanistan.addAdjacentCountry(Ukraine);
		Afghanistan.addAdjacentCountry(MiddleEast);
		Afghanistan.addAdjacentCountry(Ural);
		Afghanistan.addAdjacentCountry(China);
		Afghanistan.addAdjacentCountry(India);
		Afghanistan.addAdjacentCountry(Mongolia);

		China.addAdjacentCountry(India);
		China.addAdjacentCountry(Siam);
		China.addAdjacentCountry(Afghanistan);
		China.addAdjacentCountry(Mongolia);

		India.addAdjacentCountry(MiddleEast);
		India.addAdjacentCountry(Afghanistan);
		India.addAdjacentCountry(China);
		India.addAdjacentCountry(Siam);

		Irkutsk.addAdjacentCountry(Siberia);
		Irkutsk.addAdjacentCountry(Yakutsk);
		Irkutsk.addAdjacentCountry(Kamchatka);
		Irkutsk.addAdjacentCountry(Mongolia);
		Irkutsk.addAdjacentCountry(Ural);

		Japan.addAdjacentCountry(Mongolia);
		Japan.addAdjacentCountry(Kamchatka);

		Kamchatka.addAdjacentCountry(Alaska);
		Kamchatka.addAdjacentCountry(Yakutsk);
		Kamchatka.addAdjacentCountry(Irkutsk);
		Kamchatka.addAdjacentCountry(Mongolia);
		Kamchatka.addAdjacentCountry(Japan);

		MiddleEast.addAdjacentCountry(Ukraine);
		MiddleEast.addAdjacentCountry(Afghanistan);
		MiddleEast.addAdjacentCountry(India);
		MiddleEast.addAdjacentCountry(SouthernEurope);
		MiddleEast.addAdjacentCountry(Egypt);
		MiddleEast.addAdjacentCountry(NorthernEurope);

		Mongolia.addAdjacentCountry(China);
		Mongolia.addAdjacentCountry(Ural);
		Mongolia.addAdjacentCountry(Irkutsk);
		Mongolia.addAdjacentCountry(Kamchatka);
		Mongolia.addAdjacentCountry(Japan);
		Mongolia.addAdjacentCountry(Afghanistan);

		Siam.addAdjacentCountry(China);
		Siam.addAdjacentCountry(India);
		Siam.addAdjacentCountry(Indonesia);

		Siberia.addAdjacentCountry(Yakutsk);
		Siberia.addAdjacentCountry(Irkutsk);
		Siberia.addAdjacentCountry(Ural);

		Ural.addAdjacentCountry(Siberia);
		Ural.addAdjacentCountry(Mongolia);
		Ural.addAdjacentCountry(Afghanistan);
		Ural.addAdjacentCountry(Ukraine);
		Ural.addAdjacentCountry(Irkutsk);

		Yakutsk.addAdjacentCountry(Kamchatka);
		Yakutsk.addAdjacentCountry(Irkutsk);
		Yakutsk.addAdjacentCountry(Siberia);

		// Australia Connections
		EasternAustralia.addAdjacentCountry(NewGuinea);
		EasternAustralia.addAdjacentCountry(WesternAustralia);
		EasternAustralia.addAdjacentCountry(Indonesia);

		Indonesia.addAdjacentCountry(Siam);
		Indonesia.addAdjacentCountry(NewGuinea);
		Indonesia.addAdjacentCountry(EasternAustralia);
		Indonesia.addAdjacentCountry(WesternAustralia);

		NewGuinea.addAdjacentCountry(EasternAustralia);
		NewGuinea.addAdjacentCountry(WesternAustralia);
		NewGuinea.addAdjacentCountry(Indonesia);

		WesternAustralia.addAdjacentCountry(EasternAustralia);
		WesternAustralia.addAdjacentCountry(NewGuinea);
		WesternAustralia.addAdjacentCountry(Indonesia);

		countries.add(Kamchatka);
		countries.add(Yakutsk);
		countries.add(NorthAfrica);
		countries.add(Egypt);
		countries.add(Congo);
		countries.add(EastAfrica);
		countries.add(SouthAfrica);
		countries.add(WesternEurope);
		countries.add(SouthernEurope);
		countries.add(NorthernEurope);
		countries.add(GreatBritain);
		countries.add(Scandinavia);
		countries.add(Ukraine);
		countries.add(MiddleEast);
		countries.add(India);
		countries.add(Siam);
		countries.add(Indonesia);
		countries.add(NewGuinea);
		countries.add(WesternAustralia);
		countries.add(EasternAustralia);
		countries.add(Ural);
		countries.add(China);
		countries.add(Afghanistan);
		countries.add(Siberia);
		countries.add(Irkutsk);
		countries.add(Mongolia);
		countries.add(Japan);
		countries.add(Alaska);
		countries.add(NorthWestTerritory);
		countries.add(Ontario);
		countries.add(Quebec);
		countries.add(Greenland);
		countries.add(Iceland);
		countries.add(Alberta);
		countries.add(WesternUnitedStates);
		countries.add(EasternUnitedStates);
		countries.add(Madagascar);
		countries.add(Peru);
		countries.add(CentralAmerica);
		countries.add(Venezuela);
		countries.add(Brazil);
		countries.add(Argentina);
	}

	public static boolean allLandsAreChosen() {
		for (Country c : countries) {
			System.out.println(c.getName() + "  " + c.getColor().toString());
			if (c.getColor().equals(Color.GRAY.toString())) {
				return false;
			}
		}
		return true;
	}

	public static boolean isUnoccupiedLand(String landName) {
		// <html>North Territory<br>0<html>
		landName = landName.replace("<html>", "");
		// North Territory<br>0
		landName = landName.substring(0, landName.indexOf("<"));

		for (Country c : countries) {
			// System.out.println("|" + landName + "| |" + c.getName() + "|");
			if (c.getName().equals(landName)) {
				// System.out.println(c.getColor() + "| |" + (Color.GRAY.toString()));
				if (c.getColor().equals(Color.GRAY.toString())) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	public static void colorCountry(String countryName, String Color) {

		for (int i = 0; i < countries.size(); i++) {
			if (countries.get(i).getName().equals(countryName)) {
				countries.get(i).setColor(Color);
				countries.get(i).setTroops(1);
				countries.get(i).updateLabel();
			}
		}

	}

	public static void update(String countryName, String Color, int units) {
		for (int i = 0; i < countries.size(); i++) {
			if (countries.get(i).getName().equals(countryName)) {
				countries.get(i).setTroops(units);
				countries.get(i).setColor(Color); // May be obsolete
				countries.get(i).updateLabel();// .setTroopLabel
			}
		}
	}

	public static String getColorOfLand(String landName) {
		// <html>North Territory<br>0<html>
		landName = landName.replace("<html>", "");
		// North Territory<br>0
		if (landName.contains("<")) {
			landName = landName.substring(0, landName.indexOf("<"));
		}

		for (int i = 0; i < countries.size(); i++) {
			if (countries.get(i).getName().equals(landName)) {
				return countries.get(i).getColorName();
			}
		}

		return null;
	}

	public static void addUnitsToCountry(String landName, int units) {
		for (int i = 0; i < countries.size(); i++) {
			if (countries.get(i).getName().equals(landName)) {
				countries.get(i).setTroops(countries.get(i).getTroops() + units);
				countries.get(i).updateLabel();
			}
		}

	}

	public static String getGroupName(String landName) {
		for (int i = 0; i < countries.size(); i++) {
			if (countries.get(i).getName().equals(landName)) {
				return countries.get(i).getGroup();
			}
		}
		return null;
	}

	public static boolean areAdjacent(String attacker, String target) {
		ArrayList<Country> adjs = new ArrayList<Country>();
		for (int i = 0; i < countries.size(); i++) {
			if (countries.get(i).getName().equals(attacker)) {
				adjs = countries.get(i).getAdjacentCountries();
			}
		}

		for (int i = 0; i < adjs.size(); i++) {
			if (adjs.get(i).getName().equals(target)) {
				return true;
			}
		}
		return false;
	}

	public static int getFortifyUnits() {
		return (int) troopCount.getValue();
	}

	public static void setBtnSubmitFortificationEnabled(boolean status) {
		btnSubmitFortification.setEnabled(status);
	}
}
