package net.codejava;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.*;

public class Settings {
    // Param carte
	int carteNumberOfLigns = 12;
    int carteNumberOfColumns = 15;
    
    //Param players
    int numberOfPlayers = 3;
    public Player[] players = new Player[numberOfPlayers];
    Color[] playersColors = {Color.blue, Color.red, Color.green};
    
    // UNUSED: temp datas/params
    int minTemp = -15;
    int maxTemp = 45;
    public String[][] tempLevels = new String[][] {
    	{"Temp tr�s basse : bleu", "0", "5"},
    	{"Temp basse: cyan", "1", "15"},
    	{"Temp haute: jaune", "2", "25"},
    	{"Temp tr�s haute: rouge", "3", "35"}
    };
	
    //DATAS
    String [] possibleFilters = {"Biome", "Player", "Fibre", "Spore", "Min�raux", "MushCoins"};
    String[] directions = {"NO", "N", "NE", "O", "C", "E", "SO", "S", "SE"};
    
    //biomes datas
	String[] possibleBiomes = {"Brume", "Plaine", "Hauteurs", "Foret", "D�sert", "Marais"};
    
    //params selecteurs
    Sliders[] BiomeSliders;
    Sliders[] TempSliders;
    JFrame biomeFrame = new JFrame("Biomes settings");
    JFrame tempFrame = new JFrame("Temperatures settings");
    JFrame carteFrame = new JFrame("Carte settings");
    JDialog biomeDialog = new JDialog(biomeFrame);
    JDialog tempDialog = new JDialog(tempFrame);
    JDialog carteDialog = new JDialog(carteFrame);
    
    JTextArea debugText = new JTextArea();
    JTextArea dataText = new JTextArea();

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
//		Main.temperatureLabel.setText("Tuile temp is " + temperature + "�C");
		return players[playerIndex];
    }
    
    //UNUSED TEMP
    public void createTempPopup() {
    	tempDialog.setLayout(new FlowLayout());

    	tempDialog.setBounds(200, 200, 900, 200);

        JLabel jLabel = new JLabel("Press apply button to apply settings and update temp view");
        
        JPanel tempPanel = new JPanel();
        createTempSliders(tempPanel);
        
        JButton jButton = new JButton("Apply");
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	Main.cartePanel.updateFilterView();
            }
        });

        tempDialog.add(tempPanel);
        tempDialog.add(jLabel);
        tempDialog.add(jButton);
    }
    
    public void showTempPopup() {
        tempDialog.setVisible(true);
    }
    
    //BIOME SETTINGS
    public void createBiomePopup() {
    	biomeDialog.setLayout(new FlowLayout());
    	
    	biomeDialog.setBounds(200, 200, 1500, 200);
        
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
    	carteDialog.setLayout(new FlowLayout());
    	
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

    //TEMP SETTINGS
    void createTempSliders(JPanel sliderPanel) {
        //Sliders
    	TempSliders = new Sliders[tempLevels.length];
        int i = 0;
        for (String[] tempLevel : tempLevels) {
        	Sliders slider = new Sliders(tempLevel[0], minTemp, maxTemp, Integer.parseInt(tempLevel[2]));
        	sliderPanel.add(slider);
        	TempSliders[i] = slider;
        	i++;
        }
    }
    
    Color getTempColor(int temperature) {
    	int[] tempValues = getSlidersValue(TempSliders);
    	if (temperature <= tempValues[0]) {
    		return Color.blue;
    	} else if (temperature <= tempValues[1]) {
    		return Color.cyan;
    	} else if (temperature >= tempValues[3]) {
    		return Color.red;
    	} else if (temperature > tempValues[2]) {
    		return Color.yellow;
    	} else {
    		return Color.green;
    	}
    }

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
