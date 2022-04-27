package net.codejava;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class FilterViews {
	// ACTUALLY UNUSED
	private Settings settings;
	
    String filterSelected = "Biome";
    
    // Paramètres d'affichage
    HashMap<String, Color> biomeColors = new HashMap<String, Color>();
    HashMap<String, Boolean> fiberDisplayed = new HashMap<String, Boolean>();
    HashMap<String, Boolean> sporeDisplayed = new HashMap<String, Boolean>();
    HashMap<String, Boolean> sucDisplayed = new HashMap<String, Boolean>();
    HashMap<String, Boolean> phosphoDisplayed = new HashMap<String, Boolean>();
//    HashMap<String, Boolean> coinsDisplayed = new HashMap<String, Boolean>();
    
	//images datas
    Image imgEau;
    Image imgForet;
    Image imgFarm;
    Image imgPort;
    Image imgGenerator;

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
		
		sucDisplayed.put("Brume", true);
		sucDisplayed.put("Désert", true);
		sucDisplayed.put("Hauteurs", true);
		
		phosphoDisplayed.put("Brume", true);
		phosphoDisplayed.put("Désert", true);
		phosphoDisplayed.put("Hauteurs", true);
		
		sizeImages();
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
		    default:
				Main.settings.AddDebugLog("This filter is unknown: " + filterSelected);
		}
		caseBackground.setVerticalAlignment(SwingConstants.TOP);
		
	}
	
	void caseToBiomeView(JButton caseBackground, String terrain, String building) {
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
    
    public void displayGeneratorPopup (TuileCase tuileCase) {
        // General popup graphic params
    	JFrame generatorFrame = new JFrame("Generator energy");
        JDialog generatorDialog = new JDialog(generatorFrame);
    	    	
        generatorDialog.setBounds(200, 200, 500, 100);
        
        // List of possible buildings to power creation
        int buildingNumber = 0;
//        JPanel generatorPanel = new JPanel();
        List<Object[]> buildingsToPowerList = new ArrayList<Object[]>();
        
        for (TuileCase caseFromDistance : tuileCase.getCasesFromDistance(3)) {
        	String buildingFromDistance = caseFromDistance.building;
        	if (buildingFromDistance != "" && Main.settings.getPowerCons(buildingFromDistance) > 0) {
        		// Getting building position
        		Integer buildX = caseFromDistance.parentTuile.xPos * 3 + caseFromDistance.xPos;
        		Integer buildY = caseFromDistance.parentTuile.yPos * 3 + caseFromDistance.yPos;
        		Main.settings.AddDebugLog("" + buildingFromDistance);
        		JLabel buildingLabel = new JLabel(buildingFromDistance + " (" + buildX + "," + buildY +  ")");
        		// Getting how much power is needed
        		Integer powerCons = Main.settings.getPowerCons(buildingFromDistance);
                JCheckBox powerCheckBox = new JCheckBox("" + powerCons);
        		generatorDialog.add(buildingLabel);
        		generatorDialog.add(powerCheckBox);
        		Object[] buildingLine = {buildingFromDistance, buildX, buildY, powerCheckBox};
        		//buildingsToPower[buildingNumber] = buildingLine;
        		buildingsToPowerList.add(buildingLine);
        		buildingNumber++;
        		// Check if the building is already powered by this generator, and check box if so
        		for (Object [] buildingPowered : tuileCase.buildingsPowered) {
        			// TODO: Check for non possible buildings
        			// TODO: Check for building already powered
        			if ((String)buildingPowered[0] == buildingFromDistance && (Integer)buildingPowered[1] == buildX && (Integer)buildingPowered[2] == buildY) {
        				powerCheckBox.setSelected((boolean) buildingPowered[3]);
        			}
        		}
        	}
        }
        
        
        
        if (buildingNumber == 0) {
    		JLabel noBuildingLabel = new JLabel("No building to power in the area");
    		generatorDialog.add(noBuildingLabel);
        }
        else {
        	Object[][] buildingsToPower = new Object[buildingsToPowerList.size()][4];
        	buildingsToPowerList.toArray(buildingsToPower);
        	JButton applyPower = new JButton("Apply power connections");
    		generatorDialog.add(applyPower);
    		applyPower.addActionListener(new ActionListener(){
    			// Apply the list of powered building to generator
    			public void actionPerformed(ActionEvent e){
    		        List<Object[]> buildingsPoweredList = new ArrayList<Object[]>();
    				for (Object [] buildingLine : buildingsToPower) {
    					Boolean isPoweredHere = ((AbstractButton) buildingLine[3]).isSelected();
    					if (isPoweredHere) {
    						buildingLine[3] = isPoweredHere;
    						buildingsPoweredList.add(buildingLine);
    						
    		        		Main.settings.AddDebugLog("Powering this building" + buildingLine[0]);
    					}
    				}
    				// Set buildingsPowered from constructed list
    				Object [][] buildingsPoweredArray =  new Object[buildingsPoweredList.size()][4];
    				buildingsPoweredList.toArray(buildingsPoweredArray);
		        	tuileCase.buildingsPowered = buildingsPoweredArray;
		            generatorDialog.setVisible(false);
    			}
    		});
        }

        generatorDialog.setLayout(new GridLayout(buildingNumber + 1, 2));

        generatorDialog.setVisible(true);
    }
}
