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
    int temperature;
    
    String building = "";
    String[][] buildingParts = null;
    
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
	
	public void setProdFibre() {
		HashMap<String, String[]> fibreProdRules = new HashMap<String, String[]>();
		String[] Foret0 = {"Foret", "0", "2"};
		String[] Foret1 = {"Foret", "1", "1", "Brume"};
		fibreProdRules.put("Foret0", Foret0);
		fibreProdRules.put("Foret1", Foret1);
		
		prodFibre = 0;
		
		prodFibre += getProdFromHashMap(fibreProdRules);
	}
	
	public void setProdSpore() {
		HashMap<String, String[]> sporeProdRules = new HashMap<String, String[]>();
		String[] Foret0 = {"Foret", "0", "1"};
		String[] Marais0 = {"Marais", "0", "2"};
		String[] Plaine0 = {"Plaine", "0", "1"};
		String[] Port1 = {"Port", "1", "1"};
		sporeProdRules.put("Foret0", Foret0);
		sporeProdRules.put("Marais0", Marais0);
		sporeProdRules.put("Plaine0", Plaine0);
		sporeProdRules.put("Port1", Port1);
		
		prodSpore = 0;
		
		prodSpore += getProdFromHashMap(sporeProdRules);
		
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

