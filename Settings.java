package net.codejava;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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
    
    //params selecteurs
    Sliders[] BiomeSliders;
    Sliders[] TempSliders;
    JFrame biomeFrame = new JFrame("Biomes settings");
    JFrame carteFrame = new JFrame("Carte settings");
    JDialog biomeDialog = new JDialog(biomeFrame);
    JDialog carteDialog = new JDialog(carteFrame);

    //biomes datas
	String[] possibleBiomes = {"Brume", "Plaine", "Hauteurs", "Foret", "Désert", "Marais"};
	HashMap<String, Integer[]>  biomeTypes = new HashMap<String, Integer[]>();
	Integer[] allBiomesType = {25, 25, 25, 25, 25, 25};
	HashMap<String, String[]> fibreProdRules = new HashMap<String, String[]>();
	HashMap<String, String[]> sporeProdRules = new HashMap<String, String[]>();
	HashMap<String, String[]> sucProdRules = new HashMap<String, String[]>();
	HashMap<String, String[]> phosphoProdRules = new HashMap<String, String[]>();
	HashMap<String, String[]> coinProdRules = new HashMap<String, String[]>();
	Integer prodRulesLength = 4;
    // Prod rules dialogs
    JFrame prodFrame = new JFrame("Production settings");
    JDialog prodDialog = new JDialog(prodFrame);
    JPanel fibreProdPanel = new JPanel();
	HashMap<String, JTextArea[]> fibreTextAreaFields = new HashMap<String, JTextArea[]>();
	JPanel sporeProdPanel = new JPanel();
	HashMap<String, JTextArea[]> sporeTextAreaFields = new HashMap<String, JTextArea[]>();
	JPanel sucProdPanel = new JPanel();
	HashMap<String, JTextArea[]> sucTextAreaFields = new HashMap<String, JTextArea[]>();
	JPanel phosphoProdPanel = new JPanel();
	HashMap<String, JTextArea[]> phosphoTextAreaFields = new HashMap<String, JTextArea[]>();
	JPanel coinProdPanel = new JPanel();
	HashMap<String, JTextArea[]> coinTextAreaFields = new HashMap<String, JTextArea[]>();
	// Tableau des infos de prod
	Object[][] prodDatas = {
			{"Production des fibres", fibreProdRules, fibreProdPanel, fibreTextAreaFields},
			{"Production des spores", sporeProdRules, sporeProdPanel, sporeTextAreaFields},
			{"Production du suc", sucProdRules, sucProdPanel, sucTextAreaFields},
			{"Production de phosphorite", phosphoProdRules, phosphoProdPanel, phosphoTextAreaFields},
			{"Production de Mushcoins", coinProdRules, coinProdPanel, coinTextAreaFields}
	};
	
	// building datas
	// Rules
	HashMap<String, String[]> buildingRules = new HashMap<String, String[]>();
	String[] dummyRule = {"En.requise", "En.produite", "distance"};
	String[] farmRule = {"10", "", ""};
	String[] portRule = {"20", "", ""};
	String[] generatorRule = {"0", "50", "3"};
	String[] pierRule = {"10", "", ""};
	Integer buildRulesLength = 3;
	Integer maxGeneratorDistance = 0;
	// buildings dialog
    JFrame buildRulesFrame = new JFrame("Buildings position settings");
    JDialog buildRulesDialog = new JDialog(buildRulesFrame);
    JPanel buildRulesPanel = new JPanel();
	HashMap<String, JTextArea[]> buildRulesTextAreaFields = new HashMap<String, JTextArea[]>();
	// Conditions
	HashMap<String, String[]> buildingConditions = new HashMap<String, String[]>();
	String[] dummyCondition = {"biomes/buildings,were,to,place", "nearby,biomes,or,buildings", "price in ressources (F/Sp/Suc/Phos)"};
	String[] farmCondition = {"Plaine,Marais", "", "100,200,200,100"};
	String[] portCondition = {"Brume", "Brume", "150,200,10,200"};
	String[] pierCondition = {"Port", "", ""};
	String[] generatorCondition = {"Désert,Plaine", "", ""};
	Integer buildConditionsLength = 3;
	public int constructRange = 3;
	// buildings dialog
    JFrame buildCondFrame = new JFrame("Buildings position settings");
    JDialog buildCondDialog = new JDialog(buildCondFrame);
    JPanel buildCondPanel = new JPanel();
	HashMap<String, JTextArea[]> buildCondTextAreaFields = new HashMap<String, JTextArea[]>();

    // Debugger
    JTextArea debugText = new JTextArea();
    
    //CONSTRUCTEUR
    public Settings() {
    	biomeTypes.put("Tout biomes", allBiomesType);
    	
    	initBuildingsSettings();
    	
    	initProdSettings();
    	
    	computeMaxGeneratorDistance();
    }
    
    // Setting initialisation
    void initProdSettings() {
    	// Fibre
		String[] FForet0 = {"Foret", "0", "5"};
		String[] MaraisF = {"Marais", "0", "1"};
		String[] FForet1 = {"Foret", "1", "1", "Brume,Plaine,Haiteurs,Désert,Marais"};
		String[] DésertF = {"Désert", "0", "1"};
		String[] PlaineF = {"Plaine", "0", "1"};
		fibreProdRules.put("Foret0", FForet0);
		fibreProdRules.put("MaraisF", MaraisF);
		fibreProdRules.put("Foret1", FForet1);
		fibreProdRules.put("DésertF", DésertF);
		fibreProdRules.put("PlaineF", PlaineF);
		
		// Spore
		String[] Plaine0 = {"Plaine", "0", "4"};
		String[] SPPier0 = {"Jetée", "1", "1"};
		String[] SPHauteurs0 = {"Hauteurs", "0", "1"};
		String[] SPBrume1 = {"Brume", "1", "1"};
		sporeProdRules.put("Plaine0", Plaine0);
		sporeProdRules.put("SPHauteurs0", SPHauteurs0);
		sporeProdRules.put("SPBrume1", SPBrume1);
		sporeProdRules.put("SPPier0", SPPier0);
		
		//Suc
		String[] SMarais1 = {"Marais", "1", "1"};
		String[] SMarais0 = {"Marais", "0", "5"};
		sucProdRules.put("SMarais1", SMarais1);
		sucProdRules.put("SMarais0", SMarais0);
		
		//Phosphorite
		String[] PHauteurs0 = {"Hauteurs", "0", "2"};
		String[] BrumeP0 = {"Brume", "1", "1"};
		String[] MaraisP0 = {"Marais", "0", "1"};
		phosphoProdRules.put("PHauteurs0", PHauteurs0);
		phosphoProdRules.put("BrumeP0", BrumeP0);
		phosphoProdRules.put("MaraisP0", MaraisP0);
		
		//MushCoins
		String[] CPort = {"Port+", "0", "2"};
		String[] CFarm = {"Ferme", "0", "1"};
		coinProdRules.put("CPort", CPort);
		coinProdRules.put("CFarm", CFarm);
    }
    
    void initBuildingsSettings() {
    	buildingRules.put("Ferme", farmRule);
    	buildingRules.put("Port", portRule);
    	buildingRules.put("Generateur", generatorRule);
    	buildingRules.put("Jetée", pierRule);
    	
    	buildingConditions.put("Ferme", farmCondition);
    	buildingConditions.put("Port", portCondition);
    	buildingConditions.put("Generateur", generatorCondition);
    	buildingConditions.put("Jetée", pierCondition);
    }
    
    void computeMaxGeneratorDistance() {
    	for (String ruleName : buildingRules.keySet()) {
    		String[] rule = buildingRules.get(ruleName);
    		if (rule[2] != "") {
        		Integer generatorDistance = Integer.parseInt(rule[2]);
        		if (generatorDistance > maxGeneratorDistance) {
        			maxGeneratorDistance = generatorDistance;
        		}
    		}
    	}
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
		return players[playerIndex];
    }
    
    public void updatePlayersProd() {
    	for(Player player : players) {
    		player.updateRessourceInfos();
    	}
    }
    
    //BIOME SETTINGS
    public void createBiomePopup() {
    	biomeDialog.setBounds(150, 200, 1300, 600);
        
        JPanel biomePanel = new JPanel();
        createBiomeSliders(biomePanel);
        
        JPanel biomeTypeCrea = new JPanel();
        JTextArea biomeTypeTextArea = new JTextArea("Nouveau type de biome");
        JButton biomeTypeSave = new JButton("Créer nouveau type de biome");

        // Create the table model
        DefaultTableModel model = new DefaultTableModel(possibleBiomes, 0);
        JTable biomeTable = new JTable(model);
		// Fill table
        for (String biomeTypeName : biomeTypes.keySet()) {
    		Integer[] biomeValues = biomeTypes.get(biomeTypeName);
        	fillTableWithBiomeDatas(biomeTypeName, biomeValues, model);
        }
        
        biomeTypeSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	biomeTypeCreation(biomeTypeTextArea, model);
            }
		});
        biomeTypeCrea.add(biomeTypeTextArea);
        biomeTypeCrea.add(biomeTypeSave);
        biomeTypeCrea.setLayout(new GridLayout(1, 2));
        
        biomeDialog.add(biomePanel);
        biomeDialog.add(biomeTypeCrea);
        biomeDialog.add(biomeTable);
        biomeDialog.setLayout(new GridLayout(3, 1));
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
    
    void biomeTypeCreation(JTextArea biomeTypeTextArea, DefaultTableModel model) {
    	String biomeTypeName = biomeTypeTextArea.getText();
    	if (biomeTypeName.length() == 0) {
    		JOptionPane.showMessageDialog(Main.frame,
    			    "Biome type name must not be empty",
    			    "Inane warning",
    			    JOptionPane.WARNING_MESSAGE);
    	} else if(biomeTypes.containsKey(biomeTypeName)) {
    		JOptionPane.showMessageDialog(Main.frame,
    			    "Biome type name is already used",
    			    "Inane warning",
    			    JOptionPane.WARNING_MESSAGE);
    	} else {
    		Integer[] biomeValues = getSlidersValue(BiomeSliders);
    		biomeTypes.put(biomeTypeName, biomeValues);
    		fillTableWithBiomeDatas(biomeTypeName, biomeValues, model);
    		Main.gameController.biomeSelector.addItem(biomeTypeName);
    	}
    }
    
    void fillTableWithBiomeDatas (String biomeTypeName, Integer[] biomeValues, DefaultTableModel model) {
		Object[] biomeDatas = new Object[biomeValues.length + 1];
		biomeDatas[0] = biomeTypeName;
		int index = 1;
		for (Integer biomeValue : biomeValues) {
			biomeDatas[index] = biomeValue;
			index++;
		}
        model.addRow(biomeDatas);
    }
    
    String getRandomTerrainValue() {
    	Integer[] terrainValues;
    	if (Main.gameController != null) {
    		String biomeSelectorValue = Main.gameController.biomeSelector.getItemAt(Main.gameController.biomeSelector.getSelectedIndex());
    		terrainValues = biomeTypes.get(biomeSelectorValue);
    	} else {
    		terrainValues = allBiomesType;
    	}
    	Integer sum = Arrays.stream(terrainValues).mapToInt(Integer::intValue).sum();
    	
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
    		AddDebugLog("[Settings.getRandomTerrainValue] value is out of bound: " + Integer.toString(biomeIndex));
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
    @SuppressWarnings("unchecked")
	public void createProdPopup() {
    	prodDialog.setBounds(50, 50, 1400, 700);
    	
    	JPanel leftPanel = new JPanel();
    	JPanel rightPanel = new JPanel();
    	
    	JLabel titleLabel = new JLabel("Production rules");
    	JLabel rulesTemplateLabel = new JLabel("Nom de la règle | Biome/Batiment d'où la règle s'applique (\"+\" si alimentation requise)| Distance à laquelle la règle s'applique | Production ajoutée aux cases | Biomes où la production ne s'ajoute pas");
    	leftPanel.add(titleLabel);
		rightPanel.add(rulesTemplateLabel);

    	for (Object [] prodData: prodDatas) {
    		// Sub-panel Name
    		JLabel subProductionLabel = new JLabel((String) prodData[0]);
    		leftPanel.add(subProductionLabel);
    		HashMap<String, String[]> subProdRules = (HashMap<String, String[]>) prodData[1];
    		JPanel subProdPanel = (JPanel) prodData[2];
//    		HashMap<String, JTextArea[]> subTextAreaFields = (HashMap<String, JTextArea[]>) prodData[3];
    		
    		// Get sub-panel content
//    		subTextAreaFields = 
    		setInputPanel(subProdPanel, subProdRules, prodRulesLength);
    		rightPanel.add(subProdPanel);
    	}
    	
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	applyRulesAndUpdateProds();
            	// TODO: adding new rule field when new one is entered
            	fibreTextAreaFields = setInputPanel(fibreProdPanel, fibreProdRules, prodRulesLength);
            	sporeTextAreaFields = setInputPanel(sporeProdPanel, sporeProdRules, prodRulesLength);
            	sucTextAreaFields = setInputPanel(sucProdPanel, sucProdRules, prodRulesLength);
            	phosphoTextAreaFields = setInputPanel(phosphoProdPanel, phosphoProdRules, prodRulesLength);
            	coinTextAreaFields = setInputPanel(coinProdPanel, coinProdRules, prodRulesLength);
            }
        });

        JButton applyAndExitButton = new JButton("Apply and Exit");
        applyAndExitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	applyRulesAndUpdateProds();
            	prodDialog.setVisible(false);
            }
        });
        
        leftPanel.setLayout(new GridLayout(prodDatas.length + 1, 1));
    	rightPanel.setLayout(new GridLayout(prodDatas.length + 1, 1));

        // créer un séparateur de panneau
        JSplitPane sep = new JSplitPane(SwingConstants.VERTICAL, leftPanel, rightPanel); 
        // définir l'orientation du séparateur
        sep.setOrientation(SwingConstants.VERTICAL);
        // Ajouter le séparateur
        prodDialog.add(sep);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(applyButton);    	
        buttonPanel.add(applyAndExitButton);    	
        buttonPanel.setLayout(new GridLayout(1, 2));
        
        prodDialog.add(buttonPanel, BorderLayout.SOUTH);    	
    }
    
    HashMap<String, String[]> getProdRulesFromTextAreas(HashMap<String, JTextArea[]> TextAreaFields) {
		HashMap<String, String[]> newProdRules = new HashMap<String, String[]>();
		
    	for (String ruleName : TextAreaFields.keySet()) {
    		JTextArea[] ruleFromTextFields = TextAreaFields.get(ruleName);
    		if (ruleFromTextFields[1].getText().length() > 0 && ruleFromTextFields[2].getText().length() > 0 && ruleFromTextFields[3].getText().length() > 0) {
    			String[] newRule = new String[prodRulesLength];
    			for (Integer i = 0; i < prodRulesLength; i ++) {
    				newRule[i] = ruleFromTextFields[i + 1].getText();
    			}
    			newProdRules.put(ruleFromTextFields[0].getText(), newRule);
    		}
    	}
    	return newProdRules;
    }
    
    void applyRulesAndUpdateProds() {
    	fibreProdRules = getProdRulesFromTextAreas(fibreTextAreaFields);
    	sporeProdRules = getProdRulesFromTextAreas(sporeTextAreaFields);
    	sucProdRules = getProdRulesFromTextAreas(sucTextAreaFields);
    	phosphoProdRules = getProdRulesFromTextAreas(phosphoTextAreaFields);
    	coinProdRules = getProdRulesFromTextAreas(coinTextAreaFields);

    	for (Player player : players) {
    		player.tuileViewer.updateTuileProductions();
    		player.tuileViewer.updateFilterView();
    	}
    	Main.cartePanel.updateWorldView();
    }
    
    public void showProdPopup() {
    	fibreTextAreaFields = setInputPanel(fibreProdPanel, fibreProdRules, prodRulesLength);
    	sporeTextAreaFields = setInputPanel(sporeProdPanel, sporeProdRules, prodRulesLength);
    	sucTextAreaFields = setInputPanel(sucProdPanel, sucProdRules, prodRulesLength);
    	phosphoTextAreaFields = setInputPanel(phosphoProdPanel, phosphoProdRules, prodRulesLength);
    	coinTextAreaFields = setInputPanel(coinProdPanel, coinProdRules, prodRulesLength);
    	prodDialog.setVisible(true);
    }
    
    // BUILDINGS-Condition
    public void createBuildCondPopup() {
    	buildCondDialog.setBounds(50, 50, 1400, 700);
    	
    	JPanel leftPanel = new JPanel();
    	JPanel rightPanel = new JPanel();
    	
    	JLabel titleLabel = new JLabel("Building conditions");
    	JLabel rulesTemplateLabel = new JLabel("Nom du batiment | Biome/batiment où l'on peut poser le batiment | Biome ou batiment devant se situer à côté | Coût du batiment (F,Sp,Suc,Phos)");
    	leftPanel.add(titleLabel);
		rightPanel.add(rulesTemplateLabel);

		JLabel buildingRulesLabel = new JLabel("Conditions de placement des buildings");
		leftPanel.add(buildingRulesLabel);
		
		// Get sub-panel content
		buildCondTextAreaFields = setInputPanel(buildCondPanel, buildingConditions, buildConditionsLength);
		rightPanel.add(buildCondPanel);
    	
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	updateBuildingConditions();
            	// TODO: adding new rule field when new one is entered
        		buildCondTextAreaFields = setInputPanel(buildCondPanel, buildingConditions, buildConditionsLength);
            }
        });

        JButton applyAndExitButton = new JButton("Apply and Exit");
        applyAndExitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	updateBuildingConditions();
            	buildCondDialog.setVisible(false);
            }
        });
        
        leftPanel.setLayout(new GridLayout(2, 1));
    	rightPanel.setLayout(new GridLayout(2, 1));

        // créer un séparateur de panneau
        JSplitPane sep = new JSplitPane(SwingConstants.VERTICAL, leftPanel, rightPanel); 
        // définir l'orientation du séparateur
        sep.setOrientation(SwingConstants.VERTICAL);
        // Ajouter le séparateur
        buildCondDialog.add(sep);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(applyButton);    	
        buttonPanel.add(applyAndExitButton);    	
        buttonPanel.setLayout(new GridLayout(1, 2));
        
        buildCondDialog.add(buttonPanel, BorderLayout.SOUTH);    	
    }

    HashMap<String, String[]> getRulesFromTextAreas(HashMap<String, JTextArea[]> TextAreaFields, Integer rulesLength) {
		HashMap<String, String[]> newProdRules = new HashMap<String, String[]>();
		
    	for (String ruleName : TextAreaFields.keySet()) {
    		JTextArea[] ruleFromTextFields = TextAreaFields.get(ruleName);
    		// TODO: Check if ressource price is okay (3e field)
    		if (ruleFromTextFields[1].getText().length() > 0) { // && ruleFromTextFields[2].getText().length() > 0 && ruleFromTextFields[3].getText().length() > 0) {
    			String[] newRule = new String[rulesLength];
    			for (Integer i = 0; i < rulesLength; i ++) {
    				newRule[i] = ruleFromTextFields[i + 1].getText();
    			}
    			newProdRules.put(ruleFromTextFields[0].getText(), newRule);
    		}
    	}
    	return newProdRules;
    }
    
    void updateBuildingConditions() {
    	buildingConditions = getRulesFromTextAreas(buildCondTextAreaFields, buildConditionsLength);
    }
    
    public void showBuildConditionPopup() {
		buildCondTextAreaFields = setInputPanel(buildCondPanel, buildingConditions, buildConditionsLength);
    	buildCondDialog.setVisible(true);
    }
    
    String getRessourcePrice(String buildingName) {
    	if (buildingConditions.containsKey(buildingName)) {
    		String priceInRessource = buildingConditions.get(buildingName)[2];
    		if (priceInRessource != null && !priceInRessource.equals("")) {
    			return priceInRessource;
    		} else {
    			AddDebugLog("[getRessourcePrice] building price undefined for this building: " + buildingName);
    		}
    	} else {
    		AddDebugLog("[getRessourcePrice] this building does not exists: " + buildingName);
    	}
    	return null;
    }
    
    //BUILDINGS-Rules
    public void createBuildRulesPopup() {
    	buildRulesDialog.setBounds(50, 50, 1400, 700);
    	
    	JPanel leftPanel = new JPanel();
    	JPanel rightPanel = new JPanel();
    	
    	JLabel titleLabel = new JLabel("Building rules");
    	JLabel rulesTemplateLabel = new JLabel("Energie requise | Energie produite | Portée");
    	leftPanel.add(titleLabel);
		rightPanel.add(rulesTemplateLabel);

		JLabel buildingRulesLabel = new JLabel("Règles de gestion des buildings");
		leftPanel.add(buildingRulesLabel);
		
		// Get sub-panel content
		buildRulesTextAreaFields = setInputPanel(buildRulesPanel, buildingRules, buildRulesLength);
		rightPanel.add(buildRulesPanel);
    	
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	updateBuildingRules();
            	// TODO: adding new rule field when new one is entered
        		buildRulesTextAreaFields = setInputPanel(buildRulesPanel, buildingRules, buildRulesLength);
            }
        });

        JButton applyAndExitButton = new JButton("Apply and Exit");
        applyAndExitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	updateBuildingRules();
            	//TODO: Check that closing works
            	buildRulesDialog.setVisible(false);
            }
        });
        
        leftPanel.setLayout(new GridLayout(2, 1));
    	rightPanel.setLayout(new GridLayout(2, 1));

        // créer un séparateur de panneau
        JSplitPane sep = new JSplitPane(SwingConstants.VERTICAL, leftPanel, rightPanel); 
        // définir l'orientation du séparateur
        sep.setOrientation(SwingConstants.VERTICAL);
        // Ajouter le séparateur
        buildRulesDialog.add(sep);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(applyButton);    	
        buttonPanel.add(applyAndExitButton);    	
        buttonPanel.setLayout(new GridLayout(1, 2));
        
        buildRulesDialog.add(buttonPanel, BorderLayout.SOUTH);    	
    }
    
    void updateBuildingRules() {
    	buildingRules = getRulesFromTextAreas(buildRulesTextAreaFields, buildRulesLength);
    	computeMaxGeneratorDistance();
    }

    public void showBuildRulesPopup() {
    	buildRulesTextAreaFields = setInputPanel(buildRulesPanel, buildingRules, buildRulesLength);
    	buildRulesDialog.setVisible(true);
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
   HashMap<String, JTextArea[]> setInputPanel(JPanel subProdPanel, HashMap<String, String[]> prodRules, Integer rulesLength) {
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
	   
	Integer[] getSlidersValue(Sliders[] sliders) {
		// TO DO: Use map function instead
		Integer[] slidersValues = new Integer[sliders.length];
		int i = 0;
		for (Sliders slider : sliders) {
			slidersValues[i] = slider.getValue();
			i ++;
		}
		return slidersValues;
	}

	public void saveRules() {
	    String saveName = "UBprodSave";
	    String savePath = "C:\\UBsaves\\" + saveName + ".txt";
		try {
			File myObj = new File(savePath);
			if (myObj.createNewFile()) {
	        	System.out.println("File created: " + myObj.getName());
			} else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
		}
	    try {
	        FileWriter myWriter = new FileWriter(savePath);
	        String saveContent = createSaveFile();
	        myWriter.write(saveContent);
	        myWriter.close();
	        System.out.println("Successfully wrote to the file.");
	    } catch (IOException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	    }
	}

	private String createSaveFile() {
		String saveContent = "";
		saveContent += "fibreProdRulesç" + createSaveFilefromHashMap(fibreProdRules) + "\r\n";
		saveContent += "sporeProdRulesç" + createSaveFilefromHashMap(sporeProdRules) + "\r\n";
		saveContent += "sucProdRulesç" + createSaveFilefromHashMap(sucProdRules) + "\r\n";
		saveContent += "phosphoProdRulesç" + createSaveFilefromHashMap(phosphoProdRules) + "\r\n";
		saveContent += "coinProdRulesç" + createSaveFilefromHashMap(coinProdRules) + "\r\n";
		saveContent += "buildingRulesç" + createSaveFilefromHashMap(buildingRules) + "\r\n";
		saveContent += "buildingConditionsç" + createSaveFilefromHashMap(buildingConditions) + "\r\n";
		return saveContent;
	}

	private String createSaveFilefromHashMap(HashMap<String, String[]> hashMapRules) {
    	String saveSubContent = "";
		for (String ruleName : hashMapRules.keySet()) {
			saveSubContent += ruleName;
    		String[] ruleValues = hashMapRules.get(ruleName);
    		for (String savedValue : ruleValues) {
    			if (savedValue.equals("") || savedValue == null) {
    				savedValue = "@";
    			}
    			saveSubContent += "!" + savedValue;
    		}
    		saveSubContent += "!!";
    	}
		return saveSubContent;
	}
	
	void readSaveFile(String saveName) {
		String savePath = "C:\\UBsaves\\" + saveName + ".txt";
	    try {
	        File myObj = new File(savePath);
	        Scanner myReader = new Scanner(myObj);
	        while (myReader.hasNextLine()) {
	          String data = myReader.nextLine();
	          System.out.println(data);
	          loadHashmapRulesFromString(data);
	        }
	        myReader.close();
	    } catch (FileNotFoundException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	    }
	}
	
	private void loadHashmapRulesFromString(String saveDatas) {
		AddDebugLog("saveDatas:" + saveDatas);
		String[] rulesDatas = saveDatas.split("ç");
		AddDebugLog("rule name:" + rulesDatas[0]);
		AddDebugLog("rulesDatas:" + rulesDatas[1]);
		switch (rulesDatas[0]) {
			case "fibreProdRules":
				fibreProdRules = getHashMapRuleFromString(rulesDatas[1]);
				break;
			case "sporeProdRules":
				sporeProdRules = getHashMapRuleFromString(rulesDatas[1]);
				break;
			case "sucProdRules":
				sucProdRules = getHashMapRuleFromString(rulesDatas[1]);
				break;
			case "phosphoProdRules":
				phosphoProdRules = getHashMapRuleFromString(rulesDatas[1]);
				break;
			case "coinProdRules":
				coinProdRules = getHashMapRuleFromString(rulesDatas[1]);
				break;
			case "buildingRules":
				buildingRules = getHashMapRuleFromString(rulesDatas[1]);
				break;
			case "buildingConditions":
				buildingConditions = getHashMapRuleFromString(rulesDatas[1]);
				break;
		}
	}

	private HashMap<String, String[]> getHashMapRuleFromString(String rulesDatas) {
		HashMap<String, String[]> rule = new HashMap<String, String[]>();
		String[] splitRulesDatas = rulesDatas.split("!!");
		for (String ruleDatas : splitRulesDatas) {
			AddDebugLog("" + ruleDatas);
			String[] splitRuleDatas = ruleDatas.split("!");
			String[] newRule = new String[splitRuleDatas.length - 1];
			for (int i = 0; i < splitRuleDatas.length - 1; i++) {
				if (splitRuleDatas[i + 1].equals("@")) {
					newRule[i] = "";
				} else {
					newRule[i] = splitRuleDatas[i + 1];					
				}
			}
			rule.put(splitRuleDatas[0], newRule);
		}
		return rule;
	}
}
