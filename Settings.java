package net.codejava;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.*;

public class Settings {
    // Param carte
	int carteNumberOfLigns = 12;
    int carteNumberOfColumns = 15;
    
    //Param players
    int numberOfPlayers = 3;
    public Player[] players = new Player[numberOfPlayers];
    Color[] playersColors = {Color.blue, Color.red, Color.green};
	
    //DATAS
    String [] possibleFilters = {"Biome", "Player", "Fibre", "Spore", "Suc", "Phosphorite", "MushCoins"};
    String[] directions = {"NO", "N", "NE", "O", "C", "E", "SO", "S", "SE"};
    
    //biomes datas
	String[] possibleBiomes = {"Brume", "Plaine", "Hauteurs", "Foret", "Désert", "Marais"};
	HashMap<String, String[]> fibreProdRules = new HashMap<String, String[]>();
	HashMap<String, String[]> sporeProdRules = new HashMap<String, String[]>();
	HashMap<String, String[]> sucProdRules = new HashMap<String, String[]>();
	HashMap<String, String[]> phosphoProdRules = new HashMap<String, String[]>();
	Integer rulesLength = 4;
	
	// building datas
	HashMap<String, String[]> buildingRules = new HashMap<String, String[]>();
	String[] farmRules = {"10", ""};
	String[] portRules = {"20", ""};
	String[] generatorRules = {"0", ""};
    
    //params selecteurs
    Sliders[] BiomeSliders;
    Sliders[] TempSliders;
    JFrame biomeFrame = new JFrame("Biomes settings");
    JFrame carteFrame = new JFrame("Carte settings");
    JFrame prodFrame = new JFrame("Production settings");
    JDialog biomeDialog = new JDialog(biomeFrame);
    JDialog carteDialog = new JDialog(carteFrame);
    
    // Prod rules dialogs
    JDialog prodDialog = new JDialog(prodFrame);
    JPanel fibreProdPanel = new JPanel();
	HashMap<String, JTextArea[]> fibreTextAreaFields = new HashMap<String, JTextArea[]>();
	JPanel sporeProdPanel = new JPanel();
	HashMap<String, JTextArea[]> sporeTextAreaFields = new HashMap<String, JTextArea[]>();
	JPanel sucProdPanel = new JPanel();
	HashMap<String, JTextArea[]> sucTextAreaFields = new HashMap<String, JTextArea[]>();
	JPanel phosphoProdPanel = new JPanel();
	HashMap<String, JTextArea[]> phosphoTextAreaFields = new HashMap<String, JTextArea[]>();
	// Tableau des infos de prod
	Object[][] prodDatas = {
			{"Production des fibres", fibreProdRules, fibreProdPanel, fibreTextAreaFields},
			{"Production des spores", sporeProdRules, sporeProdPanel, sporeTextAreaFields},
			{"Production du suc", sucProdRules, sucProdPanel, sucTextAreaFields},
			{"Production de phosphorite", phosphoProdRules, phosphoProdPanel, phosphoTextAreaFields}
	};

    
    JTextArea debugText = new JTextArea();
    JTextArea dataText = new JTextArea();

    
    //CONSTRUCTEUR
    public Settings() {
    	buildingRules.put("Ferme", farmRules);
    	buildingRules.put("Port", portRules);
    	buildingRules.put("Generator", generatorRules);
    	
    	initProdSettings();
    }
    
    // Setting initialisation
    void initProdSettings() {
    	// Fibre
		String[] FForet0 = {"Foret", "0", "2"};
		String[] FForet1 = {"Foret", "1", "1", "Brume"};
		fibreProdRules.put("Foret0", FForet0);
		fibreProdRules.put("Foret1", FForet1);
		
		// Spore
		String[] Foret0 = {"Foret", "0", "1"};
		String[] Marais0 = {"Marais", "0", "2"};
		String[] Plaine0 = {"Plaine", "0", "1"};
		String[] Port1 = {"Port", "1", "1"};
		sporeProdRules.put("Foret0", Foret0);
		sporeProdRules.put("Marais0", Marais0);
		sporeProdRules.put("Plaine0", Plaine0);
		sporeProdRules.put("Port1", Port1);
		
		//Suc
		String[] SDesert0 = {"Désert", "0", "1"};
		String[] SHauteurs0 = {"Hauteurs", "0", "1"};
		String[] SHauteurs1 = {"Hauteurs", "1", "1", "Brume"};
		sucProdRules.put("SDesert0", SDesert0);
		sucProdRules.put("SHauteurs0", SHauteurs0);
		sucProdRules.put("SHauteurs1", SHauteurs1);
		
		//Phosphorite
		String[] PHauteurs1 = {"Hauteurs", "0", "1"};
		phosphoProdRules.put("PHauteurs1", PHauteurs1);
    }

