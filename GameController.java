package net.codejava;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
import javax.swing.JOptionPane;
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
    JMenuItem saveB = new JMenuItem("Sauvegarde des paramètres");
    JMenuItem loadB = new JMenuItem("Chargement de la sauvegarde");
    JMenuItem biomeMenu = new JMenuItem("Biomes");
    JMenuItem carteMenu = new JMenuItem("Carte");
    JMenuItem prodMenu = new JMenuItem("Productions");
    JMenuItem buildConditionMenu = new JMenuItem("Placement des constructions");
    JMenuItem buildRulesMenu = new JMenuItem("Gestion des constructions");
    // ComboBoxes
    JComboBox<String> filterSelector;
    JComboBox<String> biomeSelector;
    JComboBox<String> seasonSelector;
    JComboBox<String> overviewSelector;
    String[] possibleViews = {"Aucune", "Tuiles", "Cases"};
    // Time label
    JLabel timeLabel = new JLabel();
    Integer timeIteration = 0;
    // Variables for generator dialog
    Object[][] buildingsToPower;
    Integer buildingNumber;
    JLabel energyCounter = new JLabel();
    Integer energyProduced;
    Integer energyProvided;
	
	public GameController() {
		this.settings = Main.settings;
		
		// Construction des sous-menus
	    file.add(newf);
	    file.add(save);
	    file.add(saveB);
	    file.add(loadB);
	    setMenu.add(biomeMenu);
	    setMenu.add(carteMenu);
	    setMenu.add(prodMenu);
	    setMenu.add(buildConditionMenu);
	    setMenu.add(buildRulesMenu);
	    menu.add(file);
	    menu.add(setMenu);
	    menu.add(help);
	    
	    // créer un panneau
	    JPanel menuPanel = new JPanel();
	    menuPanel.add(menu);
	    menuPanel.add(timeLabel);
	    menuPanel.setLayout(new GridLayout(1, 2));
	    controllerPanel.add(menuPanel);

	    //ComboBoxes
	    String[] biomeTypes = settings.biomeTypes.keySet().toArray(String[]::new);
	    filterSelector = new JComboBox<String>(Main.filterViews.possibleFilters);
	    biomeSelector = new JComboBox<String>(biomeTypes);
	    seasonSelector = new JComboBox<String>(Main.settings.possibleSeasons);
	    overviewSelector = new JComboBox<String>(possibleViews);
	    JPanel comboBoxesPanel = new JPanel();
	    comboBoxesPanel.add(filterSelector);
	    comboBoxesPanel.add(biomeSelector);
	    comboBoxesPanel.add(seasonSelector);
	    comboBoxesPanel.add(overviewSelector);
	    comboBoxesPanel.setLayout(new GridLayout(2, 2));
	    controllerPanel.add(comboBoxesPanel);

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
	    JButton timeB = new JButton("Itérer temps");
	    
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
		seasonSelector.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	settings.currentSeason = seasonSelector.getItemAt(seasonSelector.getSelectedIndex());
            	Main.cartePanel.updateWorldView();
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
		timeB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	IterateWorld();

            }

        });
		saveB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	settings.saveRules();
            }
        });
		loadB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	settings.readSaveFile("UBprodSave");
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
		buttonPanel.add(timeB);
		buttonPanel.setLayout(new GridLayout(4, 2));
		
		controllerPanel.add(buttonPanel);
        
        settings.addPlayersMenus(controllerPanel);
        
        // debug text
        controllerPanel.add(settings.debugText);
        
        controllerPanel.setLayout(new BoxLayout(controllerPanel, BoxLayout.Y_AXIS));
        
        refreshTimeLabel();
	}
	
	public void refreshTimeLabel() {
		timeLabel.setText("  Itération #" + timeIteration);
	}

	public void resetBiomeSelectorValues() {
		biomeSelector.removeAllItems();
		for (String biomeType : settings.biomeTypes.keySet()) {
			biomeSelector.addItem(biomeType);
		}
	}

	public void clickOnCarteCase(TuileCase tuileCase, Boolean isViewer) {
    	if (isViewer) {
			tuileCase.changeTerrain();
			tuileCase.updateFilterView();
    	} else if (!tuileCase.inConstruction.equals("")) {
    		displayInContruction(tuileCase);
    	} else if (buildMode) {
        	displayBuildingPopup(tuileCase);
    	} else if (Main.filterViews.filterSelected == "Biome" && !tuileCase.building.equals("") && !settings.buildingRules.get(tuileCase.building)[2].equals("")) {
    		displayGeneratorPopup(tuileCase);
    	} else if (tuileCase.xPos == 1 && tuileCase.yPos == 1) {
			tuileCase.parentTuile.setCopiedTuile(settings.returnActivePlayer().tuileViewer);
			settings.returnActivePlayer().tuileViewer.randomize(true);    					
    	} else {
//    		Main.settings.AddDebugLog("Case cood is " + xPos + ", " + yPos + ", tuile cood is " + parentTuile.xPos + ", " + parentTuile.yPos);
			Main.settings.AddDebugLog("Filter selected is " + Main.filterViews.filterSelected + ", building is " + tuileCase.building);
//			settings.AddDebugLog("Building part is " + ((tuileCase.buildingParts == null || tuileCase.buildingParts.length == 0) ? "no building part" : tuileCase.buildingParts[0]));
//			settings.AddDebugLog("Filter selected is " + Main.filterViews.filterSelected + ", prod is " + tuileCase.prodFibre);
    	}
	}
	
	public void displayInContruction(TuileCase tuileCase) {
		// General popup graphic params
    	JFrame inContructFrame = new JFrame("Building in construction");
        JDialog inConstructDialog = new JDialog(inContructFrame);

        inConstructDialog.setBounds(200, 200, 300, 200);
        JLabel buildingName = new JLabel(tuileCase.inConstruction + " en construction");
        inConstructDialog.add(buildingName);
        String[] priceInRessource = settings.getRessourcePrice(tuileCase.inConstruction).split(",");
        addConstructionRessourceLabel(inConstructDialog, "Fibre", tuileCase.buildFibre, priceInRessource[0]);
        addConstructionRessourceLabel(inConstructDialog, "Spore", tuileCase.buildSpore, priceInRessource[1]);
        addConstructionRessourceLabel(inConstructDialog, "Suc", tuileCase.buildSuc, priceInRessource[2]);
        addConstructionRessourceLabel(inConstructDialog, "Phosphorite", tuileCase.buildPhospho, priceInRessource[3]);
        inConstructDialog.setLayout(new GridLayout(5, 1));
        inConstructDialog.setVisible(true);
	}
	
	public void addConstructionRessourceLabel(JDialog dialog, String ressource, Integer currentRessource, String requiredRessource) {
    	JLabel necessaryRessources = new JLabel (ressource + " required: " + currentRessource + "/" + requiredRessource);
		dialog.add(necessaryRessources);
	}

	public void displayBuildingPopup (TuileCase tuileCase) {
		// General popup graphic params
    	JFrame buildCondFrame = new JFrame("What to build?");
        JDialog buildCondDialog = new JDialog(buildCondFrame);

        
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
						// Construction de module: Vérifier si emplacement de module libre
						Integer nbPartsPostition = 0;
						String[] partList = {};
						switch (conditionsOnCurrentCase[1]) {
							case "sol":
								nbPartsPostition = Integer.valueOf(settings.buildingRules.get(tuileCase.building)[0].split(",")[0]);
								partList = tuileCase.floorBuildingParts;
								break;
							case "mur":
								nbPartsPostition = Integer.valueOf(settings.buildingRules.get(tuileCase.building)[0].split(",")[1]);
								partList = tuileCase.wallBuildingParts;
								break;
							case "toit":
								nbPartsPostition = Integer.valueOf(settings.buildingRules.get(tuileCase.building)[0].split(",")[2]);
								partList = tuileCase.roofBuildingParts;
								break;
						}
						if (partList.length < nbPartsPostition) {
							isCurrentCaseOk = true;
							break;
						}
					}
    			}
    		}
    		if (nearbyRequired.length > 0) {
    			for (String condition : nearbyRequired) {
    				boolean isNearbyConditionOk = false;
	    			for (String direction : directions) {
	    				TuileCase relativeCase = tuileCase.getRelativeCase(direction);
		    			if (relativeCase != null) {
	    					if (possibleBiomesList.contains(condition)) {
	    						if (condition.equals(relativeCase.terrain)) {
	    							isNearbyConditionOk = true;
	    							break;
	    						}
	    					} else {
	    						if (condition.equals(relativeCase.building)) {
	    							isNearbyConditionOk = true;
	    							break;
	    						}
	    					}
	    				}
	    			}
	    			if (!isNearbyConditionOk) {
	    				isNearbyOk = false;
	    				break;
	    			}	    				
    			}
    		}
    		if (isCurrentCaseOk && isNearbyOk && !buildCondName.equals(tuileCase.building)) {
    			JLabel buildingName = new JLabel(buildCondName);
    			JButton buidButton = new JButton("Construire");
    			buidButton.addActionListener(new ActionListener() {
    	            public void actionPerformed(ActionEvent e) {
    	            	tuileCase.startConstruction(buildCondName);
    	            	buildCondDialog.setVisible(false);
    	                // Update nearby prods
    	        		for (String direction : directions) {
    	        			TuileCase relativeCase = tuileCase.getRelativeCase(direction);
    	        			if (relativeCase != null) {
    	        				relativeCase.parentTuile.updateTuileProductions();
    	        				relativeCase.parentTuile.owner.updateRessourceInfos();
    	        				relativeCase.updateFilterView();
    	        			}
    	        		}
    	        		tuileCase.parentTuile.updateTuileProductions();
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
        	buildableNumber = 1;
        }
        buildCondDialog.setLayout(new GridLayout(buildableNumber, 2));
        buildCondDialog.setBounds(200, 200, 500, 50 + buildableNumber * 50);
        buildCondDialog.setVisible(true);
	}
	
    public void displayGeneratorPopup (TuileCase tuileCase) {
        // General popup graphic params
    	JFrame generatorFrame = new JFrame("Generator energy");
        JDialog generatorDialog = new JDialog(generatorFrame);
        
        // List of possible buildings to power creation
        List<Object[]> buildingsToPowerList = new ArrayList<Object[]>();
        
        energyProduced = Integer.parseInt(settings.buildingRules.get(tuileCase.building)[2]);
        Integer generatorRange = Integer.parseInt(settings.buildingRules.get(tuileCase.building)[3]);
        // Initialize counters
        buildingNumber = 0;
        energyProvided = 0;
        
        for (TuileCase caseFromDistance : tuileCase.getCasesFromDistance(generatorRange)) {
        	String buildingFromDistance = caseFromDistance.building;
        	if (buildingFromDistance != "") {
        		// Getting buildings case position
        		Integer buildX = caseFromDistance.getCaseXPos();
        		Integer buildY = caseFromDistance.getCaseYPos();
        		if (settings.getPowerCons(buildingFromDistance) > 0) {
        			createBuildingLineForPower(generatorDialog, buildingFromDistance, caseFromDistance, buildX, buildY, tuileCase, buildingsToPowerList);
        		}
        		String[][] buildingPartLists = {caseFromDistance.floorBuildingParts, caseFromDistance.wallBuildingParts, caseFromDistance.roofBuildingParts};
        		for (String[] buildingPartList : buildingPartLists) {
        			for (String buildingPart : buildingPartList) {
        				if (settings.getPowerCons(buildingPart) > 0) {
        					createBuildingLineForPower(generatorDialog, buildingPart, caseFromDistance, buildX, buildY, tuileCase, buildingsToPowerList);
        				}
        			}        			
        		}
        	}
        }
        // Construct Dialog
        if (buildingNumber == 0) {
    		JLabel noBuildingLabel = new JLabel("No building to power in the area");
    		generatorDialog.add(noBuildingLabel);
        }
        else {
        	buildingsToPower = new Object[buildingsToPowerList.size()][4];
        	buildingsToPowerList.toArray(buildingsToPower);
        	JButton applyPower = new JButton("Apply power connections");
    		generatorDialog.add(applyPower);
    		generatorDialog.add(energyCounter);
    		recomputeGeneratorEnergyCounter();
    		// Apply button
    		applyPower.addActionListener(new ActionListener(){
    			// Apply the list of powered building to generator
    			public void actionPerformed(ActionEvent e){
    				applyGeneratorConnections(tuileCase);
    				for (TuileCase caseFromDistance : tuileCase.getCasesFromDistance(generatorRange)) {
    					caseFromDistance.updateCaseProduction();
    				}
    				tuileCase.updateCaseProduction();
    				settings.updatePlayersProd();
    				generatorDialog.setVisible(false);
    			}
    		});
        }
        generatorDialog.setLayout(new GridLayout(buildingNumber + 1, 2));
        Integer panelHeight = 100 + (50 * buildingNumber);
        generatorDialog.setBounds(200, 200, 500, panelHeight);
        generatorDialog.setVisible(true);
    }
    
    private void createBuildingLineForPower(JDialog generatorDialog, String buildingFromDistance, TuileCase buildingCase, Integer buildX, Integer buildY, TuileCase generatorCase, List<Object[]> buildingsToPowerList) {
		settings.AddDebugLog("" + buildingFromDistance);
		JLabel buildingLabel = new JLabel(buildingFromDistance + " (" + buildX + "," + buildY +  ")");
		// Getting how much power is needed
		Integer powerCons = settings.getPowerCons(buildingFromDistance);
		JCheckBox powerCheckBox = new JCheckBox("" + powerCons);
		generatorDialog.add(buildingLabel);
		generatorDialog.add(powerCheckBox);
		Object[] buildingLine = {buildingFromDistance, buildX, buildY, powerCheckBox};
		buildingsToPowerList.add(buildingLine);
		buildingNumber++;
		Boolean alreadyPowered = false;
		for (Object [] buildingPowered : generatorCase.buildingsPowered) {
			// TODO: Check for non possible buildings
			// Check if the building is already powered by this generator, and check box if so
			if ((String)buildingPowered[0] == buildingFromDistance && (Integer)buildingPowered[1] == buildX && (Integer)buildingPowered[2] == buildY) {
				// TODO: remove boolean in tuileCase.buildingsPowered object ?
				powerCheckBox.setSelected((boolean) buildingPowered[3]);
				energyProvided += powerCons;
				alreadyPowered = true;
				break;
			}
		}
		boolean isPoweredByOther = buildingCase.checkIfBuildingPowered(buildingFromDistance);
		powerCheckBox.setEnabled(!isPoweredByOther || alreadyPowered);
		// Update of energy consuption when a checkbox is touched
		powerCheckBox.addActionListener(new ActionListener(){
			// Apply the list of powered building to generator
			public void actionPerformed(ActionEvent e){
				recomputeGeneratorEnergyCounter();
			}
		});
    }
    
    public Integer recomputeGeneratorEnergyCounter() {
		Integer energyToProvide = 0;
		for (Object [] buildingLine : buildingsToPower) {
			Boolean isPoweredHere = ((JCheckBox) buildingLine[3]).isSelected();
			if (isPoweredHere) {
				energyToProvide += settings.getPowerCons((String) buildingLine[0]);
			}
		}
    	energyCounter.setText("(" + energyToProvide + "/" + energyProduced + ")");
    	
    	energyCounter.setForeground(energyToProvide > energyProduced ? Color.red : Color.black);

    	return energyToProvide;
    }
    
    public void applyGeneratorConnections(TuileCase generatorCase) {
		Integer energyToProvide = recomputeGeneratorEnergyCounter();

		if (energyToProvide > energyProduced) {
    		JOptionPane.showMessageDialog(Main.frame,
    			    "Too much energy to provide for this generator",
    			    "Inane warning",
    			    JOptionPane.WARNING_MESSAGE);
		} else {
			// Set buildingsPowered from constructed list
			List<Object[]> buildingsPoweredList = new ArrayList<Object[]>();
			for (Object [] buildingLine : buildingsToPower) {
				Boolean isPoweredHere = ((JCheckBox) buildingLine[3]).isSelected();
				if (isPoweredHere) {
					buildingLine[3] = isPoweredHere;
					buildingsPoweredList.add(buildingLine);
					}
			}
			Object [][] buildingsPoweredArray =  new Object[buildingsPoweredList.size()][4];
			buildingsPoweredList.toArray(buildingsPoweredArray);
			generatorCase.buildingsPowered = buildingsPoweredArray;
		}
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
        JTextArea coinsTextArea = new JTextArea ();//"" + activePlayer.givenCoins);
        prodToAddPanel.add(coinsTextArea);
        
        prodToAddPanel.setLayout(new GridLayout(5, 2));
        
        JButton jButton = new JButton("Apply");
        jButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	activePlayer.givenProdSpore = Integer.parseInt(sporeTextArea.getText());
            	activePlayer.givenProdFibre = Integer.parseInt(fibreTextArea.getText());
            	activePlayer.givenProdSuc = Integer.parseInt(sucTextArea.getText());
            	activePlayer.givenProdPhospho = Integer.parseInt(phosphoTextArea.getText());
            	activePlayer.mushCoins += Integer.parseInt(coinsTextArea.getText());
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
    
    private void IterateWorld() {
    	timeIteration++;
    	consumeSucAndSetActivity();
    	setWorldRemainingProd();
    	refreshTimeLabel();
    	feedSucStock();
    	produceMushCoins();
    	IterateCarte();
    	Main.cartePanel.updateWorldProd();
    	Main.cartePanel.updateWorldView();
    }

	private String consumeSucAndSetActivity() {
		for (Tuile[] tuileLign : Main.cartePanel.tableauTuile) {
			for (Tuile tuile : tuileLign) {
				for (TuileCase[] tuileCaseLign : tuile.cases) {
					for (TuileCase tuileCase : tuileCaseLign) {
						tuileCase.consumeSucAndSetActivity();
					}
				}
			}
		}
		return null;
	}	
	
	private String setWorldRemainingProd() {
		for (Tuile[] tuileLign : Main.cartePanel.tableauTuile) {
			for (Tuile tuile : tuileLign) {
				for (TuileCase[] tuileCaseLign : tuile.cases) {
					for (TuileCase tuileCase : tuileCaseLign) {
						tuileCase.setRemainingProd();
					}
				}
			}
		}
		return null;
	}

	private void feedSucStock() {
		for (Tuile[] tuileLign : Main.cartePanel.tableauTuile) {
			for (Tuile tuile : tuileLign) {
				for (TuileCase[] tuileCaseLign : tuile.cases) {
					for (TuileCase tuileCase : tuileCaseLign) {
						tuileCase.feedSucStock();
					}
				}
			}
		}		
	}

	private void produceMushCoins() {
		for (Player player : settings.players) {
			Integer prodCoins = player.getPlayerProd("prodCoins");
			player.mushCoins += prodCoins;
			player.refreshProdLabel();
		}
	}

	private void IterateCarte() {
		for (Tuile[] tuileLign : Main.cartePanel.tableauTuile) {
			for (Tuile tuile : tuileLign) {
				for (TuileCase[] tuileCaseLign : tuile.cases) {
					for (TuileCase tuileCase : tuileCaseLign) {
						tuileCase.iterateConstruction();
					}
				}
			}
		}
	}
}
