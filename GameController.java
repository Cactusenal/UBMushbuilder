package net.codejava;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class GameController {
	JPanel controllerPanel = new JPanel(); 
	Settings settings = null;
	
	public boolean buildMode = false;
	
    // Définir le menu principal
    JMenuBar menu = new JMenuBar();
    JMenu file = new JMenu("Fichier");
    JMenu setMenu = new JMenu("Paramètres");
    JMenu help = new JMenu("Aide");
    // Définir les sous-menus
    JMenuItem newf = new JMenuItem("Nouveau");
    JMenuItem save = new JMenuItem("Enregistrer");
    JMenuItem biomeMenu = new JMenuItem("Biomes");
    JMenuItem carteMenu = new JMenuItem("Carte");
    JMenuItem prodMenu = new JMenuItem("Productions");
    JMenuItem buildConditionMenu = new JMenuItem("Placement des constructions");
    JMenuItem buildRulesMenu = new JMenuItem("Gestion des constructions");
    // ComboBoxes
    JComboBox<String> filterSelector;
    JComboBox<String> biomeSelector;
	
	public GameController() {
		this.settings = Main.settings;
		
		// Construction des sous-menus
	    file.add(newf);
	    file.add(save);
	    setMenu.add(biomeMenu);
	    setMenu.add(carteMenu);
	    setMenu.add(prodMenu);
	    setMenu.add(buildConditionMenu);
	    setMenu.add(buildRulesMenu);
	    menu.add(file);
	    menu.add(setMenu);
	    menu.add(help);
	    
	    // créer un panneau
	    controllerPanel.add(menu);
	    
	    //ComboBoxes
	    filterSelector = new JComboBox<String>(settings.possibleFilters);
	    controllerPanel.add(filterSelector);
	    String[] biomeTypes = settings.biomeTypes.keySet().toArray(String[]::new);
	    biomeSelector = new JComboBox<String>(biomeTypes);
	    controllerPanel.add(biomeSelector);

	    //Settings popup
	    settings.createBiomePopup();
	    settings.createCartePopup();
	    settings.createProdPopup();
	    settings.createBuildCondPopup();
	    settings.createBuildRulesPopup();
	    //Menu de création
	    JButton createB = new JButton("Créer une tuile");
	    JButton turnRB = new JButton("Tourner à droite");
	    JButton buildB = new JButton("Construire");
	    JButton testB = new JButton("Test");
	    JButton clearB = new JButton("Clear");
	    JButton giveRessourcesB = new JButton("Give");
	    
        // action listeners
		newf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	settings.AddDebugLog("New world");
            	Main.cartePanel.resetWorld();
            }
        });
		biomeMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				settings.showBiomePopup();
			}
		});
		carteMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	settings.showCartePopup();
            }
        });	
		prodMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	settings.showProdPopup();
            }
        });
		buildConditionMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	settings.showBuildConditionPopup();
            }
        });
		buildRulesMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	settings.showBuildRulesPopup();
            }
        });
		filterSelector.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	String filter = (String) filterSelector.getItemAt(filterSelector.getSelectedIndex());
//            	settings.AddDebugLog(filter);
            	Main.filterViews.filterSelected = filter;
            	Main.cartePanel.updateFilterView();
            	for (Player player : settings.players) {
            		player.tuileViewer.updateFilterView();
            	}
            }
        });
		createB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//settings.AddDebugLog("Création de tuile");
            	settings.returnActivePlayer().tuileViewer.randomize(true);
//        		temperatureLabel.setText("Tuile temp is " + tuileViewer.randomize() + "°C");
            	//settings.AddDebugLog("" + slider.getValue());
            }
        });
		turnRB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//settings.AddDebugLog("Trun");
            	settings.returnActivePlayer().tuileViewer.turnClockWise();
            }
        });
		buildB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//settings.AddDebugLog("Trun");
            	buildMode = !buildMode;
            	buildB.setBackground(buildMode? Color.green : Color.red);
            }
        });	
		testB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//            	settings.AddDebugLog("biome value 2 is " + BiomeSliders[2].getValue());