    //DEBUG
    void AddDebugLog(String message) {
    	debugText.setText(debugText.getText() + message + "\r\n");
    }
    
    void ClearDebugLog() {
    	debugText.setText("");
    }
     
    //PLAYERS
    public void createPlayersMenus() {
        for (var index = 0; index < numberOfPlayers; index++) {
    	//for (Player player : players) {
        	String playerName = "Joueur " + (index + 1);
        	if (index == playersColors.length) {
        		AddDebugLog("No more colors defined for players!");
        	}
        	Player player = new Player(playerName, playersColors[index]);
        	players[index] = player;
        	player.createMenu();
//        	AddDebugLog("Player " + index + " created");
        }
    }
    
    public void addPlayersMenus(JPanel panel) {
		// Ajouter les boutons radio au groupe
		ButtonGroup bg = new ButtonGroup();    
		for (var index = 0; index < numberOfPlayers; index++) {
        	Player player = players[index];
        	player.addMenu(panel);
        	bg.add(player.rBtn);
//        	AddDebugLog("Player added" + index);
        }
		players[0].rBtn.setSelected(true);
    }
    
    public Player returnActivePlayer() {
    	for (Player player : players) {
    		if(player.rBtn.isSelected()) {
    			return player;    			
    		}
    	}
    	return null;
    }
    
    public Player getRandomPlayer() {
		int Min = 0;
		int Max = numberOfPlayers - 1;
		int playerIndex = Min + (int)(Math.random() * (Max - Min + 1));
//		Main.temperatureLabel.setText("Tuile temp is " + temperature + "°C");
		return players[playerIndex];
    }
    
    public void updatePlayersProd() {
    	for(Player player : players) {
    		player.updateRessourceInfos();
    	}
    }
    
    //BIOME SETTINGS
    public void createBiomePopup() {
    	biomeDialog.setLayout(new FlowLayout());
    	
    	biomeDialog.setBounds(150, 200, 1300, 200);
        
        JPanel biomePanel = new JPanel();
        createBiomeSliders(biomePanel);
//    	biomeDialog.setLayout(new GridLayout(5,3));
        biomeDialog.add(biomePanel);
    }
    
    public void showBiomePopup() {
    	biomeDialog.setVisible(true);
    }
    
    void createBiomeSliders(JPanel sliderPanel) {
        //Sliders
    	BiomeSliders = new Sliders[possibleBiomes.length];
        int i = 0;
        for (String biome : possibleBiomes) {
        	Sliders slider = new Sliders(biome, 0, 100, 25);
        	sliderPanel.add(slider);
        	BiomeSliders[i] = slider;
        	i++;
        }
    }
    
    String getRandomTerrainValue() {
    	int[] terrainValues = getSlidersValue(BiomeSliders);
    	int sum = Arrays.stream(terrainValues).sum();
    	
    	int Min = 0;
		int Max = sum - 1;
    	int randomValue = Min + (int)(Math.random() * (Max - Min + 1));
    	
    	int biomeIndex = 0;
    	int sliderIndex = terrainValues[0];
    	
    	while (randomValue > sliderIndex -1) {
    		biomeIndex++;
    		sliderIndex += terrainValues[biomeIndex];
    	}
    	    	
    	if ((biomeIndex > possibleBiomes.length - 1) || biomeIndex < 0) {
    		System.out.println(biomeIndex);    		
    		Main.settings.AddDebugLog("[Settings.getRandomTerrainValue] value is out of bound: " + Integer.toString(biomeIndex));
    		return "Brume";
    	} else {
    		return(possibleBiomes[biomeIndex]);    	    		
    	}
    }
    
