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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    String[] directions = {"NO", "N", "NE", "O", "C", "E", "SO", "S", "SE"};
    Integer inhabitantWorkforce = 50;
    
    //params selecteurs
    Sliders[] BiomeSliders;
    Sliders[] TempSliders;
    JFrame biomeFrame = new JFrame("Biomes settings");
    JFrame carteFrame = new JFrame("Carte settings");
    JDialog biomeDialog = new JDialog(biomeFrame);
    JDialog carteDialog = new JDialog(carteFrame);

    // Seasons
    String[] possibleSeasons = {"Jour", "Crépuscule", "Nuit", "Aube"};
    String currentSeason = "Jour";
    //biomes datas
	String[] possibleBiomes = {"Brume", "Plaine", "Hauteurs", "Foret", "Désert", "Marais"};
    DefaultTableModel model = new DefaultTableModel(possibleBiomes, 0);
	HashMap<String, Integer[]>  biomeTypes = new HashMap<String, Integer[]>();
	Integer[] allBiomesType = {25, 25, 25, 25, 25, 25};
	
	// PRODUCTIONS
    String[] prodRulesTableTitle = {"Nom de la règle", "Biome/Batiment d'où la règle s'applique (\"+\" si alimentation requise)", "Distance à laquelle la règle s'applique", "Production ajoutée aux cases", "Biomes où la production ne s'ajoute pas", "Saison(s) si spécifique", "Abondance"};
    TableCustomModel prodRulesTableModel;
	HashMap<String, String[]> fibreProdRules = new HashMap<String, String[]>();
	HashMap<String, String[]> sporeProdRules = new HashMap<String, String[]>();
	HashMap<String, String[]> sucProdRules = new HashMap<String, String[]>();
	HashMap<String, String[]> phosphoProdRules = new HashMap<String, String[]>();
	HashMap<String, String[]> coinProdRules = new HashMap<String, String[]>();
	Integer prodRulesLength = 6;
    // Prod rules dialogs
    JFrame prodFrame = new JFrame("Production settings");
    JDialog prodDialog = new JDialog(prodFrame);
    JPanel fibreProdPanel = new JPanel();
    TableCustomModel fibreTableModel;
	JPanel sporeProdPanel = new JPanel();
	TableCustomModel sporeTableModel;
	JPanel sucProdPanel = new JPanel();
	TableCustomModel sucTableModel;
	JPanel phosphoProdPanel = new JPanel();
	TableCustomModel phosphoTableModel;
	JPanel coinProdPanel = new JPanel();
	TableCustomModel coinTableModel;
	// Tableau des infos de prod
	Object[][] prodDatas = {
			{"Production des fibres", fibreProdRules, fibreProdPanel, fibreTableModel},
			{"Production des spores", sporeProdRules, sporeProdPanel, sporeTableModel},
			{"Production du suc", sucProdRules, sucProdPanel, sucTableModel},
			{"Production de phosphorite", phosphoProdRules, phosphoProdPanel, phosphoTableModel},
			{"Production de Mushcoins", coinProdRules, coinProdPanel, coinTableModel}
	};
	
	// building datas
    String [] emptyStringArray = {};
	// Rules
    String[] buildRulesTableTitle = {"Nom du batiment", "Nombres emplacements de modules (sol, mur, toit)","Energie requise", "Energie produite", "Portée", "Consommation de Suc", "Réserve de Suc"};
    TableCustomModel buildRulesTableModel;
	HashMap<String, String[]> buildingRules = new HashMap<String, String[]>();
	String[] dummyRule = {"Emp. modules (sol,mur,toit)", "En.requise", "En.produite", "distance", "Consommation de Suc", "Réserve de Suc"};
	String[] farmRule = {"3,1,0","10", "", "", "10", "100"};
	String[] portRule = {"3,2,1","20", "", "", "5", "200"};
	String[] generatorRule = {"2,2,2", "0", "50", "3", "20", "500"};
	String[] pierRule = {"-", "10", "", "", "5"};
	String[] roadRule = {"-", "0", "", "", "0"};
	Integer buildRulesLength = 6;
	Integer maxGeneratorDistance = 0;
	// buildings dialog
    JFrame buildRulesFrame = new JFrame("Buildings position settings");
    JDialog buildRulesDialog = new JDialog(buildRulesFrame);
    JPanel buildRulesPanel = new JPanel();

	// Conditions
	String[] buildCondTableTitle = {"Nom du batiment", "Type de batiment", "Biome/batiment où l'on peut poser le batiment", "Biome ou batiment devant se situer à côté", "Coût du batiment (F,Sp,Suc,Phos)"};
	TableCustomModel buildCondTableModel;
	HashMap<String, String[]> buildingConditions = new HashMap<String, String[]>();
