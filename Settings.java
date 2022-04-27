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
	HashMap<String, String[]> coinProdRules = new HashMap<String, String[]>();
	Integer rulesLength = 4;
	
	// building datas
	HashMap<String, String[]> buildingRules = new HashMap<String, String[]>();
	String[] dummyRule = {"10", ""};
	String[] farmRule = {"10", ""};
	String[] portRule = {"20", ""};
	String[] generatorRule = {"0", ""};
	HashMap<String, String[]> buildingConditions = new HashMap<String, String[]>();
	String[] dummyCondition = {"biomes,were,to,place", "nearby,biomes,or,buildings", "price in ressources"};
	String[] farmCondition = {"Plaine,Marais", "", ""};
	String[] portCondition = {"Brume", "Brume", ""};
	String[] generatorCondition = {"Désert,Plaine", "", ""};
    
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

    
    JTextArea debugText = new JTextArea();
    JTextArea dataText = new JTextArea();

    
    //CONSTRUCTEUR
    public Settings() {
    	initBuildingsSettings();
    	
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
		
		//MushCoins
		String[] CPort = {"Port", "0", "2"};
		String[] CFarm = {"Ferme", "0", "1"};
		coinProdRules.put("CPort", CPort);
		coinProdRules.put("CFarm", CFarm);
    }
    
    void initBuildingsSettings() {
    	buildingRules.put("Ferme", farmRule);
    	buildingRules.put("Port", portRule);
    	buildingRules.put("Generateur", generatorRule);
    	
    	buildingConditions.put("Ferme", farmCondition);
    	buildingConditions.put("Port", portCondition);
    	buildingConditions.put("Generateur", generatorCondition);
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
    	prodDialog.setBounds(50, 50, 1400, 700);
    	
    	JPanel leftPanel = new JPanel();
    	JPanel rightPanel = new JPanel();
    	
    	JLabel titleLabel = new JLabel("Production rules");
    	JLabel rulesTemplateLabel = new JLabel("Nom de la règle | Biome d'où la règle s'applique | Distance à laquelle la règle s'applique | Production ajoutée aux cases | Biomes où la production ne s'ajoute pas");
//		titleLabel.setSize(titleLabel.getPreferredSize());
    	leftPanel.add(titleLabel);
		titleLabel.setSize(200, 50);

		rightPanel.add(rulesTemplateLabel);

    	for (Object [] prodData: prodDatas) {
    		// Sub-panel Name
    		JLabel subProductionLabel = new JLabel((String) prodData[0]);
//    		subProductionLabel.setSize(subProductionLabel.getPreferredSize());
    		leftPanel.add(subProductionLabel);
    		HashMap<String, String[]> subProdRules = (HashMap<String, String[]>) prodData[1];
    		JPanel subProdPanel = (JPanel) prodData[2];
    		HashMap<String, JTextArea[]> subTextAreaFields = (HashMap<String, JTextArea[]>) prodData[3];
    		
    		// Get sub-panel content
    		subTextAreaFields = setSubProdPanel(subProdPanel, subProdRules);
    		rightPanel.add(subProdPanel);
    	}
    	
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	applyRulesAndUpdateProds();
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
        
        prodDialog.add(buttonPanel);    	
        prodDialog.setLayout(new GridLayout(1, 2));
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
    		if (ruleFromTextFields[1].getText().length() > 0 && ruleFromTextFields[2].getText().length() > 0 && ruleFromTextFields[3].getText().length() > 0) {
    			String[] newRule = new String[rulesLength];
    			for (Integer i = 0; i < rulesLength; i ++) {
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
    		player.tuileViewer.updateProductions();
    		player.tuileViewer.updateFilterView();
    	}
    	Main.cartePanel.updateWorldView();
    }
    
    public void showProdPopup() {
    	fibreTextAreaFields = setSubProdPanel(fibreProdPanel, fibreProdRules);
    	sporeTextAreaFields = setSubProdPanel(sporeProdPanel, sporeProdRules);
    	sucTextAreaFields = setSubProdPanel(sucProdPanel, sucProdRules);
    	phosphoTextAreaFields = setSubProdPanel(phosphoProdPanel, phosphoProdRules);
    	coinTextAreaFields = setSubProdPanel(coinProdPanel, coinProdRules);
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