    //CARTE SETTINGS
    public void createCartePopup() {
    	carteDialog.setBounds(200, 200, 800, 200);
        
        JLabel lignLabel = new JLabel("Enter the number of ligns for the map");
        JTextArea lignText = new JTextArea("" + carteNumberOfLigns);
        
        JLabel columnLabel = new JLabel("Enter the number of columns for the map");
        JTextArea columnText = new JTextArea("" + carteNumberOfColumns);
        
        JLabel buttonLabel = new JLabel("Press apply button to apply settings and create a new map");
        JButton jButton = new JButton("Apply");
        jButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	carteNumberOfLigns = Integer.parseInt(lignText.getText());
            	carteNumberOfColumns = Integer.parseInt(columnText.getText());
            	Main.cartePanel.resetSize(carteNumberOfLigns, carteNumberOfColumns);
            	Main.cartePanel.updateFilterView();
            }
        });

        carteDialog.add(lignLabel);
        carteDialog.add(lignText);
        carteDialog.add(columnLabel);
        carteDialog.add(columnText);
        carteDialog.add(buttonLabel);
        carteDialog.add(jButton);
        
        carteDialog.setLayout(new GridLayout(3,2));
    }
    
    public void showCartePopup() {
    	carteDialog.setVisible(true);
    }

    
    // PRODUCTION
    public void createProdPopup() {
    	prodDialog.setBounds(200, 50, 1000, 700);
    	// Sub-panel Name
    	JLabel fibreProductionLabel = new JLabel("Production des fibres");
    	prodDialog.add(fibreProductionLabel);
//    	// Get sub-panel content
    	fibreTextAreaFields = setSubProdPanel(fibreProdPanel, fibreProdRules);
    	prodDialog.add(fibreProdPanel);
//    	// Sub-panel Name
//    	JLabel sporeProductionLabel = new JLabel("Production des spores");
//    	prodDialog.add(sporeProductionLabel);
//    	// Get sub-panel content
//    	sporeTextAreaFields = setSubProdPanel(sporeProdPanel, sporeProdRules);
//    	prodDialog.add(sporeProdPanel);
    	
//    	for (Object [] prodData: prodDatas) {
//    		// Sub-panel Name
//    		JLabel subProductionLabel = new JLabel((String) prodData[0]);
//    		prodDialog.add(subProductionLabel);
//    		HashMap<String, String[]> subProdRules = (HashMap<String, String[]>) prodData[1];
//    		JPanel subProdPanel = (JPanel) prodData[2];
//    		HashMap<String, JTextArea[]> subTextAreaFields = (HashMap<String, JTextArea[]>) prodData[3];
//    		
//    		// Get sub-panel content
//    		subTextAreaFields = setSubProdPanel(subProdPanel, subProdRules);
//    		prodDialog.add(subProdPanel);
//    	}
    	
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	fibreProdRules = getProdRulesFromTextAreas(fibreTextAreaFields);
//            	for (Object [] prodData: prodDatas) {
//            		HashMap<String, String[]> subProdRules = (HashMap<String, String[]>) prodData[1];
//            		HashMap<String, JTextArea[]> subTextAreaFields = (HashMap<String, JTextArea[]>) prodData[3];
//            		subProdRules = getProdRulesFromTextAreas(subTextAreaFields);
//            	}

            	Main.cartePanel.updateWorldView();
            	prodDialog.setVisible(false);
            }
        });
        prodDialog.add(applyButton);    	
