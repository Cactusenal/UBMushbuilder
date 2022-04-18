package net.codejava;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class GameController {
	JPanel controllerPanel = new JPanel(); 
	Settings settings = null;
	
	public boolean buildMode = false;
	
    // D�finir le menu principal
    JMenuBar menu = new JMenuBar();
    JMenu file = new JMenu("Fichier");
    JMenu setMenu = new JMenu("Param�tres");
    JMenu help = new JMenu("Aide");
    // D�finir les sous-menus
    JMenuItem newf = new JMenuItem("Nouveau");
    JMenuItem save = new JMenuItem("Enregistrer");
    JMenuItem biomeMenu = new JMenuItem("Biomes");
    JMenuItem carteMenu = new JMenuItem("Carte");
	
	public GameController() {
		this.settings = Main.settings;
		
		// Construction des sous-menus
	    file.add(newf);
	    file.add(save);
	    setMenu.add(biomeMenu);
	    setMenu.add(carteMenu);
	    menu.add(file);
	    menu.add(setMenu);
	    menu.add(help);
	    
	    // cr�er un panneau
	    controllerPanel.add(menu);
	    
	    //ComboBox
	    JComboBox filterSelector = new JComboBox(settings.possibleFilters);
	    controllerPanel.add(filterSelector);
	    
	    //Sliders
	    settings.createBiomePopup();
	    settings.createCartePopup();
	    //Menu de cr�ation
	    JButton createB = new JButton("Cr�er une tuile");
	    JButton turnRB = new JButton("Tourner � droite");
	    JButton buildB = new JButton("Construire");
	    JButton testB = new JButton("Test");
	    JButton clearB = new JButton("Clear");
	    
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
		filterSelector.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	String filter = "" + filterSelector.getItemAt(filterSelector.getSelectedIndex());
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
            	//settings.AddDebugLog("Cr�ation de tuile");
            	settings.returnActivePlayer().tuileViewer.randomize(true);
//        		temperatureLabel.setText("Tuile temp is " + tuileViewer.randomize() + "�C");
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
            }
        });	
		testB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//            	settings.AddDebugLog("biome value 2 is " + BiomeSliders[2].getValue());
//            	settings.AddDebugLog("tableau [2 , 3] Cvalue is " + cartePanel.tableauTuile[2][3].cases[4].getTerrain());
//            	settings.AddDebugLog("" + GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts( ));
//            	settings.AddDebugLog("tableau [2 , 3] temp value is " + cartePanel.tableauTuile[2][3].getTemperature());
            	settings.AddDebugLog(settings.returnActivePlayer().playerName);
            }
        });	
		clearB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	settings.ClearDebugLog();
            }
        });
		
        // boutons
        controllerPanel.add(createB);
        controllerPanel.add(turnRB);
        controllerPanel.add(buildB);
        controllerPanel.add(testB);
        controllerPanel.add(clearB);
        
        settings.addPlayersMenus(controllerPanel);
        
        // debug text
        controllerPanel.add(settings.debugText);
        
        controllerPanel.setLayout(new BoxLayout(controllerPanel, BoxLayout.Y_AXIS));
	}
	
	public void clickOnCarteCase(TuileCase tuileCase, Boolean isViewer) {
    	if (isViewer) {
			tuileCase.changeTerrain();
			tuileCase.updateFilterView();
    	} else if (Main.gameController.buildMode) {
    		tuileCase.setBuilding();
    	} else if (Main.filterViews.filterSelected == "Biome" && tuileCase.building == "Generateur") {
    		Main.filterViews.displayGeneratorPopup(tuileCase);
    	} else if (tuileCase.xPos == 1 && tuileCase.yPos == 1) {
			tuileCase.parentTuile.setCopiedTuile(Main.settings.returnActivePlayer().tuileViewer);
			Main.settings.returnActivePlayer().tuileViewer.randomize(true);    					
    	} else {
//    		Main.settings.AddDebugLog("Case cood is " + xPos + ", " + yPos + ", tuile cood is " + parentTuile.xPos + ", " + parentTuile.yPos);
			Main.settings.AddDebugLog("Filter selected is " + Main.filterViews.filterSelected + ", building is " + tuileCase.building);
    	}
	}
}
