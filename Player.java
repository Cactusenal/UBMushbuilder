package net.codejava;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import java.awt.Color;

public class Player {
	
	public String playerName;
	public Color playerColor;

	public JRadioButton rBtn;
	public Tuile tuileViewer;
	public JLabel temperatureLabel;
	
	ArrayList<Tuile> placedTuiles = new ArrayList<Tuile>();
	
	public JLabel ressourceLabel;
    int prodFibre;
    int prodSpore;
    int prodSuc;
    int prodPhospho;
    int prodCoins;
	
	public Player(String givenName, Color givenColor) {
		playerName = givenName;
		playerColor = givenColor;
		
		// Gestion ressouces
		ressourceLabel = new JLabel("");
	}

	public void createMenu() {
		rBtn = new JRadioButton(playerName);
		rBtn.setForeground(playerColor);
		//Tuile à poser
		tuileViewer = new Tuile(true, this, 0, 0);
	}
	
	public void addMenu(JPanel panel) {
        panel.add(rBtn);
		panel.add(tuileViewer.tuileBack);
        panel.add(ressourceLabel);
        tuileViewer.randomize(true);
	}
	
	public int getPlayerProd(String ressourceToGet) {
		int ressourceProd = 0;
		for (Tuile tuile : placedTuiles) {
			ressourceProd += tuile.getProduction(ressourceToGet);
		}
		return ressourceProd;
	}
	
	public void updateRessourceInfos() {
		prodFibre = getPlayerProd("prodFibre");
		prodSpore = getPlayerProd("prodSpore");
		prodSuc = getPlayerProd("prodSuc");
		prodPhospho = getPlayerProd("prodPhospho");
		prodCoins = getPlayerProd("prodCoins");
		ressourceLabel.setText("Spore: " + prodSpore + ", Fibre: " + prodFibre + ", Suc: " + prodSuc + ", Phosphorite: "+ prodPhospho + ", MushCoins: " + prodCoins);
	}
}
