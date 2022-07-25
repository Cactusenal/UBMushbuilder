package net.codejava;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class Tuile {
	JLayeredPane tuileBack;
	Player owner;
	
	int xPos;
	int yPos;
	
	TuileCase[][] cases = new TuileCase [3][3];
	Integer nbHabitant = 1;

	boolean isViewTuile;
	
	int ecartCase = 40;
	int tuileDim = ecartCase * 6;
	
	int i = 0;
	int j = 0;

    // Preview
    JFrame infoFrame;
    JDialog infoDialog;    
	
	public Tuile(boolean isViewer, Player ownerData, int x, int y) {
		owner = ownerData;
		
		xPos = x;
		yPos = y;
		isViewTuile = isViewer;
		
		//New panel
		tuileBack = new JLayeredPane();
		tuileBack.setBackground(Color.white);
		tuileBack.setPreferredSize(new Dimension(tuileDim, tuileDim));
		for (int yIndex = 0; yIndex < 3; yIndex++) {
			for (int xIndex = 0; xIndex < 3; xIndex++) {
				TuileCase newCase = new TuileCase(this, tuileBack, Main.settings.directions[xIndex], xIndex, yIndex, isViewer);
	        	cases[xIndex][yIndex] = newCase;
	
	        	setTuileLayout(newCase);
			}
        }
		i = 0;
		j = 0;
	}
	
	void setTuileLayout(TuileCase newCase) {
    	boolean isIso = true;
		
		if(isIso) {
    		int gridx = 2 - j + i;
    		int gridy = j + i;
//    		Main.settings.AddDebugLog("Tuile data: " + gridx + ", " + gridy + ", " + xPos + ", " + yPos);
    		i++;
    		if (i > 2) {
    			i -= 3;
    			j++;
    		}
    		tuileBack.add(newCase.caseBackground); //, JLayeredPane.DEFAULT_LAYER);
    		if (isViewTuile) {
    			newCase.caseBackground.setBounds(gridx*ecartCase / 2, gridy*ecartCase / 2, ecartCase, ecartCase);
    		} else {
    			newCase.caseBackground.setBounds(gridx*ecartCase, gridy*ecartCase, ecartCase * 2, ecartCase * 2);        		    			
    		}
    		
    		// Gestion de la profondeur
        	tuileBack.setLayer(newCase.caseBackground, gridy);
    	} else {
        	tuileBack.add(newCase.caseBackground);
    		tuileBack.setLayout(new GridLayout(3,3));        		
    	}
	}
	
	public void setCopiedTuile(Tuile tuileToCopy) {
		// Set tuile into the right owner tuiles list
		Player formerPlayer = null;
		if (owner != tuileToCopy.owner) {
			if (owner != Main.cartePanel.mondePlayer) {
				owner.placedTuiles.remove(this);
				formerPlayer = owner;
			}
			tuileToCopy.owner.placedTuiles.add(this);
			owner = tuileToCopy.owner;
		}

		// Update Tuile infos
		setTerrain(tuileToCopy.getTerrain());
		updatePopulation();

		updateTuileProductions();
		
		// Update view and surrounding cases
		updateFilterView();
		String[] directions = {"X+", "X-", "Y+", "Y-"};
		for (String direction : directions) {
			Tuile closeTuile = getRelativeTuile(direction);
			if (closeTuile != null) {
				closeTuile.updateTuileProductions();
				closeTuile.updateFilterView();
				closeTuile.owner.updateRessourceInfos();
			}
		}
		
		// Update players infos
		owner.updateRessourceInfos();
		if (formerPlayer != null) {
			formerPlayer.updateRessourceInfos();
		}
	}
	
	public String[][] getTerrain() {
		String[][] terrainGrid = new String[3][3];
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
            	terrainGrid[x][y] = cases[x][y].getTerrain();
            }
        }
		return terrainGrid;
	}
	
	public void setTerrain(String[][] terrainGrid) {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
            	cases[x][y].setTerrain(terrainGrid[x][y]);
            	cases[x][y].building = "";
            	cases[x][y].floorBuildingParts = null;
            	cases[x][y].wallBuildingParts = null;
            	cases[x][y].roofBuildingParts = null;
            	cases[x][y].sucLevel = 0;
            }
        }
	}

	public void randomize(Boolean isViewer) {
        for (TuileCase[] currentCaseLign : cases) {
            for (TuileCase currentCase : currentCaseLign) {
            	currentCase.changeTerrain();
            }
        }
//        int temp = setRandomTemperature();
        if (isViewer) {
        	updateTuileProductions();
        	updateFilterView();        	
        }
//		Main.settings.AddDebugLog("Tuile temp is " + getTemperature());
//		return temp;
	}
	
	public void turnClockWise() {
		String[][] pastTerrain = getTerrain();
		String[][] newTerrain = new String[3][3];
		newTerrain[1][1] = pastTerrain[1][1];
		newTerrain[2][0] = pastTerrain[0][0];
		newTerrain[2][1] = pastTerrain[1][0];
		newTerrain[2][2] = pastTerrain[2][0];
		newTerrain[1][2] = pastTerrain[2][1];
		newTerrain[0][2] = pastTerrain[2][2];
		newTerrain[0][1] = pastTerrain[1][2];
		newTerrain[0][0] = pastTerrain[0][2];
		newTerrain[1][0] = pastTerrain[0][1];

		setTerrain(newTerrain);
		updateFilterView();
	}
	
	Tuile getRelativeTuile(String orientation) {
		
		Tuile relativeTuile = null;
		
		if(isViewTuile) {
			return relativeTuile;
		}
		
		switch (orientation) {
			case "Y-":
				relativeTuile = Main.cartePanel.getTuile(xPos, yPos - 1);
				break;
			case "X+":
				relativeTuile = Main.cartePanel.getTuile(xPos + 1, yPos);
				break;
			case "Y+":
				relativeTuile = Main.cartePanel.getTuile(xPos, yPos + 1);
				break;
			case "X-":
				relativeTuile = Main.cartePanel.getTuile(xPos - 1, yPos);
				break;
			default:
				return null;
		}
		return relativeTuile;
	}
	
	public Tuile[] getTuilesFromDistance(int TuileDistance) {
		Tuile[] tuilesArray = null;
		List<Tuile> tuilesList = new ArrayList<Tuile>(Arrays.asList(tuilesArray));
		int maxColumn = Main.cartePanel.column;
		int maxLign = Main.cartePanel.lign;
		int minX = Math.max(0, xPos - TuileDistance);
		int minY = Math.max(0, yPos - TuileDistance);
		int maxX = Math.min(maxColumn, xPos - TuileDistance);
		int maxY = Math.min(maxLign, yPos - TuileDistance);

		for (int x = minX; x < maxX; x++) {
			for (int y = minY; y < maxY; y++) {
				tuilesList.add(Main.cartePanel.getTuile(x, y));
			}
		}
		tuilesArray = tuilesList.toArray(tuilesArray);
		return tuilesArray;
	}
	
	public void updateTuileProductions() {
		for (TuileCase[] currentCaseLign : cases) {
            for (TuileCase currentCase : currentCaseLign) {
            	currentCase.updateCaseProduction();
            }
        }
	}
	
	public int updatePopulation() {
		nbHabitant = 1;
		for (TuileCase[] currentCaseLign : cases) {
            for (TuileCase currentCase : currentCaseLign) {
            	if (!currentCase.building.equals("")) {
            		nbHabitant++;
            	}
            }
		}
		return nbHabitant;
	}
	
	public int getProduction(String ressourceToGet) {
		if (ressourceToGet.equals("habitant")) {
			return nbHabitant;
		}	
		int ressourceProd = 0;
		for (TuileCase[] currentCaseLign : cases) {
            for (TuileCase currentCase : currentCaseLign) {
            	ressourceProd += currentCase.getRessource(ressourceToGet);
            }
		}
		return ressourceProd;
	}
	
	public void updateFilterView() {
        for (TuileCase[] currentCaseLign : cases) {
            for (TuileCase currentCase : currentCaseLign) {
            	currentCase.updateFilterView();
            }
        }
	}

	// PANEL PREVIEW
	public void showPanelTuileInfo() {
    	infoFrame = new JFrame();
        infoDialog = new JDialog(infoFrame);
        
        updateTuileProductions();
        
        infoDialog.add(new JLabel(owner.playerName + " (" + xPos + ":" + yPos + ")"));
        infoDialog.add(new JLabel(""));	
        infoDialog.add(new JLabel("Fibre: +" + getProduction("prodFibre")));
        infoDialog.add(new JLabel("Spore: +" + getProduction("prodSpore")));
        infoDialog.add(new JLabel("Suc: +" + getProduction("prodSuc")));
        infoDialog.add(new JLabel("Phosphorite: +" + getProduction("prodPhospho")));
        infoDialog.add(new JLabel("MushCoins: +" + getProduction("prodCoins")));
        infoDialog.add(new JLabel(""));
        infoDialog.add(new JLabel("Habitant(s): " + getProduction("habitant")));
        infoDialog.add(new JLabel(""));
        int numberOfLines = 10;
		
		infoDialog.setLayout(new GridLayout(numberOfLines, 1));
		
        infoDialog.setBounds(350, 50, 150, numberOfLines * 30);
        infoDialog.setVisible(true);		
	}

	public void hidePanelTuileInfo() {
		infoDialog.setVisible(false);
		infoDialog.dispose();
		infoDialog.removeAll();		
	}
}
