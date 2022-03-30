package net.codejava;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.*;

public class Main 
{
	public static Settings settings = null;
	public static GameController gameController = null;
	public static Carte cartePanel;
	public static FilterViews filterViews;
    	
	public static void main(String[] args) 
    {
        // D�finissez le frame
        JFrame frame = new JFrame("Univ. Builder proto");
        
        settings = new Settings();
        settings.sizeImages();

        settings.createPlayersMenus();
        
        filterViews = new FilterViews(settings);

        gameController = new GameController(frame);
        
//        // D�finir le menu principal
//        JMenuBar menu = new JMenuBar();
//        JMenu file = new JMenu("Fichier");
//        JMenu setMenu = new JMenu("Param�tres");
//        JMenu help = new JMenu("Aide");
//        // D�finir les sous-menus
//        JMenuItem newf = new JMenuItem("Nouveau");
//        JMenuItem save = new JMenuItem("Enregistrer");
//        JMenuItem biomeMenu = new JMenuItem("Biomes");
//        JMenuItem tempMenu = new JMenuItem("Temp�ratures");
//        JMenuItem carteMenu = new JMenuItem("Carte");
//        file.add(newf);
//        file.add(save);
//        setMenu.add(biomeMenu);
//        setMenu.add(tempMenu);
//        setMenu.add(carteMenu);
//        menu.add(file);
//        menu.add(setMenu);
//        menu.add(help);
        
        
//        // cr�er un panneau
//        JPanel controllerPanel = new JPanel(); 
        JPanel p2 = new JPanel(); 
        
//        // PANNEAU GAUCHE
//        controllerPanel.add(menu);
//        //ComboBox
//        JComboBox filterSelector = new JComboBox(settings.possibleFilters);
//        controllerPanel.add(filterSelector);
//        
        // D�finition de la carte
        cartePanel = new Carte(settings);//.carteNumberOfLigns, Main.settings.carteNumberOfColumns);
        cartePanel.updateWorldView();
//
//        //Sliders
//        settings.createBiomePopup();
//        settings.createTempPopup();
//        settings.createCartePopup();
//        //Menu de cr�ation
//        JButton createB = new JButton("Cr�er une tuile");
//        JButton turnRB = new JButton("Tourner � droite");
//        JButton buildB = new JButton("Construire");
//        JButton testB = new JButton("Test");
//        JButton clearB = new JButton("Clear");
    	
    	
//        // action listener
//		newf.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//            	settings.AddDebugLog("New world");
//            	cartePanel.resetWorld();
//            }
//        });
//		biomeMenu.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				settings.showBiomePopup();
//			}
//		});
//		tempMenu.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//            	settings.showTempPopup();
//            }
//        });
//		carteMenu.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//            	settings.showCartePopup();
//            }
//        });
//		filterSelector.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//            	String filter = "" + filterSelector.getItemAt(filterSelector.getSelectedIndex());
////            	settings.AddDebugLog(filter);
//            	settings.filterSelected = filter;
//            	cartePanel.updateFilterView();
//            	for (Player player : settings.players) {
//            		player.tuileViewer.updateFilterView(filter);            		
//            	}
//            }
//        });
//		createB.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//            	//settings.AddDebugLog("Cr�ation de tuile");
//            	settings.returnActivePlayer().tuileViewer.randomize();
////        		temperatureLabel.setText("Tuile temp is " + tuileViewer.randomize() + "�C");
//            	//settings.AddDebugLog("" + slider.getValue());
//            }
//        });
//		turnRB.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//            	//settings.AddDebugLog("Trun");
//            	settings.returnActivePlayer().tuileViewer.turnClockWise();
//            }
//        });
//		buildB.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//            	//settings.AddDebugLog("Trun");
//            	buildMode = !buildMode;
//            }
//        });	
//		testB.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
////            	settings.AddDebugLog("biome value 2 is " + BiomeSliders[2].getValue());
////            	settings.AddDebugLog("tableau [2 , 3] Cvalue is " + cartePanel.tableauTuile[2][3].cases[4].getTerrain());
////            	settings.AddDebugLog("" + GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts( ));
////            	settings.AddDebugLog("tableau [2 , 3] temp value is " + cartePanel.tableauTuile[2][3].getTemperature());
//            	settings.AddDebugLog(settings.returnActivePlayer().playerName);
//            }
//        });	
//		clearB.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//            	settings.ClearDebugLog();
//            }
//        });

//
//        
//        settings.addPlayersMenus(controllerPanel);
//        
//        // debug text
//        controllerPanel.add(settings.debugText);
//        
//        controllerPanel.setLayout(new BoxLayout(controllerPanel, BoxLayout.Y_AXIS));
        
        // PANNEAU DROIT
        JScrollPane scrPanel = new JScrollPane(cartePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrPanel.setPreferredSize(new Dimension(1300, 800));
        p2.add(scrPanel);

        // cr�er un s�parateur de panneau
        JSplitPane sep = new JSplitPane(SwingConstants.VERTICAL, gameController.controllerPanel, p2); 
        // d�finir l'orientation du s�parateur
        sep.setOrientation(SwingConstants.VERTICAL);
        // Ajouter le s�parateur
        frame.add(sep);

        frame.pack();
		frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}