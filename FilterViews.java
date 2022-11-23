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
    String [] possibleFilters = {"Biome", "Player", "Routes", "Fibre", "Spore", "Suc", "Phosphorite", "MushCoins"};
    String [] possibleActions = {"Pose de tuile", "Construire", "Détails du batiment"};
    
    // Paramètres d'affichage
    HashMap<String, Color> biomeColors = new HashMap<String, Color>();
    HashMap<String, Boolean> fiberDisplayed = new HashMap<String, Boolean>();
    HashMap<String, Boolean> sporeDisplayed = new HashMap<String, Boolean>();
    HashMap<String, Boolean> sucDisplayed = new HashMap<String, Boolean>();
    HashMap<String, Boolean> phosphoDisplayed = new HashMap<String, Boolean>();
    HashMap<String, Boolean> allDisplay = new HashMap<String, Boolean>();
    
	//images datas
    Image imgEau;
    Image imgForet;
    Image imgFarm;
    Image imgPort;
    Image imgGenerator;
    Image imgConstruct;
    Image imgRuins;
    Image imgMountain;
    Image imgDesert;
    Image imgHaze;
    Image imgSwamp;
    Image imgGrass;

	public FilterViews(Settings settings) {
		// Construction des hashmaps
		// Couleurs des biomes
		biomeColors.put("Brume", Color.white);
		biomeColors.put("Plaine", Color.yellow);
		biomeColors.put("Hauteurs", Color.gray);
		biomeColors.put("Foret", Color.green);
		biomeColors.put("Désert", Color.orange);
		biomeColors.put("Marais", Color.pink);
		
		// all colors display
		allDisplay.put("Brume", true);
		allDisplay.put("Foret", true);
		allDisplay.put("Plaine", true);
		allDisplay.put("Marais", true);
		allDisplay.put("Désert", true);
		allDisplay.put("Hauteurs", true);
		
		// For filter fibre
		fiberDisplayed.put("Brume", true);
		fiberDisplayed.put("Foret", true);
		fiberDisplayed.put("Marais", true);
		
		sporeDisplayed.put("Brume", true);
		sporeDisplayed.put("Hauteurs", true);
		sporeDisplayed.put("Plaine", true);
		
		sucDisplayed.put("Brume", true);
		sucDisplayed.put("Marais", true);
		sucDisplayed.put("Foret", true);
		
		phosphoDisplayed.put("Brume", true);
		phosphoDisplayed.put("Marais", true);
		phosphoDisplayed.put("Hauteurs", true);
		
		sizeImages();
	}

	public void updateCaseView(TuileCase tuileCase) {
		// Getting necessary infos
		JButton caseBackground = tuileCase.caseBackground;
		Tuile parent = tuileCase.parentTuile;
		String terrain = tuileCase.terrain;
		
		caseBackground.setIcon(null);
		caseBackground.setText("");

		switch(filterSelected) {
			case "Player":
				Color playerColor = parent.owner.playerColor;
				caseToPlayerView(caseBackground, playerColor);
				break;			
			case "Biome":
				caseToBiomeView(tuileCase);
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
			case "Suc":
				int prodSuc = tuileCase.prodSuc;
				caseBackground.setText("" + prodSuc);
				caseToBiomeColor(caseBackground, terrain, sucDisplayed);
				break;
			case "Phosphorite":
				int prodPhospho = tuileCase.prodPhospho;
				caseBackground.setText("" + prodPhospho);
				caseToBiomeColor(caseBackground, terrain, phosphoDisplayed);
				break;
			case "MushCoins":
				int prodCoins = tuileCase.prodCoins;
				caseBackground.setText("" + prodCoins);
				caseToBiomeColor(caseBackground, terrain);
		    	break;
			case "Routes":
				caseBackground.setText("" + tuileCase.roadLevel);
				caseToBiomeColor(caseBackground, terrain);
				break;
		    default:
				Main.settings.AddDebugLog("This filter is unknown: " + filterSelected);
		}
		caseBackground.setVerticalAlignment(SwingConstants.TOP);
		
	}
	
	void caseToBiomeView(TuileCase tuileCase) {
		JButton caseBackground = tuileCase.caseBackground;
		String terrain = tuileCase.terrain;
		caseToBiomeColor(caseBackground, terrain);

		if (!tuileCase.inConstruction.equals("")) {
		    caseBackground.setIcon(new ImageIcon(imgConstruct));
		} else if (tuileCase.isRuins) {
		    caseBackground.setIcon(new ImageIcon(imgRuins));
		} else {
			switch(terrain) {
		    	case "Brume":
				    caseBackground.setIcon(new ImageIcon(imgHaze));
					break;
		    	case "Foret":
				    caseBackground.setIcon(new ImageIcon(imgForet));
					break;
				case "Hauteurs":
					caseBackground.setIcon(new ImageIcon(imgMountain));
					break;
				case "Désert":
					caseBackground.setIcon(new ImageIcon(imgDesert));
					break;
				case "Marais":
					caseBackground.setIcon(new ImageIcon(imgSwamp));
					break;
				case "Plaine":
					caseBackground.setIcon(new ImageIcon(imgGrass));
					break;
			}
			switch (tuileCase.building) {
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
//    	imgEau = sizeImage("water.png");
    	imgForet = sizeImage("tree.png");
    	imgFarm = sizeImage("farm.png");
    	imgPort = sizeImage("port.png");
    	imgGenerator = sizeImage("generator.png");
    	imgConstruct = sizeImage("construct.png");
    	imgRuins = sizeImage("ruins.png");
    	imgMountain = sizeImage("mountain.png");
    	imgDesert = sizeImage("desert.png");
    	imgHaze = sizeImage("haze.png");
    	imgGrass = sizeImage("grass.png");
    	imgSwamp = sizeImage("swamp.png");
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
