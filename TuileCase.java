package net.codejava;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class TuileCase {
	String terrain = "Brume";
    JButton caseBackground;
    Tuile parentTuile;
    String casePosition;
    
    int xPos;
    int yPos;
    int temperature;
    
    String building = "";
    
    int prodFibre;
    int prodSpore;
    int prodMineral;
    int prodCoins;
    
    //Constructeur
    public TuileCase(Tuile tuileFrom, JLayeredPane panel, String position, int x, int y, boolean isViewer) {
    	parentTuile = tuileFrom;
    	casePosition = position;
    	xPos = x;
    	yPos = y;
    	temperature = parentTuile.temperature;
    	caseBackground = new JButton("" + temperature);
    	caseBackground.setBackground(Color.blue);
    	caseBackground.setFont(new Font("Monospace", Font.PLAIN, 12));
    	// bouton listener
    	if (isViewer) {
//    		caseBackground.setText(position);
    		caseBackground.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent e){
    				changeTerrain();
    				updateFilterView();
    			}
    		});    		
    	} else if (xPos == 1 && yPos == 1) {
//			caseBackground.setBackground(Color.red);
    		caseBackground.setFont(new Font("Monospace", Font.BOLD, 14));
    		caseBackground.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent e){
    				if(Main.gameController.buildMode) {
    					setBuilding();
    				} else {
    					parentTuile.setCopiedTuile(Main.settings.returnActivePlayer().tuileViewer);
    					Main.settings.returnActivePlayer().tuileViewer.randomize();    					
    				}
    			}
    		});
    	} else if (position == "C") {
//    		caseBackground.setBackground(Color.red);
    		caseBackground.setFont(new Font("Monospace", Font.BOLD, 14));
    		caseBackground.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent e){
    				parentTuile.setCopiedTuile(Main.settings.returnActivePlayer().tuileViewer);
    				Main.settings.returnActivePlayer().tuileViewer.randomize();
    				Main.settings.AddDebugLog(parentTuile.xPos + ", " + parentTuile.yPos);
    			}
    		});
    	} else {
    		caseBackground.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent e){
//    				int temperatureCase = getTemperature();
    				if(Main.gameController.buildMode) {
    					setBuilding();
    				} else {
    					Main.settings.AddDebugLog("Case cood is " + xPos + ", " + yPos + ", tuile cood is " + parentTuile.xPos + ", " + parentTuile.yPos);
    				}
    			}
    		});
    	}
    }
    
	
	void setBuilding() {
		String[] directions = {"X+", "X-", "Y+", "Y-"};
		Main.gameController.buildMode = false;
		if (terrain == "Plaine") {
			building = "Ferme";
		}
		if (terrain == "Brume") {
			boolean isCoastal = false;
			for (String direction : directions) {
				TuileCase relativeCase = getRelativeCase(direction);
				if (relativeCase != null && relativeCase.terrain != "Brume") {
					isCoastal = true;
					break;
				}
			}
			if (isCoastal) {
				building = "Port";				
			}
		}
		if (terrain == "Marais") {
			building = "Port";
		}
		for (String direction : directions) {
			TuileCase relativeCase = getRelativeCase(direction);
			if (relativeCase != null) {
				relativeCase.parentTuile.updateProductions();
				relativeCase.parentTuile.owner.updateRessourceInfos();
				relativeCase.updateFilterView();
			}
		}
		updateFilterView();
	}


	void changeTerrain() {
    	setTerrain(Main.settings.getRandomTerrainValue());
	}
	
	public String getTerrain() {
		return terrain;
	}
	
	public void setTerrain(String newTerrain) {
		terrain = newTerrain;
	}
	
	public int getTemperature() {
		// Cassé, to fix to use
		
		int calculatedTemperature = 0;
		int parentTuileTemp = parentTuile.getTemperature();
		
		switch(casePosition) {
			case "C":
				calculatedTemperature = parentTuileTemp;
				break;
			case "N", "E", "S", "O": 
				Tuile tuileACote = parentTuile.getRelativeTuile(casePosition);
				if(tuileACote != null) {
					calculatedTemperature = (parentTuileTemp * 3 + tuileACote.getTemperature())/4;
				} else {
					calculatedTemperature = parentTuileTemp;
				}
//				Main.settings.AddDebugLog("[caseBackground.getTemperature] " + tempAcote);
				break;
			case "NO", "NE", "SO", "SE":
				Tuile tuileACote1 = parentTuile.getRelativeTuile("" + casePosition.charAt(0));
				Tuile tuileACote2 = parentTuile.getRelativeTuile("" + casePosition.charAt(1));
				calculatedTemperature = parentTuileTemp * 2; // + tempAcote1 + tempAcote2)/4;
				int divider = 2;
				if (tuileACote1 != null) {
					calculatedTemperature += tuileACote1.getTemperature();
					divider++;
				}
				if (tuileACote2 != null) {
					calculatedTemperature += tuileACote2.getTemperature();
					divider++;
				}
				calculatedTemperature /= divider;
//				Main.settings.AddDebugLog("[caseBackground.getTemperature] " + );
				break;
			default:
				Main.settings.AddDebugLog("This position is unknown: " + casePosition);
		}
		
		return calculatedTemperature;
	}
	
	public void setTemperature(int temp) {
		temperature = temp;
	}
	
	public void updateTemp() {
		setTemperature(getTemperature());
	}
	
	public int getRessource(String ressourceToGet) {
		switch(ressourceToGet) {
			case "prodFibre":
				return prodFibre;
			case "prodSpore":
				return prodSpore;
			case "prodMineral":
				return prodMineral;
			case "prodCoins":
				return prodCoins;
			default:
				Main.settings.AddDebugLog("This ressource is unknown: " + ressourceToGet);
				return 0;
		}
	}
	
	public void setProdBois() {
		String[] directions = {"X+", "X-", "Y+", "Y-"};
		prodFibre = 0;
		if (terrain == "Foret") {
			prodFibre += 2;			
		}
		if (terrain != "Brume") {
			for (String direction : directions) {
				TuileCase relativeCase = getRelativeCase(direction);
				if (relativeCase != null && relativeCase.terrain == "Foret") {
					prodFibre += 1;
				}
			}
		}
	}
	
	public void setProdNourriture() {
		String[] directions = {"X+", "X-", "Y+", "Y-"};
		prodSpore = 0;
		boolean isWettable = true;
		switch (terrain) {
			case "Foret":
				prodSpore += 1;
				break;
			case "Marais":
				prodSpore += 2;
				break;
			case "Plaine":
				prodSpore += 1;
				break;
			default:
				isWettable = false;
		}
		for (String direction : directions) {
			TuileCase relativeCase = getRelativeCase(direction);
			if(building == "Port" || isWettable) {
				if (relativeCase != null && relativeCase.terrain == "Brume") {
					prodSpore += 1;
				}
			}
			if (building != "") {
				if (relativeCase != null && relativeCase.building == "Ferme") {
					prodSpore += 1;
				}
			}
		}
		switch (building) {
			case "Ferme":
				prodSpore *= 2;
				break;
			case "Port":
				prodSpore += 1;
				break;
		}
	}
	
	public void setProdMineral() {
		String[] directions = {"X+", "X-", "Y+", "Y-"};
		prodMineral = 0;
		switch (terrain) {
			case "Désert":
				prodMineral += 1;
				break;
			case "Hauteurs":
				prodMineral += 1;
				break;
		}
		if (terrain != "Brume") {
			for (String direction : directions) {
				TuileCase relativeCase = getRelativeCase(direction);
				if (relativeCase != null && relativeCase.terrain == "Hauteurs") {
					prodMineral += 1;
				}
			}
		}
	}
	
	public void setProdCoins() {
//		String[] directions = {"X+", "X-", "Y+", "Y-"};
		prodCoins = 0;
		switch (building) {
			case "Port":
				prodCoins += 2;
				break;
			case "Ferme":
				prodCoins += 1;
				break;
		}
//		for (String direction : directions) {
//			caseBackground relativeCase = getRelativeCase(direction);
//			if (relativeCase != null && relativeCase.terrain == "Hauteurs") {
//				prodMineral += 1;
//			}
//		}
	}
	
	TuileCase getRelativeCase(String orientation) {
		TuileCase relativeTuileCase = null;
		
		int relativeTuileCasex = xPos;
		int relativeTuileCasey = yPos;
		Tuile relativeTuileCaseParent = parentTuile;
//		if(isViewTuile) {
//			return relativeTuileCase;
//		}
		
		switch (orientation) {
			case "Y-":
				relativeTuileCasey -= 1;
				break;
			case "X+":
				relativeTuileCasex += 1;
				break;
			case "Y+":
				relativeTuileCasey += 1;
				break;
			case "X-":
				relativeTuileCasex -= 1;
				break;
			default:
				return null;
		}
		if(relativeTuileCasex < 0) {
			relativeTuileCasex = 2;
			relativeTuileCaseParent = parentTuile.getRelativeTuile("X-");
		} else if(relativeTuileCasex > 2) {
			relativeTuileCasex = 0;
			relativeTuileCaseParent = parentTuile.getRelativeTuile("X+");
		}
		if(relativeTuileCasey < 0) {
			relativeTuileCasey = 2;
			relativeTuileCaseParent = parentTuile.getRelativeTuile("Y-");
		} else if(relativeTuileCasey > 2) {
			relativeTuileCasey = 0;
			relativeTuileCaseParent = parentTuile.getRelativeTuile("Y+");
		}
		if (relativeTuileCaseParent == null) {
			return null;
		}
		relativeTuileCase = relativeTuileCaseParent.cases[relativeTuileCasex][relativeTuileCasey];
		return relativeTuileCase;
	}
	
	public void updateFilterView() {
		Main.filterViews.updateCaseView(this);
//		caseBackground.setIcon(null);
//		switch(filter) {
//			case "Player":
//				caseBackground.setText("");
//	//			caseBackground.setVerticalTextPosition(SwingConstants.TOP);
//				Color playerColor = parentTuile.owner.playerColor;
//				caseBackground.setBackground(playerColor);
//				break;			
//			case "Température":
//				caseBackground.setText("" + temperature);
////				caseBackground.setVerticalTextPosition(SwingConstants.TOP);
//				Color color = Main.settings.getTempColor(temperature);
//				caseBackground.setBackground(color);
//				break;
//			case "Biome":
////				caseBackground.setText(casePosition);
//				caseBackground.setText("");
//		    	switch(terrain) {
//			    	case "Brume":
//						caseBackground.setBackground(Color.white);
//					    caseBackground.setIcon(new ImageIcon(Main.settings.imgEau));
//						break;
//			    	case "Plaine":
//						caseBackground.setBackground(Color.yellow);
//						break;
//			    	case "Hauteurs":
//						caseBackground.setBackground(Color.gray);
//						break;
//			    	case "Foret":
//						caseBackground.setBackground(Color.green);
//					    caseBackground.setIcon(new ImageIcon(Main.settings.imgForet));
//						break;
////			    	case "Ville":
////						caseBackground.setBackground(Color.lightGray);
////						break;
//			    	case "Marais":
//						caseBackground.setBackground(Color.pink);
//						break;
//			    	case "Désert":
//						caseBackground.setBackground(Color.orange);
//						break;
//					default:
//						Main.settings.AddDebugLog("This biome is unknown: " + terrain);
//		    	}
//		    	switch (building) {
//		    		case "Ferme":
//					    caseBackground.setIcon(new ImageIcon(Main.settings.imgFarm));
//			    		break;
//		    		case "Port":
//					    caseBackground.setIcon(new ImageIcon(Main.settings.imgPort));
//			    		break;
//		    	}
//		    	break;
//			case "Fibre":
//				caseBackground.setText("" + prodFibre);
//				switch(terrain) {
//		    	case "Brume":
//					caseBackground.setBackground(Color.white);
//					break;
//		    	case "Foret":
//					caseBackground.setBackground(Color.green);
//					break;
//		    	case "Marais":
//					caseBackground.setBackground(Color.pink);
//					break;
//				default:
//					caseBackground.setBackground(Color.lightGray);
//				}
//				break;
//			case "Spore":
//				caseBackground.setText("" + prodSpore);
//				switch(terrain) {
//		    	case "Brume":
//					caseBackground.setBackground(Color.white);
//					break;
//		    	case "Foret":
//					caseBackground.setBackground(Color.green);
//					break;
//		    	case "Plaine":
//					caseBackground.setBackground(Color.yellow);
//					break;
//		    	case "Marais":
//					caseBackground.setBackground(Color.pink);
//					break;
//				default:
//					caseBackground.setBackground(Color.lightGray);
//				}
//				break;
//			case "Minéraux":
//				caseBackground.setText("" + prodMineral);
//				switch(terrain) {
//		    	case "Brume":
//					caseBackground.setBackground(Color.white);
//					break;
//		    	case "Désert":
//					caseBackground.setBackground(Color.orange);
//					break;
//		    	case "Hauteurs":
//					caseBackground.setBackground(Color.gray);
//					break;
//				default:
//					caseBackground.setBackground(Color.lightGray);
//				}
//				break;
//			case "MushCoins":
//				caseBackground.setText("" + prodCoins);
//		    	switch(terrain) {
//			    	case "Brume":
//						caseBackground.setBackground(Color.white);
//						break;
//			    	case "Plaine":
//						caseBackground.setBackground(Color.yellow);
//						break;
//			    	case "Hauteurs":
//						caseBackground.setBackground(Color.gray);
//						break;
//			    	case "Foret":
//						caseBackground.setBackground(Color.green);
//						break;
//			    	case "Marais":
//						caseBackground.setBackground(Color.pink);
//						break;
//			    	case "Désert":
//						caseBackground.setBackground(Color.orange);
//						break;
//					default:
//						Main.settings.AddDebugLog("This biome is unknown: " + terrain);
//		    	}
//		    	break;
//		    default:
//				Main.settings.AddDebugLog("This filter is unknown: " + filter);
//		}
//		caseBackground.setVerticalAlignment(SwingConstants.TOP);

	}
}

