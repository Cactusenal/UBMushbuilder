package net.codejava;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLayeredPane;

public class TuileCase {
	String terrain = "Brume";
    JButton caseBackground;
    Tuile parentTuile;
    String casePosition;
    
    int xPos;
    int yPos;
    
    String building = "";
    String[][] buildingParts = null;
    
    public Object[][] buildingsPowered = {};
    
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
    	caseBackground = new JButton();
    	caseBackground.setBackground(Color.blue);
    	caseBackground.setFont(new Font("Monospace", Font.PLAIN, 12));
    	TuileCase thisCase = this;
    	// bouton listener
    	caseBackground.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Main.gameController.clickOnCarteCase(thisCase, isViewer);
			}
		});
    	if (xPos == 1 && yPos == 1) {
    		caseBackground.setFont(new Font("Monospace", Font.BOLD, 14));
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
		if (terrain == "Désert") {
			building = "Generateur";
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
	
	public void setProdFibre() {
		prodFibre = 0;
		
		prodFibre += getProdFromHashMap(Main.settings.fibreProdRules);
	}
	
	public void setProdSpore() {
		prodSpore = 0;
		
		prodSpore += getProdFromHashMap(Main.settings.sporeProdRules);
		
		switch (building) {
			case "Ferme":
				prodSpore *= 2;
				break;
			case "Port":
				prodSpore *= 2;
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
	
	public int getProdFromHashMap(HashMap<String, String[]> baseProductionRules) {
		int prod = 0;
		String[] possibleBiomes = {"Brume", "Plaine", "Hauteurs", "Foret", "Désert", "Marais"};
        // Convert String Array to List
        List<String> possibleBiomesList = Arrays.asList(possibleBiomes);
        // ruleValuesExample = ["biome/building", "distance", "valeur de production", "terrains,oula,regle,nesappliquepas"];
		
        prod = 0;
		for (String ruleName : baseProductionRules.keySet()) {
			String[] ruleValues = baseProductionRules.get(ruleName);
			
			String caseType = ruleValues[0];
			int distanceValue = Integer.parseInt(ruleValues[1]);
			int prodValue = Integer.parseInt(ruleValues[2]);
			String[] biomeToNotApplyString = ruleValues.length > 3 ? ruleValues[3].split(",") : null;
			
			Boolean isBiome = possibleBiomesList.contains(caseType);
			String typeToCompare = isBiome ? terrain : building;
			List<String> biomesToNotApply = null;
			Boolean applyOnAllBiome = false;				
			if (biomeToNotApplyString != null) {
				biomesToNotApply = Arrays.asList(biomeToNotApplyString);
			}
			else {
				applyOnAllBiome = true;
			}
			
			if (distanceValue == 0 && caseType == typeToCompare) {
				prod += prodValue;
			}
			else if (distanceValue > 0) {
				TuileCase[] tuilesWithinDistance = getCasesFromDistance(distanceValue);
				for (TuileCase relativeCase : tuilesWithinDistance) {
					if (relativeCase != null) {
						typeToCompare = isBiome ? relativeCase.terrain : relativeCase.building;
						if (caseType == typeToCompare && (applyOnAllBiome || !biomesToNotApply.contains(terrain))) {
							prod += prodValue;
						}
					}
				}
			}
		}
		return prod;
	}
	
	TuileCase getRelativeCase(String orientation) {
		TuileCase relativeTuileCase = null;
		
		int relativeTuileCasex = xPos;
		int relativeTuileCasey = yPos;
		Tuile relativeTuileCaseParent = parentTuile;
		
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
	
	public TuileCase[] getCasesFromDistance(int caseDistance) {
		TuileCase[] casesArray = {};
		List<TuileCase> casesList = new ArrayList<TuileCase>(Arrays.asList(casesArray));
		int maxColumn = parentTuile.isViewTuile ? 3 : Main.cartePanel.column * 3;
		int maxLign = parentTuile.isViewTuile ? 3 : Main.cartePanel.lign * 3;
		int xCase = parentTuile.xPos * 3 + xPos;
		int yCase = parentTuile.yPos * 3 + yPos;
		int minX = Math.max(0, xCase - caseDistance);
		int minY = Math.max(0, yCase - caseDistance);
		int maxX = Math.min(maxColumn -1, xCase + caseDistance);
		int maxY = Math.min(maxLign - 1, yCase + caseDistance);

		for (int x = minX; x < maxX + 1; x++) {
			for (int y = minY; y < maxY + 1; y++) {
				// Main.settings.AddDebugLog("[" + x / 3 + ", " + y / 3 + "], [" + x % 3 + ", " + y % 3 + "]");
				if ((Math.abs(x - xCase) + Math.abs(y - yCase)) < (caseDistance + 1) && !(x == xCase && y == yCase)) {
//					 && (!this.parentTuile.isViewTuile || (x / 3 == parentTuile.xPos && y / 3 == parentTuile.yPos))
					if (this.parentTuile.isViewTuile) {
						casesList.add(parentTuile.cases[x][y]);
					}
					else {
						casesList.add(Main.cartePanel.getTuile(x / 3, y / 3).cases[x % 3][y % 3]);
					}
				}
			}
		}
		casesArray = casesList.toArray(casesArray);
//		Main.settings.AddDebugLog("Nombre de cases retournées: " + casesArray.length);
		return casesArray;
	}
	
	public void updateFilterView() {
		Main.filterViews.updateCaseView(this);
	}
}