//        carteDialog.add(applyAndExitButton);    	
    	prodDialog.setLayout(new GridLayout(5, 1));
    }
    
    HashMap<String, JTextArea[]> setSubProdPanel(JPanel subProdPanel, HashMap<String, String[]> prodRules) {
    	HashMap<String, JTextArea[]> TextAreaFields = new HashMap<String, JTextArea[]>();
    	subProdPanel.removeAll();
    	
    	// TODO: remove dummy
		JTextArea dummyTextField = new JTextArea("test");
		Font boldFont= new Font(dummyTextField.getFont().getName(), Font.BOLD, dummyTextField.getFont().getSize());
    	
    	for (String ruleName : prodRules.keySet()) {
			// Get present rules
    		String[] ruleValues = prodRules.get(ruleName);
    		// Fill rule name
			JTextArea ruleNameTextField = new JTextArea(ruleName);
			ruleNameTextField.setFont(boldFont);
			subProdPanel.add(ruleNameTextField);
			// Create array with all text area fields
			JTextArea[] textAreaArray = new JTextArea[rulesLength + 1];
			textAreaArray[0] = ruleNameTextField;
			for (Integer i = 0; i < rulesLength; i ++) {
				// Get rules data to fill text area fields
				String ruleValue = i < ruleValues.length ? ruleValues[i] : "";
				JTextArea ruleTextField = new JTextArea(ruleValue);
				subProdPanel.add(ruleTextField);
				textAreaArray[i + 1] = ruleTextField;
			}
			TextAreaFields.put(ruleNameTextField.getText(), textAreaArray);
    	}
    	// Adding an empty line for a new rule
		JTextArea[] emptyTextAreaArray = new JTextArea[rulesLength + 1];
		JTextArea emptyTextAreaName = new JTextArea("New rule ?");
		emptyTextAreaArray[0] = emptyTextAreaName;
		emptyTextAreaName.setFont(boldFont);
		subProdPanel.add(emptyTextAreaName);
		for (Integer i = 0; i < rulesLength; i ++) {
			JTextArea ruleTextField = new JTextArea("");
			subProdPanel.add(ruleTextField);
			emptyTextAreaArray[i + 1] = ruleTextField;
		}
		TextAreaFields.put(emptyTextAreaName.getText(), emptyTextAreaArray);

    	subProdPanel.setLayout(new GridLayout(prodRules.size() + 1, rulesLength + 1));
    	
    	return TextAreaFields;
    }
    
    HashMap<String, String[]> getProdRulesFromTextAreas(HashMap<String, JTextArea[]> TextAreaFields) {
		HashMap<String, String[]> newProdRules = new HashMap<String, String[]>();
		
    	for (String ruleName : TextAreaFields.keySet()) {
    		JTextArea[] ruleFromTextFields = TextAreaFields.get(ruleName);
    		Main.settings.AddDebugLog("rule name: " + ruleFromTextFields[0].getText());
    		Main.settings.AddDebugLog("field: " + ruleFromTextFields[1].getText() + ", length: " + ruleFromTextFields[1].getText().length());
    		if (ruleFromTextFields[1].getText().length() > 0 && ruleFromTextFields[2].getText().length() > 0 && ruleFromTextFields[3].getText().length() > 0) {
        		Main.settings.AddDebugLog("rule added");
    			String[] newRule = new String[rulesLength];
    			for (Integer i = 0; i < rulesLength; i ++) {
    				newRule[i] = ruleFromTextFields[i + 1].getText();
    	    		Main.settings.AddDebugLog("field: " + ruleFromTextFields[i + 1].getText() + ", length: " + ruleFromTextFields[i + 1].getText().length());
    			}
    			newProdRules.put(ruleFromTextFields[0].getText(), newRule);    			
    		}
    	}
    	return newProdRules;
    }
    
    public void showProdPopup() {
    	// TODO: use prodDatas tableau
    	fibreTextAreaFields = setSubProdPanel(fibreProdPanel, fibreProdRules);
    	sporeTextAreaFields = setSubProdPanel(sporeProdPanel, sporeProdRules);
    	sucTextAreaFields = setSubProdPanel(sucProdPanel, sucProdRules);
    	phosphoTextAreaFields = setSubProdPanel(phosphoProdPanel, phosphoProdRules);
    	prodDialog.setVisible(true);
    }

    // POWER
	public int getPowerCons(String buildingName) {
		if (buildingRules.containsKey(buildingName)) {
			return Integer.parseInt(buildingRules.get(buildingName)[0]);			
		} else {
			return 0;
		}
	}
	
	//MISC
	int[] getSlidersValue(Sliders[] sliders) {
		// TO DO: Use map function instead
		int[] slidersValues = new int[sliders.length];
		int i = 0;
		for (Sliders slider : sliders) {
			slidersValues[i] = slider.getValue();
			i ++;
		}
		return slidersValues;
	}
}