//	String[] dummyCondition = {"biomes/buildings modules,were,to,place", "nearby,biomes,or,buildings", "price in ressources (F/Sp/Suc/Phos)"};
	String[] farmCondition = {"Core", "Plaine,Marais", "", "100,200,200,100"};
	String[] portCondition = {"Core", "Brume", "Brume", "150,200,10,200"};
	String[] pierCondition = {"Sol", "Port", "", "30,30,30,30"};
	String[] roadCondition = {"Autre", "Plaine,Désert,Foret,Hauteurs,Marais", "", "10,10,0,0"};
	String[] generatorCondition = {"Core", "Désert,Plaine", "", "400,0,100,100"};
	String[] lightHouseCondition = {"Toit", "Port", "Brume", "30,30,30,30"};
	Integer buildConditionsLength = 4;
	Integer constructRange = 5;
	// buildings dialog
    JFrame buildCondFrame = new JFrame("Buildings position settings");
    JDialog buildCondDialog = new JDialog(buildCondFrame);
    JPanel buildCondPanel = new JPanel();

    // Debugger
    JTextArea debugText = new JTextArea();
	Font boldFont = new Font("Dialog", Font.BOLD, 12);
    
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
		String[] FForet0 = {"Foret", "0", "5", "", "Jour"};
		String[] MaraisF = {"Marais", "0", "1"};
		String[] FForet1 = {"Foret", "1", "1", "Brume,Plaine,Hauteurs,Désert,Marais"};
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
		String[] SForet0 = {"Foret", "0", "1"};
		sucProdRules.put("SMarais1", SMarais1);
		sucProdRules.put("SMarais0", SMarais0);
		sucProdRules.put("SForet0", SForet0);
		
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
    	buildingRules.put("Route", roadRule);
    	
    	buildingConditions.put("Ferme", farmCondition);
    	buildingConditions.put("Port", portCondition);
    	buildingConditions.put("Generateur", generatorCondition);
    	buildingConditions.put("Jetée", pierCondition);
    	buildingConditions.put("Route", roadCondition);
    	buildingConditions.put("Phare", lightHouseCondition);
    }
    
    void computeMaxGeneratorDistance() {
    	for (String ruleName : buildingRules.keySet()) {
    		String[] rule = buildingRules.get(ruleName);
    		// Check if energy is produced
    		if (!rule[3].equals("")) {
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
    	System.out.println(message);
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
    
    public Player getPlayerfromName(String playerName) {
    	for (Player player : players) {
    		if(player.playerName.equals(playerName)) {
    			return player;
    		}
    	}
		AddDebugLog("Player " + playerName + " does not exist");
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
        JTable biomeTable = new JTable(model);
        
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
    	model.setRowCount(0);
    	// Fill table
        for (String biomeTypeName : biomeTypes.keySet()) {
    		Integer[] biomeValues = biomeTypes.get(biomeTypeName);
        	fillTableWithBiomeDatas(biomeTypeName, biomeValues, model);
        }
    	
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
    	prodDialog.setBounds(10, 50, 1520, 700);
    	
    	JPanel leftPanel = new JPanel();
    	JPanel rightPanel = new JPanel();
    	
    	for (Object [] prodData: prodDatas) {
    		// Sub-panel Name
    		JLabel subProductionLabel = new JLabel((String) prodData[0]);
    		leftPanel.add(subProductionLabel);
    		HashMap<String, String[]> subProdRules = (HashMap<String, String[]>) prodData[1];
    		JPanel subProdPanel = (JPanel) prodData[2];
    		
    		// Get sub-panel content
    		setInputPanel(subProdPanel, prodRulesTableTitle, subProdRules, prodRulesLength);
    		rightPanel.add(subProdPanel);
    	}
    	
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	applyRulesAndUpdateProds();
            	// TODO: adding new rule field when new one is entered
            	fibreTableModel = setInputPanel(fibreProdPanel, prodRulesTableTitle, fibreProdRules, prodRulesLength);
            	sporeTableModel = setInputPanel(sporeProdPanel, prodRulesTableTitle, sporeProdRules, prodRulesLength, false);
            	sucTableModel = setInputPanel(sucProdPanel, prodRulesTableTitle, sucProdRules, prodRulesLength, false);
            	phosphoTableModel = setInputPanel(phosphoProdPanel, prodRulesTableTitle, phosphoProdRules, prodRulesLength, false);
            	coinTableModel = setInputPanel(coinProdPanel, prodRulesTableTitle, coinProdRules, prodRulesLength, false);
            }
        });

        JButton applyAndExitButton = new JButton("Apply and Exit");
        applyAndExitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	applyRulesAndUpdateProds();
            	prodDialog.setVisible(false);
            }
        });
        
        leftPanel.setLayout(new GridLayout(prodDatas.length, 1));
    	rightPanel.setLayout(new GridLayout(prodDatas.length, 1));

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
    
    HashMap<String, String[]> getProdRulesFromTextAreas(TableCustomModel tableModel) {
		HashMap<String, String[]> newProdRules = new HashMap<String, String[]>();
		
		for (int rowIndex = 0; rowIndex < tableModel.getRowCount(); rowIndex++) {
			if (!tableModel.getValueAt(rowIndex, 1).equals("") && !tableModel.getValueAt(rowIndex, 2).equals("") && !tableModel.getValueAt(rowIndex, 3).equals("")) {
				String[] newRule = new String[prodRulesLength];
				for (int columnIndex = 1; columnIndex < tableModel.getColumnCount(); columnIndex++) {
					newRule[columnIndex - 1] = tableModel.getValueAt(rowIndex, columnIndex);
				}
				newProdRules.put(tableModel.getValueAt(rowIndex, 0), newRule);				
			}
		}
    	return newProdRules;
    }
    
    void applyRulesAndUpdateProds() {
    	fibreProdRules = getProdRulesFromTextAreas(fibreTableModel);
    	sporeProdRules = getProdRulesFromTextAreas(sporeTableModel);
    	sucProdRules = getProdRulesFromTextAreas(sucTableModel);
    	phosphoProdRules = getProdRulesFromTextAreas(phosphoTableModel);
    	coinProdRules = getProdRulesFromTextAreas(coinTableModel);

    	for (Player player : players) {
    		player.tuileViewer.updateTuileProductions();
    		player.tuileViewer.updateFilterView();
    	}
    	Main.cartePanel.updateWorldView();
    }
    
    public void showProdPopup() {
    	fibreTableModel = setInputPanel(fibreProdPanel, prodRulesTableTitle, fibreProdRules, prodRulesLength);
    	sporeTableModel = setInputPanel(sporeProdPanel, prodRulesTableTitle, sporeProdRules, prodRulesLength, false);
    	sucTableModel = setInputPanel(sucProdPanel, prodRulesTableTitle, sucProdRules, prodRulesLength, false);
    	phosphoTableModel = setInputPanel(phosphoProdPanel, prodRulesTableTitle, phosphoProdRules, prodRulesLength, false);
    	coinTableModel = setInputPanel(coinProdPanel, prodRulesTableTitle, coinProdRules, prodRulesLength, false);
    	prodDialog.setVisible(true);
    }
    
    
    //BUILDINGS-Rules
    public void createBuildRulesPopup() {
    	buildRulesDialog.setBounds(50, 50, 1400, 700);
    	
		// Get sub-panel content
		buildRulesTableModel = setInputPanel(buildRulesPanel, buildRulesTableTitle, buildingRules, buildRulesLength);
    	
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	updateBuildingRules();
            	// TODO: adding new rule field when new one is entered
        		buildRulesTableModel = setInputPanel(buildRulesPanel, buildRulesTableTitle, buildingRules, buildRulesLength);
            }
        });

        JButton applyAndExitButton = new JButton("Apply and Exit");
        applyAndExitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	updateBuildingRules();
        		buildRulesTableModel = setInputPanel(buildRulesPanel, buildRulesTableTitle, buildingRules, buildRulesLength);
            	buildRulesDialog.setVisible(false);
            }
        });
        
        buildRulesDialog.add(buildRulesPanel);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(applyButton);    	
        buttonPanel.add(applyAndExitButton);    	
        buttonPanel.setLayout(new GridLayout(1, 2));
        
        buildRulesDialog.add(buttonPanel, BorderLayout.SOUTH);    	
    }
    
    void updateBuildingRules() {
    	buildingRules = getRulesFromTextAreas(buildRulesTableModel, buildRulesLength);
    	computeMaxGeneratorDistance();
    }

    public void showBuildRulesPopup() {
    	buildRulesTableModel = setInputPanel(buildRulesPanel, buildRulesTableTitle, buildingRules, buildRulesLength);
    	buildRulesDialog.setVisible(true);
    }
    
    // BUILDINGS-Condition
    public void createBuildCondPopup() {
    	buildCondDialog.setBounds(50, 50, 1400, 700);
    	
		// Get sub-panel content
		buildCondTableModel = setInputPanel(buildCondPanel, buildCondTableTitle, buildingConditions, buildConditionsLength);
    	
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	updateBuildingConditions();
            	// TODO: adding new rule field when new one is entered
        		buildCondTableModel = setInputPanel(buildCondPanel, buildCondTableTitle, buildingConditions, buildConditionsLength);
            }
        });

        JButton applyAndExitButton = new JButton("Apply and Exit");
        applyAndExitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	updateBuildingConditions();
        		buildCondTableModel = setInputPanel(buildCondPanel, buildCondTableTitle, buildingConditions, buildConditionsLength);
            	buildCondDialog.setVisible(false);
            }
        });
        
        buildCondDialog.add(buildCondPanel);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(applyButton);    	
        buttonPanel.add(applyAndExitButton);    	
        buttonPanel.setLayout(new GridLayout(1, 2));
        
        buildCondDialog.add(buttonPanel, BorderLayout.SOUTH);    	
    }

    HashMap<String, String[]> getRulesFromTextAreas(TableCustomModel tableModel, Integer rulesLength) {
		HashMap<String, String[]> newProdRules = new HashMap<String, String[]>();
		
		for (int rowIndex = 0; rowIndex < tableModel.getRowCount(); rowIndex++) {
			if (!tableModel.getValueAt(rowIndex, 1).equals("")) {
				String[] newRule = new String[rulesLength];
				for (int columnIndex = 1; columnIndex < tableModel.getColumnCount(); columnIndex++) {
					newRule[columnIndex - 1] = tableModel.getValueAt(rowIndex, columnIndex);
				}
				newProdRules.put(tableModel.getValueAt(rowIndex, 0), newRule);				
			}
		}
		
    	return newProdRules;
    }
    
    void updateBuildingConditions() {
    	buildingConditions = getRulesFromTextAreas(buildCondTableModel, buildConditionsLength);
    }
    
    public void showBuildConditionPopup() {
		buildCondTableModel = setInputPanel(buildCondPanel, buildCondTableTitle, buildingConditions, buildConditionsLength);
    	buildCondDialog.setVisible(true);
    }
    
    String getRessourcePrice(String buildingName) {
    	if (buildingConditions.containsKey(buildingName)) {
    		String priceInRessource = buildingConditions.get(buildingName)[3];
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
    
    public int getNbBuildPartPosition(String buildName, String partPosition) {
    	int nbPartsPostition = 0;
    	switch(partPosition) {
	    	case "sol":
	    		nbPartsPostition = Integer.valueOf(buildingRules.get(buildName)[0].split(",")[0]);
	    		break;
	    	case "mur":
	    		nbPartsPostition = Integer.valueOf(buildingRules.get(buildName)[0].split(",")[1]);
	    		break;
	    	case "toit":
	    		nbPartsPostition = Integer.valueOf(buildingRules.get(buildName)[0].split(",")[2]);
	    		break;
	    	default:
	    		AddDebugLog("ERROR: unknown building part position: " + partPosition);
    	}
    	return nbPartsPostition;
    }
    
    // POWER
	public int getPowerCons(String buildingName) {
		if (buildingRules.containsKey(buildingName)) {
			return Integer.parseInt(buildingRules.get(buildingName)[1]);			
		} else {
			return 0;
		}
	}
	
    // SUC
	public int getSucCons(String buildingName) {
		if (buildingRules.containsKey(buildingName)) {
			return Integer.parseInt(buildingRules.get(buildingName)[4]);			
		} else {
			return 0;
		}
	}
	
	public int getSucStock(String buildingName) {
		if (buildingRules.containsKey(buildingName)) {
			return Integer.parseInt(buildingRules.get(buildingName)[5]);			
		} else {
			return 0;
		}
	}
	
	//MISC
	TableCustomModel setInputPanel(JPanel subProdPanel, String[] tableTitle, HashMap<String, String[]> prodRules, Integer rulesLength) {
		return setInputPanel(subProdPanel, tableTitle, prodRules, rulesLength, true);
	}
	
	TableCustomModel setInputPanel(JPanel subProdPanel, String[] tableTitle, HashMap<String, String[]> prodRules, Integer rulesLength, Boolean displayHeader) {
		subProdPanel.removeAll();
    	
        List<String[]> tableDataList = new ArrayList<String[]>();
    	
    	for (String ruleName : prodRules.keySet()) {
			// Get present rules
    		String[] ruleValues = prodRules.get(ruleName);
			// Create array with all text area fields
			String[] textAreaArray = new String[rulesLength + 1];
			textAreaArray[0] = ruleName;
			for (Integer i = 0; i < rulesLength; i ++) {
				// Get rules data to fill text area fields  
				String ruleValue = i < ruleValues.length ? ruleValues[i] : "";
				textAreaArray[i + 1] = ruleValue;
			}
			tableDataList.add(textAreaArray);
    	}
    	// Adding an empty line for a new rule
		String[] emptyTextAreaArray = new String[rulesLength + 1];
		String emptyTextAreaName = new String("New rule ?");
		emptyTextAreaArray[0] = emptyTextAreaName;
		for (Integer i = 0; i < rulesLength; i ++) {
			String ruleTextField = new String("");
			emptyTextAreaArray[i + 1] = ruleTextField;
		}
		tableDataList.add(emptyTextAreaArray);

    	Object[][] tableData = new Object[tableDataList.size()][rulesLength + 1];
        tableDataList.toArray(tableData);

        TableCustomModel dataModel = new TableCustomModel(tableTitle, tableData);
        JTable table = new JTable(dataModel);

                
        subProdPanel.setLayout(new BorderLayout());
        if (displayHeader) {
        	subProdPanel.add(table.getTableHeader(), BorderLayout.PAGE_START);        	
        }
        subProdPanel.add(table, BorderLayout.CENTER);
        
        table.setFillsViewportHeight(true);
        
    	
    	return dataModel;
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
	
	public void saveMap() {
	    String saveName = "UBmapSave";
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
	        String saveContent = Main.cartePanel.createMapFile();
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
		saveContent += "biomeTypesç" + createSaveFilefromHashMapInteger(biomeTypes) + "\r\n";
		return saveContent;
	}

	private String createSaveFilefromHashMap(HashMap<String, String[]> hashMapRules) {
    	String saveSubContent = "";
		for (String ruleName : hashMapRules.keySet()) {
			saveSubContent += ruleName;
    		String[] ruleValues = hashMapRules.get(ruleName);
    		for (String savedValue : ruleValues) {
    			if (savedValue == null || savedValue.equals("")) {
    				savedValue = "@";
    			}
    			saveSubContent += "!" + savedValue;
    		}
    		saveSubContent += "!!";
    	}
		return saveSubContent;
	}
	
	private String createSaveFilefromHashMapInteger(HashMap<String, Integer[]> integerHashMap) {
		HashMap <String, String[]> stringHashMap = new HashMap<String, String[]>();
		for (String ruleName : integerHashMap.keySet()) {
			Integer[] integerValues = integerHashMap.get(ruleName);
			String[] stringValues = new String[integerValues.length];
			for (int i = 0; i < integerValues.length; i++) {
				stringValues[i] = integerValues[i].toString();
			}
			stringHashMap.put(ruleName, stringValues);
		}
		return createSaveFilefromHashMap(stringHashMap);
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
	          Main.gameController.resetBiomeSelectorValues();
	          Main.cartePanel.updateWorldView();
	        }
	        myReader.close();
	    } catch (FileNotFoundException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	    }
	}
	public void readMapFile(String saveName) {
		String savePath = "C:\\UBsaves\\" + saveName + ".txt";
		Boolean firstLine = true;
		Integer currentLign = 0;
		Integer currentcolumn = 0;
	    try {
	        File myObj = new File(savePath);
	        Scanner myReader = new Scanner(myObj);
	        while (myReader.hasNextLine()) {
	          String data = myReader.nextLine();
	          System.out.println(data);
	          if (firstLine) {
	        	  String mapSize = data.split("-")[0];
	        	  Main.gameController.timeIteration = Integer.parseInt(data.split("-")[1]);
	        	  Main.cartePanel.resetSize(Integer.parseInt(mapSize.split(":")[0]), Integer.parseInt(mapSize.split(":")[1]));
	        	  firstLine = false;
	          } else {
	        	  String[] lignData = data.split("@");
	        	  String playerName = lignData[0];
	        	  Player player = getPlayerfromName(playerName);
	        	  //Tuile savedTuile = new Tuile(false, player, currentLign, currentcolumn);
	        	  Tuile savedTuile = Main.cartePanel.tableauTuile[currentLign][currentcolumn];
	        	  savedTuile.owner = player;
	        	  String[] caseDatas = lignData[1].split("-");
		          for (int xIndex = 0; xIndex < 3; xIndex++) {
		        	  for (int yIndex = 0; yIndex < 3; yIndex++) {
	        			  TuileCase currentCase = savedTuile.cases[xIndex][yIndex];
	        			  currentCase.setSaveInfo(caseDatas[xIndex * 3 + yIndex]);
	        		  }
	        	  }
	        	  //Main.cartePanel.tableauTuile[currentLign][currentcolumn].setCopiedTuile(savedTuile);
	        	  currentcolumn++;
	        	  if (currentcolumn == Main.cartePanel.column) {
	        		  currentcolumn = 0;
	        		  currentLign++;
	        	  }
	          }
	        }
	        myReader.close();
	    } catch (FileNotFoundException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	    }		
	}
	
	private void loadHashmapRulesFromString(String saveDatas) {
//		AddDebugLog("saveDatas:" + saveDatas);
		String[] rulesDatas = saveDatas.split("ç");
//		AddDebugLog("rule name:" + rulesDatas[0]);
//		AddDebugLog("rulesDatas:" + rulesDatas[1]);
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
			case "biomeTypes":
				biomeTypes = getIntegerHashMapRuleFromString(rulesDatas[1]);
				break;
		}
	}

	private HashMap<String, String[]> getHashMapRuleFromString(String rulesDatas) {
		HashMap<String, String[]> rule = new HashMap<String, String[]>();
		String[] splitRulesDatas = rulesDatas.split("!!");
		for (String ruleDatas : splitRulesDatas) {
//			AddDebugLog("" + ruleDatas);
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

	private HashMap<String, Integer[]> getIntegerHashMapRuleFromString(String string) {
		HashMap<String, String[]> stringHashMap = getHashMapRuleFromString(string);
		HashMap <String, Integer[]> integerHashMap = new HashMap<String, Integer[]>();
		for (String ruleName : stringHashMap.keySet()) {
			String[] stringValues = stringHashMap.get(ruleName);
			Integer[] integerValues = new Integer[stringValues.length];
			for (int i = 0; i < stringValues.length; i++) {
				integerValues[i] = Integer.parseInt(stringValues[i]);
			}
			integerHashMap.put(ruleName, integerValues);
		}
		return integerHashMap;
	}
	
	public boolean isGeneratingPower(String buildingName) {
		if (buildingName.equals("")) {
			return false;
		}
		return !buildingRules.get(buildingName)[2].equals("");
	}
}
