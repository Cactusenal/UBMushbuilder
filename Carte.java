package net.codejava;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLayeredPane;

public class Carte extends JLayeredPane {
	public Tuile [][] tableauTuile;
	public int lign;
	public int column;
	int ecartTuile = 120;
	int caseSize = ecartTuile*2;
	boolean isIso = true;
	Settings mainSettings = null;
	
	Player mondePlayer = new Player("Monde", Color.LIGHT_GRAY);
	
	public Carte(Settings settings) {
		mainSettings = settings;
		lign = mainSettings.carteNumberOfLigns;
		column = mainSettings.carteNumberOfColumns;
		//créer tableau
        tableauTuile = new Tuile [lign][column];
        //Spécifier la couleur d'arrière-plan du JPanel
        setBackground(Color.white);
        
        if (isIso) {
        	// Pour le layout
        	int sideSize = column + lign;
        	setPreferredSize(new Dimension(sideSize * caseSize, sideSize * caseSize));
        	
        	
        	for (int i = 0; i < lign; i++) {
        		for (int j = 0; j < column; j++) {
        			Tuile tuile = new Tuile(false, mondePlayer, j, i);
        			tableauTuile[i][j] = tuile;
        			
        			// layout
    	    		int gridx = lign - 1 + j - i;
    	    		int gridy = j + i;

    	    		add(tuile.tuileBack);
    	    		tuile.tuileBack.setBounds(gridx*ecartTuile, gridy*ecartTuile, ecartTuile * 2, ecartTuile * 2);        		
    	    		
    	    		// Gestion de la profondeur
    	        	setLayer(tuile.tuileBack, gridy);
        		}
        	}
        } else {
        	for (int i = 0; i < lign; i++) {
        		for (int j = 0; j < column; j++) {
        			Tuile tuile = new Tuile(false, mondePlayer, j, i);
        			tableauTuile[i][j] = tuile;
        			add(tuile.tuileBack);
        		}
        	}
        	setLayout(new GridLayout(lign, column));
        	setPreferredSize(new Dimension(column * caseSize, lign * caseSize));
        }
	}

	Tuile getTuile(int x, int y) {
//		Main.settings.AddDebugLog("get tuile in position " + x + ", " + y);
		if (y < 0 || y > lign - 1) {
			return null;
		}
		if (x < 0 || x > column - 1) {
			return null;
		}
		return tableauTuile[y][x];
	}
	
	void updateWorldView() {
		updateWorldProd();
		updateFilterView();
	}
	
	public void updateWorldProd() {
		for (Tuile[] tuileLign : tableauTuile) {
			for (Tuile tuile : tuileLign) {
				tuile.updateTuileProductions();
			}
		}
		mainSettings.updatePlayersProd();
	}
	
	public void updateFilterView() {
		for (Tuile[] tuileLign : tableauTuile) {
			for (Tuile tuile : tuileLign) {
				tuile.updateFilterView();
			}
		}
	}
	
	public void resetWorld() {
		Tuile blankTuile = new Tuile(false, mondePlayer, 0, 0);
		for (Tuile[] tuileLign : tableauTuile) {
			for (Tuile tuile : tuileLign) {
				tuile.setCopiedTuile(blankTuile);
			}
		}
	}
	
	public void resetSize(int ligns, int columns) {
		removeAll();
		lign = ligns;
		column = columns;
		tableauTuile = new Tuile [ligns][columns];
		for (int i = 0; i < ligns; i++) {
    		for (int j = 0; j < columns; j++) {
    			Tuile tuile = new Tuile(false, mondePlayer,j, i);
    			tableauTuile[i][j] = tuile;
    			add(tuile.tuileBack);
    		}
    	}
    	setLayout(new GridLayout(ligns, columns));
        setPreferredSize(new Dimension(columns * caseSize, ligns * caseSize));
	}

	public void randomize() {
		for (Tuile[] tuileLign : tableauTuile) {
			for (Tuile tuile : tuileLign) {
				tuile.randomize(false);
				Player randomPlayer = mainSettings.getRandomPlayer();
				tuile.owner = randomPlayer;
				randomPlayer.placedTuiles.add(tuile);
			}
		}		
	}
}
