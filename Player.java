package net.codejava;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

import java.awt.Color;

public class Player {
	
	public String playerName;
	public Color playerColor;

	public JRadioButton rBtn;
	public Tuile tuileViewer;
	public JLabel temperatureLabel;
	
	ArrayList<Tuile> placedTuiles = new ArrayList<Tuile>();
	
	public JTextArea ressourceLabel;
    int prodFibre;
    int prodSpore;
    int prodSuc;
    int prodPhospho;
    int prodCoins;
    
    Integer mushCoins = 0;
    
    int givenProdFibre = 0;
    int givenProdSpore = 0;
    int givenProdSuc = 0;
    int givenProdPhospho = 0;
//    int givenCoins = 0;
    Integer[] givenProds = {
		givenProdFibre,
		givenProdSpore,
		givenProdSuc,
		givenProdPhospho
    };
	
	public Player(String givenName, Color givenColor) {
		playerName = givenName;
		playerColor = givenColor;
		
		// Gestion ressouces
		ressourceLabel = new JTextArea("");
		ressourceLabel.setEditable(false);
		ressourceLabel.setOpaque(false);
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
		prodFibre = getPlayerProd("prodFibre") + givenProdFibre;
		prodSpore = getPlayerProd("prodSpore") + givenProdSpore;
		prodSuc = getPlayerProd("prodSuc") + givenProdSuc;
		prodPhospho = getPlayerProd("prodPhospho") + givenProdPhospho;
		prodCoins = getPlayerProd("prodCoins");
		refreshProdLabel();
	}
	
	public void refreshProdLabel() {
		ressourceLabel.setText("Fibre: +" + prodFibre + ", Spore: +" + prodSpore + ", Suc: +" + prodSuc + ",\n Phosphorite: +"+ prodPhospho + ", MushCoins: " + mushCoins + " (+" + prodCoins + ")");
	}
}
