package net.codejava;

import java.awt.Color;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class FilterViews {
	// ACTUALLY UNUSED
	private Settings settings;
	
    String filterSelected = "Biome";
    
    HashMap<String, Color> biomeColors = new HashMap<String, Color>();
    HashMap<String, Boolean> fiberDisplayed = new HashMap<String, Boolean>();
    HashMap<String, Boolean> sporeDisplayed = new HashMap<String, Boolean>();
    HashMap<String, Boolean> mineralDisplayed = new HashMap<String, Boolean>();
//    HashMap<String, Boolean> coinsDisplayed = new HashMap<String, Boolean>();

	public FilterViews(Settings settings) {
		this.settings = settings;
		// Construction des hashmaps
		// Couleurs des biomes
		biomeColors.put("Brume", Color.white);
		biomeColors.put("Plaine", Color.yellow);
		biomeColors.put("Hauteurs", Color.gray);
		biomeColors.put("Foret", Color.green);
		biomeColors.put("Désert", Color.orange);
		biomeColors.put("Marais", Color.pink);

		// For filter fibre
		fiberDisplayed.put("Brume", true);
		fiberDisplayed.put("Foret", true);
		fiberDisplayed.put("Marais", true);
		
		sporeDisplayed.put("Brume", true);
		sporeDisplayed.put("Foret", true);
		sporeDisplayed.put("Plaine", true);
		sporeDisplayed.put("Marais", true);
		
		mineralDisplayed.put("Brume", true);
		mineralDisplayed.put("Désert", true);
		mineralDisplayed.put("Hauteurs", true);
	}

	public void updateCaseView(TuileCase tuileCase) {
		// Getting necessary infos
		JButton caseBackground = tuileCase.caseBackground;
		Tuile parent = tuileCase.parentTuile;
		String terrain = tuileCase.terrain;
		String building = tuileCase.building;
		
		caseBackground.setIcon(null);
		caseBackground.setText("");

		switch(filterSelected) {
			case "Player":
				Color playerColor = parent.owner.playerColor;
				caseToPlayerView(caseBackground, playerColor);
				break;			
			case "Biome":
				caseToBiomeView(caseBackground, terrain, building);
		    	break;
			case "Fibre":
				int prodFibre = tuileCase.prodFibre;
				caseBackground.setText("" + prodFibre);		
				caseToBiomeColor(caseBackground, terrain, fiberDisplayed);
				break;
			case "Spore":
				int prodSpore = tuileCase.prodSpore;
				caseBackground.setText("" + prodSpore);
				caseToBiomeColor(caseBackground, terrain, sporeDisplayed);
				break;
			case "Minéraux":
				int prodMineral = tuileCase.prodMineral;
				caseBackground.setText("" + prodMineral);
				caseToBiomeColor(caseBackground, terrain, mineralDisplayed);
				break;
			case "MushCoins":
				int prodCoins = tuileCase.prodCoins;
				caseBackground.setText("" + prodCoins);
				caseToBiomeColor(caseBackground, terrain);
		    	break;
		    default:
				Main.settings.AddDebugLog("This filter is unknown: " + filterSelected);
		}
		caseBackground.setVerticalAlignment(SwingConstants.TOP);
		
	}
	
	void caseToBiomeView(JButton caseBackground, String terrain, String building) {
		caseToBiomeColor(caseBackground, terrain);
		switch(terrain) {
	    	case "Brume":
			    caseBackground.setIcon(new ImageIcon(Main.settings.imgEau));
				break;
	    	case "Foret":
			    caseBackground.setIcon(new ImageIcon(Main.settings.imgForet));
				break;
		}
		switch (building) {
			case "Ferme":
			    caseBackground.setIcon(new ImageIcon(Main.settings.imgFarm));
	    		break;
			case "Port":
			    caseBackground.setIcon(new ImageIcon(Main.settings.imgPort));
	    		break;
		}
	}
	
	void caseToBiomeColor(JButton caseBackground, String terrain) {
		caseBackground.setBackground(biomeColors.get(terrain));
	}

	void caseToBiomeColor(JButton caseBackground, String terrain, HashMap<String, Boolean> biomeColorsDisplayed) {
		Boolean colorToDisplay = biomeColorsDisplayed.get(terrain);
		if (colorToDisplay == null || !colorToDisplay) {
			caseBackground.setBackground(Color.lightGray);
		} else {
			caseBackground.setBackground(biomeColors.get(terrain));
		}
	}

	void caseToPlayerView(JButton caseBackground, Color playerColor) {
		caseBackground.setBackground(playerColor);
	}

}
