package net.codejava;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

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
    JMenuItem tempMenu = new JMenuItem("Températures");
    JMenuItem carteMenu = new JMenuItem("Carte");
	
	public GameController() {
		this.settings = Main.settings;
		
		// Construction des sous-menus
	    file.add(newf);
	    file.add(save);
	    setMenu.add(biomeMenu);
	    setMenu.add(tempMenu);
	    setMenu.add(carteMenu);
	    menu.add(file);
	    menu.add(setMenu);
	    menu.add(help);
	    
	    // créer un panneau
	    controllerPanel.add(menu);
	    
	    //ComboBox
	    JComboBox filterSelector = new JComboBox(settings.possibleFilters);
	    controllerPanel.add(filterSelector);
	    
	    //Sliders
	    settings.createBiomePopup();
	    settings.createTempPopup();
	    settings.createCartePopup();
	    //Menu de création
	    JButton createB = new JButton("Créer une tuile");
	    JButton turnRB = new JButton("Tourner à droite");
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
		tempMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	settings.showTempPopup();
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
            	//settings.AddDebugLog("Création de tuile");
            	settings.returnActivePlayer().tuileViewer.randomize();
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
	
	
}
