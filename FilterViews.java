package net.codejava;

import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class FilterViews {	
    String filterSelected = "Biome";
    
    // Param�tres d'affichage
    HashMap<String, Color> biomeColors = new HashMap<String, Color>();
    HashMap<String, Boolean> fiberDisplayed = new HashMap<String, Boolean>();
    HashMap<String, Boolean> sporeDisplayed = new HashMap<String, Boolean>();
    HashMap<String, Boolean> sucDisplayed = new HashMap<String, Boolean>();
    HashMap<String, Boolean> phosphoDisplayed = new HashMap<String, Boolean>();
    HashMap<String, Boolean> allDisplay = new HashMap<String, Boolean>();
//    HashMap<String, Boolean> coinsDisplayed = new HashMap<String, Boolean>();
    
	//images datas
    Image imgEau;
    Image imgForet;
    Image imgFarm;
    Image imgPort;
    Image imgGenerator;
    Image imgConstruct;

	public FilterViews(Settings settings) {
		// Construction des hashmaps
		// Couleurs des biomes
		biomeColors.put("Brume", Color.white);
		biomeColors.put("Plaine", Color.yellow);
		biomeColors.put("Hauteurs", Color.gray);
		biomeColors.put("Foret", Color.green);
		biomeColors.put("D�sert", Color.orange);
		biomeColors.put("Marais", Color.pink);
		
		// all colors display
		allDisplay.put("Brume", true);
		allDisplay.put("Foret", true);
		allDisplay.put("Plaine", true);
		allDisplay.put("Marais", true);
		allDisplay.put("D�sert", true);
		allDisplay.put("Hauteurs", true);
		
		// For filter fibre
		fiberDisplayed.put("Brume", true);
		fiberDisplayed.put("Foret", true);
		fiberDisplayed.put("Marais", true);
		
		sporeDisplayed.put("Brume", true);
		sporeDisplayed.put("Foret", true);
		sporeDisplayed.put("Plaine", true);
		sporeDisplayed.put("Marais", true);
		
		sucDisplayed.put("Brume", true);
		sucDisplayed.put("D�sert", true);
		sucDisplayed.put("Hauteurs", true);
		
		phosphoDisplayed.put("Brume", true);
		phosphoDisplayed.put("D�sert", true);
		phosphoDisplayed.put("Hauteurs", true);
		
		sizeImages();
	}

	public void updateCaseView(TuileCase tuileCase) {
		// Getting necessary infos
		JButton caseBackground = tuileCase.caseBackground;
		Tuile parent = tuileCase.parentTuile;
		String terrain = tuileCase.terrain;
		String building = tuileCase.building;
		String inConstruction = tuileCase.inConstruction;
		
		caseBackground.setIcon(null);
		caseBackground.setText("");

		switch(filterSelected) {
			case "Player":
				Color playerColor = parent.owner.playerColor;
				caseToPlayerView(caseBackground, playerColor);
				break;			
			case "Biome":
				caseToBiomeView(caseBackground, terrain, building, inConstruction);
		    	break;
			case "Fibre":
				int prodFibre = tuileCase.prodFibre;
				caseBackground.setText("" + prodFibre);		
				caseToBiomeColor(caseBackground, terrain, allDisplay);
				break;
			case "Spore":
				int prodSpore = tuileCase.prodSpore;
				caseBackground.setText("" + prodSpore);
				caseToBiomeColor(caseBackground, terrain, allDisplay);
				break;
			case "Suc":
				int prodSuc = tuileCase.prodSuc;
				caseBackground.setText("" + prodSuc);
				caseToBiomeColor(caseBackground, terrain, allDisplay);
				break;
			case "Phosphorite":
				int prodPhospho = tuileCase.prodPhospho;
				caseBackground.setText("" + prodPhospho);
				caseToBiomeColor(caseBackground, terrain, allDisplay);
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
	
	void caseToBiomeView(JButton caseBackground, String terrain, String building, String inConstruction) {
		caseToBiomeColor(caseBackground, terrain);
		switch(terrain) {
	    	case "Brume":
			    caseBackground.setIcon(new ImageIcon(imgEau));
				break;
	    	case "Foret":
			    caseBackground.setIcon(new ImageIcon(imgForet));
				break;
		}
		switch (building) {
			case "Ferme":
			    caseBackground.setIcon(new ImageIcon(imgFarm));
	    		break;
			case "Port":
			    caseBackground.setIcon(new ImageIcon(imgPort));
	    		break;
			case "Generateur":
			    caseBackground.setIcon(new ImageIcon(imgGenerator));
	    		break;
		}
		if (!inConstruction.equals("")) {
		    caseBackground.setIcon(new ImageIcon(imgConstruct));	
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

    //IMAGE MANAGEMENT
    public void sizeImages() {
    	imgEau = sizeImage("water.png");
    	imgForet = sizeImage("tree.png");
    	imgFarm = sizeImage("farm.png");
    	imgPort = sizeImage("port.png");
    	imgGenerator = sizeImage("generator.png");
    	imgConstruct = sizeImage("construct.png");
    }
    
    Image sizeImage(String imagePath) {
    	Image scaledImg = null;
    	try {
    		Image img = ImageIO.read(getClass().getResource(imagePath));
    		scaledImg = img.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
    	} catch (IOException ex) {
    		Main.settings.AddDebugLog("" + ex);
    	}
    	return scaledImg;
    }
}