//            	settings.AddDebugLog("tableau [2 , 3] Cvalue is " + cartePanel.tableauTuile[2][3].cases[4].getTerrain());
//            	settings.AddDebugLog("" + GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts( ));
//            	settings.AddDebugLog("tableau [2 , 3] temp value is " + cartePanel.tableauTuile[2][3].getTemperature());
//            	settings.AddDebugLog(settings.returnActivePlayer().playerName);
            	for (String ruleName : settings.fibreProdRules.keySet()) {
        			// Get present rules
            		String[] ruleValues = settings.fibreProdRules.get(ruleName);
            		settings.AddDebugLog("Rule name: " + ruleName);
            		settings.AddDebugLog("" + ruleValues[0]);
            		settings.AddDebugLog("" + ruleValues[1]);
            		settings.AddDebugLog("" + ruleValues[2]);
            		settings.AddDebugLog("" + (ruleValues.length > 3 ? ruleValues[3] : ""));
            	}
            }
        });	
		clearB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	settings.ClearDebugLog();
            }
        });	
		giveRessourcesB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	showGivePopup(settings.returnActivePlayer());
            }
        });
		
        // boutons
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(createB);
		buttonPanel.add(turnRB);
		buttonPanel.add(buildB);
		buttonPanel.add(testB);
		buttonPanel.add(clearB);
		buttonPanel.add(giveRessourcesB);
		buttonPanel.setLayout(new GridLayout(3, 2));
		
		controllerPanel.add(buttonPanel);
        
        settings.addPlayersMenus(controllerPanel);
        
        // debug text
        controllerPanel.add(settings.debugText);
        
        controllerPanel.setLayout(new BoxLayout(controllerPanel, BoxLayout.Y_AXIS));
	}
	
	public void clickOnCarteCase(TuileCase tuileCase, Boolean isViewer) {
    	if (isViewer) {
			tuileCase.changeTerrain();
			tuileCase.updateFilterView();
    	} else if (buildMode) {
    		displayBuildingPopup(tuileCase);
    	} else if (Main.filterViews.filterSelected == "Biome" && tuileCase.building == "Generateur") {
    		displayGeneratorPopup(tuileCase);
    	} else if (tuileCase.xPos == 1 && tuileCase.yPos == 1) {
			tuileCase.parentTuile.setCopiedTuile(settings.returnActivePlayer().tuileViewer);
			settings.returnActivePlayer().tuileViewer.randomize(true);    					
    	} else {
//    		Main.settings.AddDebugLog("Case cood is " + xPos + ", " + yPos + ", tuile cood is " + parentTuile.xPos + ", " + parentTuile.yPos);
			Main.settings.AddDebugLog("Filter selected is " + Main.filterViews.filterSelected + ", building is " + tuileCase.building);
			Main.settings.AddDebugLog("Building part is " + tuileCase.buildingParts.get(0));
//			settings.AddDebugLog("Filter selected is " + Main.filterViews.filterSelected + ", prod is " + tuileCase.prodFibre);
    	}
	}
	
	public void displayBuildingPopup (TuileCase tuileCase) {
		// General popup graphic params
    	JFrame buildCondFrame = new JFrame("What to build?");
        JDialog buildCondDialog = new JDialog(buildCondFrame);

        buildCondDialog.setBounds(200, 200, 500, 100);
        
        // List of possible buildings to construct
        HashMap<String, String[]> buildingConditions = settings.buildingConditions;
        List<String> possibleBiomesList = Arrays.asList(settings.possibleBiomes);
        String[] directions = {"X+", "X-", "Y+", "Y-"};
        Integer buildableNumber = 0;
        for (String buildCondName : buildingConditions.keySet()) {
    		boolean isCurrentCaseOk = false;
    		boolean isNearbyOk = true;
        	String[] buildCond = buildingConditions.get(buildCondName);
    		String[] conditionsOnCurrentCase = buildCond[0].split(",");
    		String[] nearbyRequired = buildCond[1].split(",");
    		for (String condition : conditionsOnCurrentCase) {
				if (possibleBiomesList.contains(condition)) {
					if (condition.equals(tuileCase.terrain)) {
						isCurrentCaseOk = true;
						break;
					}
				} else {
					if (condition.equals(tuileCase.building)) {
						isCurrentCaseOk = true;
						break;
					}
    			}
    		}
    		if (nearbyRequired.length > 0) {
    			isNearbyOk = false;
    			for (String direction : directions) {
    				TuileCase relativeCase = tuileCase.getRelativeCase(direction);
	    			if (relativeCase != null) {
	    				for (String condition : nearbyRequired) {
	    					if (possibleBiomesList.contains(condition)) {
	    						if (condition.equals(relativeCase.terrain)) {
	    							isNearbyOk = true;
	    							break;
	    						}
	    					} else {
	    						if (condition.equals(relativeCase.building)) {
	    							isNearbyOk = true;
	    							break;
	    						}
	    					}
	    				}
	    				if (isNearbyOk) {break;}	    				
	    			}
    			}
    		}
    		if (isCurrentCaseOk && isNearbyOk && !buildCondName.equals(tuileCase.building)) {
    			JLabel buildingName = new JLabel(buildCondName);
    			JButton buidButton = new JButton("Construire");
    			buidButton.addActionListener(new ActionListener() {
    	            public void actionPerformed(ActionEvent e) {
    	            	tuileCase.setBuilding(buildCondName);
    	            	buildCondDialog.setVisible(false);
    	                // Update nearby prods
    	        		for (String direction : directions) {
    	        			TuileCase relativeCase = tuileCase.getRelativeCase(direction);
    	        			if (relativeCase != null) {
    	        				relativeCase.parentTuile.updateProductions();
    	        				relativeCase.parentTuile.owner.updateRessourceInfos();
    	        				relativeCase.updateFilterView();
    	        			}
    	        		}
    	        		tuileCase.updateFilterView();
    	            }
    	        });	
    			buildCondDialog.add(buildingName);
    			buildCondDialog.add(buidButton);
    			buildableNumber++;
    		}
        }
        if (buildableNumber == 0) {
        	buildCondDialog.add(new JLabel("No possible buildings"));
        }
        buildCondDialog.setLayout(new GridLayout(buildableNumber, 2));
        buildCondDialog.setVisible(true);
	}
	
    public void displayGeneratorPopup (TuileCase tuileCase) {
        // General popup graphic params
    	JFrame generatorFrame = new JFrame("Generator energy");
        JDialog generatorDialog = new JDialog(generatorFrame);
    	    	
        generatorDialog.setBounds(200, 200, 500, 100);
        
        // List of possible buildings to power creation
        int buildingNumber = 0;
        List<Object[]> buildingsToPowerList = new ArrayList<Object[]>();
        
        for (TuileCase caseFromDistance : tuileCase.getCasesFromDistance(3)) {
        	String buildingFromDistance = caseFromDistance.building;
        	if (buildingFromDistance != "" && settings.getPowerCons(buildingFromDistance) > 0) {
        		// Getting building position
        		Integer buildX = caseFromDistance.parentTuile.xPos * 3 + caseFromDistance.xPos;
        		Integer buildY = caseFromDistance.parentTuile.yPos * 3 + caseFromDistance.yPos;
        		settings.AddDebugLog("" + buildingFromDistance);
        		JLabel buildingLabel = new JLabel(buildingFromDistance + " (" + buildX + "," + buildY +  ")");
        		// Getting how much power is needed
        		Integer powerCons = settings.getPowerCons(buildingFromDistance);
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
    						
    		        		settings.AddDebugLog("Powering this building" + buildingLine[0]);
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
    
    public void showGivePopup(Player activePlayer) {
    	JDialog giveDialog = new JDialog();
    	giveDialog.setBounds(200, 200, 800, 600);
    	
        JLabel titleLabel = new JLabel("Enter the ressoures to give to " + activePlayer.playerName);

        JPanel prodToAddPanel = new JPanel();
        
        prodToAddPanel.add(new JLabel("Spore"));
        JTextArea sporeTextArea = new JTextArea ("" + activePlayer.givenProdSpore);
        prodToAddPanel.add(sporeTextArea);
        prodToAddPanel.add(new JLabel("Fibre"));
        JTextArea fibreTextArea = new JTextArea ("" + activePlayer.givenProdFibre);
        prodToAddPanel.add(fibreTextArea);
        prodToAddPanel.add(new JLabel("Suc"));
        JTextArea sucTextArea = new JTextArea ("" + activePlayer.givenProdSuc);
        prodToAddPanel.add(sucTextArea);
        prodToAddPanel.add(new JLabel("Phosphorite"));
        JTextArea phosphoTextArea = new JTextArea ("" + activePlayer.givenProdPhospho);
        prodToAddPanel.add(phosphoTextArea);
        prodToAddPanel.add(new JLabel("MushCoins"));
        JTextArea coinsTextArea = new JTextArea ("" + activePlayer.givenProdCoins);
        prodToAddPanel.add(coinsTextArea);
        
        prodToAddPanel.setLayout(new GridLayout(5, 2));
        
        JButton jButton = new JButton("Apply");
        jButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	activePlayer.givenProdSpore = Integer.parseInt(sporeTextArea.getText());
            	activePlayer.givenProdFibre = Integer.parseInt(fibreTextArea.getText());
            	activePlayer.givenProdSuc = Integer.parseInt(sucTextArea.getText());
            	activePlayer.givenProdPhospho = Integer.parseInt(phosphoTextArea.getText());
            	activePlayer.givenProdCoins = Integer.parseInt(coinsTextArea.getText());
            	activePlayer.updateRessourceInfos();
                giveDialog.setVisible(false);
            }
        });

        giveDialog.add(titleLabel);
        giveDialog.add(prodToAddPanel);
        giveDialog.add(jButton);
        
        giveDialog.setLayout(new GridLayout(3, 1));
        giveDialog.setVisible(true);
    }
}
